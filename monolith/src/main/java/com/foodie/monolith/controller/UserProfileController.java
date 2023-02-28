package com.foodie.monolith.controller;

import com.foodie.monolith.exception.UserProfileNotFoundException;
import com.foodie.monolith.model.UserProfile;
import com.foodie.monolith.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/profile")
public class UserProfileController {

    @Autowired
    UserProfileService userProfileService;

    @GetMapping(value = "/profiles")
    public ResponseEntity<List<UserProfile>> getProfiles() throws UserProfileNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userProfileService.getUserProfiles());
        } catch(UserProfileNotFoundException profileNotFoundException){
            return new ResponseEntity(profileNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    // Get userProfile by userId

    // Create userProfile

    // Edit userProfile

    // Delete userProfile


}