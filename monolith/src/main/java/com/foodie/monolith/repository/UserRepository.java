package com.foodie.monolith.repository;

import com.foodie.monolith.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
//    Optional<User> findTopByUsername(String userName);

    Optional<User> findByUsername(String userName);

    Boolean existsByUsername(String username);


}
