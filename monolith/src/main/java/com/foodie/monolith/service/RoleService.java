package com.foodie.monolith.service;

import com.foodie.monolith.model.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    public List<Role> getRoles();

    public Optional<Role> getRoleById(Integer roleId);

    public String createRole(Role newRole);

    public String updateRole(Integer roleId, Role updateRole);

    public String deleteRole(Integer roleId);
}
