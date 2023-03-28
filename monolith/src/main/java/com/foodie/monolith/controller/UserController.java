package com.foodie.monolith.controller;

import com.foodie.monolith.data.NewUserInformation;
import com.foodie.monolith.data.UserInformation;
import com.foodie.monolith.exception.*;
import com.foodie.monolith.model.User;
import com.foodie.monolith.payload.request.SignupRequest;
import com.foodie.monolith.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userService;

//
    @GetMapping(value = "/users")
    public ResponseEntity<List<User>> getUsers() throws UserNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.getUsers());
        } catch(UserNotFoundException userNotFoundException){
            return new ResponseEntity(userNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/allUserInformation")
    public ResponseEntity<List<UserInformation>> getAllUserInformation() throws Exception {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUserInformation());
        } catch(Exception exception){
            return new ResponseEntity(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/{userId}")
    @PreAuthorize("hasRole('OWNER') or hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<UserInformation> getUserById(@CookieValue("foodie") String foodieCookie, @PathVariable Long userId) throws NotCurrentUserException, UserNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.getUserById(foodieCookie, userId));
        } catch(NotCurrentUserException notCurrentUserException){
            return new ResponseEntity(notCurrentUserException.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(UserNotFoundException userNotFoundException){
            return new ResponseEntity(userNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

//    @PostMapping(value = "/create")
//    public ResponseEntity<String> oldCreateUser(@RequestBody NewUserInformation newUserInformation) throws Exception {
//        try {
//            return ResponseEntity.status(HttpStatus.OK).body(userService.createUser(newUserInformation));
//        } catch(Exception exception){
//            return new ResponseEntity(exception.getMessage(), HttpStatus.BAD_REQUEST);
//        }
//    }


    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@Valid @RequestBody SignupRequest signUpRequest) throws EmailTakenException, PhoneTakenException, RoleNotFoundException, UsernameTakenException, UserNotFoundException, UserProfileNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.registerUser(signUpRequest));
        } catch(EmailTakenException emailTakenException){
            return new ResponseEntity(emailTakenException.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(PhoneTakenException phoneTakenException){
            return new ResponseEntity(phoneTakenException.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(RoleNotFoundException roleNotFoundException){
            return new ResponseEntity(roleNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(UsernameTakenException usernameTakenException){
            return new ResponseEntity(usernameTakenException.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(UserNotFoundException userNotFoundException){
            return new ResponseEntity(userNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(UserProfileNotFoundException userProfileNotFoundException){
            return new ResponseEntity(userProfileNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }



    @PutMapping(value = "/update/{userId}")
    public ResponseEntity<String> updateUser(@PathVariable Long userId, @RequestBody User updateUser) throws UserNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(userId, updateUser));
        } catch(UserNotFoundException userNotFoundException){
            return new ResponseEntity(userNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/setActive")
    @PreAuthorize("hasRole('OWNER') or hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<String> setUserActive(@CookieValue("foodie") String foodieCookie, @RequestBody UserInformation updateUser) throws NotCurrentUserException, UserNotFoundException {
        try {
            System.out.println("reached set user active");
            return ResponseEntity.status(HttpStatus.OK).body(userService.setUserActive(foodieCookie, updateUser));
        } catch(NotCurrentUserException notCurrentUserException){
            return new ResponseEntity(notCurrentUserException.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(UserNotFoundException userNotFoundException){
            return new ResponseEntity(userNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) throws UserNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.deleteUser(userId));
        } catch(UserNotFoundException userNotFoundException){
            return new ResponseEntity(userNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping(value = "/login")
    public ResponseEntity<UserInformation> login(@RequestBody User user) throws UserNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.login(user));
        } catch(UserNotFoundException userNotFoundException){
            return new ResponseEntity(userNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}