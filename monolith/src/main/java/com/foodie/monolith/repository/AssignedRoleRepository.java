package com.foodie.monolith.repository;

import com.foodie.monolith.model.AssignedRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AssignedRoleRepository extends JpaRepository<AssignedRole, Integer> {
    Optional<AssignedRole> findByUsersId(Integer userId);
}
