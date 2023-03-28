package com.foodie.monolith.repository;

import com.foodie.monolith.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
//    Optional<UserProfile> findTopByEmail(String email);
    Boolean existsByEmail(String email);
    Boolean existsByPhone(String phine);
}