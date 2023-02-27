package com.foodie.monolith.service;

import com.foodie.monolith.exception.RoleNotFoundException;
import com.foodie.monolith.model.Role;
import com.foodie.monolith.repository.RoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

}