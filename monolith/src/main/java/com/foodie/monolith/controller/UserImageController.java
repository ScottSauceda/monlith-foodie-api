package com.foodie.monolith.controller;

import com.foodie.monolith.exception.ImageNotFoundException;
import com.foodie.monolith.exception.UserNotFoundException;
import com.foodie.monolith.model.UserImage;
import com.foodie.monolith.service.UserImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/userImage")
public class UserImageController {

    @Autowired
    UserImageService userImageService;

    @GetMapping(value = "/userImages")
    public ResponseEntity<List<UserImage>> getUserImages() throws RuntimeException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userImageService.getUserImages());
        } catch(Exception exception){
            return new ResponseEntity(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/{userId}")
    public ResponseEntity<Optional<UserImage>> getUserImageByUserId(@PathVariable Integer userId) throws UserNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userImageService.getUserImageByUserId(userId));
        } catch (UserNotFoundException userNotFoundException) {
            return new ResponseEntity(userNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/assign")
    public ResponseEntity<String> assignImageToUser(@RequestBody UserImage newUserImage) throws ImageNotFoundException, UserNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userImageService.assignImageToUser(newUserImage));
        } catch(ImageNotFoundException imageNotFoundException){
            return new ResponseEntity(imageNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(UserNotFoundException userNotFoundException){
            return new ResponseEntity(userNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @DeleteMapping(value = "/delete/{userId}")
    public ResponseEntity<String> deleteUserImage(@PathVariable Integer userId) throws UserNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userImageService.deleteUserImage(userId));
        } catch(UserNotFoundException userNotFoundException){
            return new ResponseEntity(userNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}