package com.shoemaster.usersservice.services;

import com.shoemaster.usersservice.enums.Roles;
import com.shoemaster.usersservice.repositories.UserRepository;
import com.shoemaster.usersservice.models.User;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);


    public User getUserById(Long id) {
        Optional<User> optional;
        if ((optional = userRepository.findById(id)).isEmpty()) {
            return null;
        } else {
            return optional.get();
        }
    }

    public User getUserByUsername(String username) {



//        User user = userRepository.findByUsername(username);
//        user.setRoles(List.of(Roles.ROLE_USER, Roles.ROLE_ADMIN));
//        return user;
        return userRepository.findByUsername(username);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Transactional
    public User createNewUser(User user) {
        logger.info("Creating new user: {}", user.getUsername());

        // Encode the user's password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Ensure account is not locked and is enabled
        user.setAccountNonLocked(true);
        user.setEnabled(true);

        // Set other necessary fields like roles, if any
         user.setRoles(Collections.singletonList(Roles.ROLE_ADMIN));

        User savedUser = userRepository.save(user);
        logger.info("User created with ID: {}", savedUser.getUserId());
        return savedUser;
    }

    public User registerUser(User user) {
        // Hash the password
//        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Save user to the database
        return userRepository.save(user);
    }

//    public void registerUser(User user) {
//        if (userRepository.existsByUsername(user.getUsername())) {
//            throw new RuntimeException("Email already in use");
//        }

    public User updateUser(User updatedUser) {
        User user = userRepository.findByUsername(updatedUser.getUsername());
        BeanUtils.copyProperties(updatedUser, user);
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public List<User> getAllUsers       () {
        return userRepository.findAll();
    }

    public void promote(User user) {

    }
}
