package com.foodie.monolith.service;

import com.foodie.monolith.exception.UserNotFoundException;
import com.foodie.monolith.model.AssignedRole;
import com.foodie.monolith.repository.AssignedRoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AssignedRoleServiceImpl implements AssignedRoleService {

    @Autowired
    AssignedRoleRepository assignedRoleRepository;

    @Transactional
    public List<AssignedRole> getAssignedRoles() throws RuntimeException {
        List<AssignedRole> assignedRoles = new ArrayList<AssignedRole>();
        if(assignedRoleRepository.findAll().isEmpty()){
            throw new RuntimeException("No AssignedRoles to return");
        } else {
            List<AssignedRole> dbAssignedRoles = assignedRoleRepository.findAll();
            for(AssignedRole assignedRole: dbAssignedRoles){
                assignedRoles.add(assignedRole);
            }
            return assignedRoles;
        }
    }

    @Transactional
    public Optional<AssignedRole> getAssignedRoleByUserId(Integer userId) throws UserNotFoundException {
        Optional<AssignedRole> assignedRole = null;
        if(assignedRoleRepository.findByUsersId(userId).isEmpty()){
            throw new UserNotFoundException("AssignedRole with userId: " + userId + " does not exists. Please try again.");
        } else {
            assignedRole = assignedRoleRepository.findByUsersId(userId);
        }
        return assignedRole;
    }

    @Transactional
    public String assignRoleToUser(AssignedRole newAssignedRole) {
        AssignedRole savedAssignedRole = null;
        savedAssignedRole = assignedRoleRepository.saveAndFlush(newAssignedRole);

        if(savedAssignedRole.getUsersId() != null){
            return "Role assigned to user Id: " + savedAssignedRole.getUsersId();
        } else {
            return "Something went wrong. Please try again";
        }
    }

    @Transactional
    public String deleteAssignedRole(Integer userId) throws UserNotFoundException {
        AssignedRole dbAssignedRole = assignedRoleRepository.findByUsersId(userId).orElse(null);

        if(dbAssignedRole == null){
            throw new UserNotFoundException("AssignedRole with usersId: " + userId + " does not exists. Please try again.");
        } else {
            assignedRoleRepository.delete(dbAssignedRole);
            return "AssignedRole has been deleted successfully";
        }
    }

}