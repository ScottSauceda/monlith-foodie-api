package com.foodie.monolith.service;

import com.foodie.monolith.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements  RoleService {

    @Autowired
    RoleRepository roleRepository;

}