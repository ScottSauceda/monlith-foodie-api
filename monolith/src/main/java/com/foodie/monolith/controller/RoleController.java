package com.foodie.monolith.controller;

import com.foodie.monolith.exception.RoleNotFoundException;
import com.foodie.monolith.model.Role;
import com.foodie.monolith.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
