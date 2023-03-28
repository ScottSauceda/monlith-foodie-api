package com.foodie.monolith.controller;

import com.foodie.monolith.exception.RoleNotFoundException;
import com.foodie.monolith.exception.UserNotFoundException;
import com.foodie.monolith.model.AssignedRole;
import com.foodie.monolith.service.AssignedRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/assignedRole")
public class AssignedRoleController {

    @Autowired
    AssignedRoleService assignedRoleService;

    @GetMapping(value = "/assignedRoles")
    public ResponseEntity<List<AssignedRole>> getAssignedRoles() throws RuntimeException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(assignedRoleService.getAssignedRoles());
        } catch (Exception exception) {
            return new ResponseEntity(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/{userId}")
    public ResponseEntity<Optional<AssignedRole>> getAssignedRoleByUserId(@PathVariable Long userId) throws UserNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(assignedRoleService.getAssignedRoleByUserId(userId));
        } catch (UserNotFoundException userNotFoundException) {
            return new ResponseEntity(userNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/assign")
    public ResponseEntity<String> assignRoleToUser(@RequestBody AssignedRole newAssignedRole) throws RoleNotFoundException, UserNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(assignedRoleService.assignRoleToUser(newAssignedRole));
        } catch(RoleNotFoundException roleNotFoundException){
            return new ResponseEntity(roleNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(UserNotFoundException userNotFoundException){
            return new ResponseEntity(userNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @DeleteMapping(value = "/delete/{userId}")
    public ResponseEntity<String> deleteAssignedRole(@PathVariable Long userId) throws UserNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(assignedRoleService.deleteAssignedRole(userId));
        } catch(UserNotFoundException userNotFoundException){
            return new ResponseEntity(userNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}