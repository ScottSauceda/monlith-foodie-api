package com.foodie.monolith.controller;

import com.foodie.monolith.exception.*;
import com.foodie.monolith.model.UserProfile;
import com.foodie.monolith.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/profile")
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

    @GetMapping(value = "/{userId}")
    public ResponseEntity<Optional<UserProfile>> getUserProfileByUserId(@PathVariable Long userId) throws UserProfileNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userProfileService.getUserProfileByUserId(userId));
        } catch(UserProfileNotFoundException userProfileNotFoundException){
            return new ResponseEntity(userProfileNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/create")
    public ResponseEntity<String> createUserProfile(@RequestBody UserProfile newUserProfile) throws UserNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userProfileService.createUserProfile(newUserProfile));
        } catch(UserNotFoundException userNotFoundException){
            return new ResponseEntity(userNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/update")
    @PreAuthorize("hasRole('OWNER') or hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<String> updateUserProfile(@CookieValue("foodie") String foodieCookie, @RequestBody UserProfile updateUserProfile) throws EmailTakenException, PhoneTakenException, NotCurrentUserException, UserNotFoundException, UserProfileNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userProfileService.updateUserProfile(foodieCookie, updateUserProfile));
        } catch(EmailTakenException emailTakenException){
            return new ResponseEntity(emailTakenException.getMessage(), HttpStatus.BAD_REQUEST);
        }catch(PhoneTakenException phoneTakenException){
            return new ResponseEntity(phoneTakenException.getMessage(), HttpStatus.BAD_REQUEST);
        }catch(NotCurrentUserException notCurrentUserException){
            return new ResponseEntity(notCurrentUserException.getMessage(), HttpStatus.BAD_REQUEST);
        }catch(UserNotFoundException userNotFoundException){
            return new ResponseEntity(userNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }catch(UserProfileNotFoundException userProfileNotFoundException){
            return new ResponseEntity(userProfileNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/delete/{userId}")
    public ResponseEntity<String> deleteUserProfile(@PathVariable Long userId) throws UserProfileNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userProfileService.deleteUserProfile(userId));
        } catch(UserProfileNotFoundException userProfileNotFoundException){
            return new ResponseEntity(userProfileNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}