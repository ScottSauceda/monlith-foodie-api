package com.foodie.monolith.service;

import com.foodie.monolith.exception.RoleNotFoundException;
import com.foodie.monolith.model.Role;
import com.foodie.monolith.repository.RoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements  RoleService {

    @Autowired
    RoleRepository roleRepository;

    @Transactional
    public List<Role> getRoles() throws RoleNotFoundException {
        List<Role> roles = new ArrayList<Role>();
        if(roleRepository.findAll().isEmpty()){
            throw new RoleNotFoundException("No Roles to return");
        } else {
            List<Role> dbRoles = roleRepository.findAll();
            for(Role role: dbRoles){
                roles.add(role);
            }
            return roles;
        }
    }

    @Transactional
    public Optional<Role> getRoleById(Integer roleId) throws RoleNotFoundException {
        Optional<Role> role = null;
        if(roleRepository.findById(roleId).isEmpty()){
            throw new RoleNotFoundException("Role with Id: " + roleId + " does not exists. Please try again.");
        } else {
            role = roleRepository.findById(roleId);
        }
        return role;
    }

    @Transactional
    public String createRole(Role newRole) {
        Role savedRole = null;
        savedRole = roleRepository.saveAndFlush(newRole);

        if(savedRole.getRoleId() != null){
            return "New Role created with Id: " + savedRole.getRoleId();
        } else {
            return "Something went wrong. Please try again";
        }
    }

    @Transactional
    public String updateRole(Integer roleId, Role updateRole) throws RoleNotFoundException {
        Role dbRole = roleRepository.findById(roleId).orElse(null);;

        if(dbRole == null){
            throw new RoleNotFoundException("Role with Id: " + roleId + "does not exists. Please try again.");
        } else {
            dbRole.setRoleName(updateRole.getRoleName());

            return "Role has been updated successfully";
        }
    }

    @Transactional
    public String deleteRole(Integer roleId) throws RoleNotFoundException {
        Role dbRole = roleRepository.findById(roleId).orElse(null);

        if(dbRole == null){
            throw new RoleNotFoundException("Role with Id: " + roleId + " does not exists. Please try again.");
        } else {
            roleRepository.delete(dbRole);
            return "Role has been deleted successfully";
        }
    }

}