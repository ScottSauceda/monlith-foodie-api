package com.foodie.monolith.controller;

import com.foodie.monolith.exception.RoleNotFoundException;
import com.foodie.monolith.model.Role;
import com.foodie.monolith.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    RoleService roleService;

    @GetMapping(value = "/roles")
    public ResponseEntity<List<Role>> getRoles() throws RoleNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(roleService.getRoles());
        } catch(RoleNotFoundException roleNotFoundException){
            return new ResponseEntity(roleNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/{roleId}")
    public ResponseEntity<Optional<Role>> getRoleById(@PathVariable Integer roleId) throws RoleNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(roleService.getRoleById(roleId));
        } catch(RoleNotFoundException roleNotFoundException){
            return new ResponseEntity(roleNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/create")
    public ResponseEntity<String> createRole(@RequestBody Role newRole) throws Exception {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(roleService.createRole(newRole));
        } catch(Exception exception){
            return new ResponseEntity(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/update/{roleId}")
    public ResponseEntity<String> updateRole(@PathVariable Integer roleId, @RequestBody Role updateRole) throws RoleNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(roleService.updateRole(roleId, updateRole));
        } catch(RoleNotFoundException roleNotFoundException){
            return new ResponseEntity(roleNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/delete/{roleId}")
    public ResponseEntity<String> deleteRole(@PathVariable Integer roleId) throws RoleNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(roleService.deleteRole(roleId));
        } catch(RoleNotFoundException roleNotFoundException){
            return new ResponseEntity(roleNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
