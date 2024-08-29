package com.shoemaster.usersservice.controllers;

import com.shoemaster.usersservice.models.User;
import com.shoemaster.usersservice.repositories.UserRepository;
import com.shoemaster.usersservice.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);


    @PostMapping("/promote")
    public ResponseEntity<?> promote(@RequestBody User user){
        userService.promote(user);
        return ResponseEntity.ok().body(null);
    }

//    @PostMapping("/register")
//    public User registerUser(@RequestBody User user) {
//        return userService.registerUser(user);
//    }


    @PostMapping("/create-account")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            if (userService.existsByUsername(user.getUsername())) {
                logger.warn("Username already in use: {}", user.getUsername());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already in use.");
            }

            User newUser = userService.createNewUser(user);
            logger.info("User registered with ID: {}", newUser.getUserId());
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
        } catch (Exception e) {
            logger.error("Registration failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Registration failed: " + e.getMessage());
        }
    }

    @GetMapping()
    public List<User> getAll(){
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

//    @PostMapping
//    public ResponseEntity<?> createNewUser(@RequestBody User user) {
//        try {
//            User newUser = userService.createNewUser(user);
//            return ResponseEntity.created(URI.create("/user/" + newUser.getId())).body(newUser);
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body(e.getMessage());
//        }
//    }

    @PatchMapping
    public ResponseEntity<?> updateUser(@RequestBody User updatedUser) {
        try {
            return ResponseEntity.ok(userService.updateUser(updatedUser));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

//    @DeleteMapping("/delete/{userId}")
//    public ResponseEntity<?> deleteUserById(@PathVariable Long userId) {
//        try {
//            userService.deleteUser(userId);
//            return ResponseEntity.ok("user with " + userId + " deleted");
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body(e.getMessage());
//        }
//    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<Void> deleteByUserId(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user-list")
    public String displayUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "user-list";
    }

//    @DeleteMapping("/delete/{userId}")
//    public ResponseEntity<Void> deleteUserById(@PathVariable Long userId) {
//        userService.deleteUser(userId);
//        return ResponseEntity.noContent().build();
//
//}
}
