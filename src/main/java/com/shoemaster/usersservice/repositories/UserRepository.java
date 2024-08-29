package com.shoemaster.usersservice.repositories;

import com.shoemaster.usersservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository <User, Long> {

        boolean existsByUsername(String username);

    User findByUsername(String username);
}
