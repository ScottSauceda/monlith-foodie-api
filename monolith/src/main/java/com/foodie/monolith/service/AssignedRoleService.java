package com.foodie.monolith.service;


import com.foodie.monolith.model.AssignedRole;

import java.util.List;
import java.util.Optional;

public interface AssignedRoleService {
    public List<AssignedRole> getAssignedRoles();

    public Optional<AssignedRole> getAssignedRoleByUserId(Long userId);

    public String assignRoleToUser(AssignedRole newAssignedRole);

    public String deleteAssignedRole(Long userId);

}