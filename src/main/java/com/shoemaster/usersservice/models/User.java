package com.shoemaster.usersservice.models;

import com.shoemaster.usersservice.enums.Roles;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long userId;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "account_non_expired")
    private boolean accountNonExpired = true;

    @Column(name = "account_non_locked")
    private boolean accountNonLocked = true;

    @Column(name = "credentials_non_expired")
    private boolean credentialsNonExpired = true;

    @Column
    private boolean enabled = true;

    @Enumerated
    private List<Roles> roles;

    @Column(nullable = true)
    private String fullName;

    @Column(nullable = true)
    private String address;

    @Column(nullable = true)
    private Long card;



}
