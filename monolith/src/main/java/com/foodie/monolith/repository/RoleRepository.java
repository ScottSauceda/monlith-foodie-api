package com.foodie.monolith.repository;

import com.foodie.monolith.model.Role;
import com.foodie.monolith.model.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
//    Optional<Role> findTopByRoleName(String roleName);
    Optional<Role> findByRoleName(ERole name);
}
