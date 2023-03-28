package com.foodie.monolith.controller;


import com.foodie.monolith.exception.*;
import com.foodie.monolith.model.Image;
import com.foodie.monolith.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/image")
public class ImageController {

    @Autowired
    ImageService imageService;

    @GetMapping(value = "/images")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Image>> getImages() throws ImageNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(imageService.getImages());
        } catch(ImageNotFoundException imageNotFoundException){
            return new ResponseEntity(imageNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/images/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Image>> getUserImages(@PathVariable Long userId) throws UserNotFoundException, ImageNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(imageService.getUserImages(userId));
        } catch (UserNotFoundException userNotFoundException){
            return new ResponseEntity(userNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(ImageNotFoundException imageNotFoundException){
            return new ResponseEntity(imageNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/{imageId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Optional<Image>> getImageById(@PathVariable Integer imageId) throws ImageNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(imageService.getImageById(imageId));
        } catch(ImageNotFoundException imageNotFoundException){
            return new ResponseEntity(imageNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }




    @PostMapping(value = "/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> createImage(@RequestBody Image newImage) throws UserNotFoundException, ImageNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(imageService.createImage(newImage));
        } catch (UserNotFoundException userNotFoundException){
            return new ResponseEntity(userNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ImageNotFoundException imageNotFoundException){
            return new ResponseEntity(imageNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/delete/{imageId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteImage(@PathVariable Integer imageId) throws ImageNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(imageService.deleteImage(imageId));
        } catch(ImageNotFoundException imageNotFoundException){
            return new ResponseEntity(imageNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/user/create")
    @PreAuthorize("hasRole('USER') or hasRole('OWNER') or hasRole('ADMIN')")
    public ResponseEntity<String> createUserImage(@CookieValue("foodie") String foodieCookie, @RequestBody Image newImage) throws ImageNotFoundException, ImageTypeException, NotCurrentUserException, UserNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(imageService.createUserImage(foodieCookie, newImage));
        } catch (ImageNotFoundException imageNotFoundException){
            return new ResponseEntity(imageNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(ImageTypeException imageTypeException){
            return new ResponseEntity(imageTypeException.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NotCurrentUserException notCurrentUserException){
            return new ResponseEntity(notCurrentUserException.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (UserNotFoundException userNotFoundException){
            return new ResponseEntity(userNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/user/delete")
    @PreAuthorize("hasRole('USER') or hasRole('OWNER') or hasRole('ADMIN')")
    public ResponseEntity<String> deleteUserImage(@CookieValue("foodie") String foodieCookie, @RequestBody Image deleteImage) throws ImageNotFoundException, NotCurrentUserException, UserNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(imageService.deleteUserImage(foodieCookie, deleteImage));
        } catch(ImageNotFoundException imageNotFoundException){
            return new ResponseEntity(imageNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(NotCurrentUserException notCurrentUserException){
            return new ResponseEntity(notCurrentUserException.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(UserNotFoundException userNotFoundException){
            return new ResponseEntity(userNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/restaurant/create/{restaurantId}")
    @PreAuthorize("hasRole('OWNER') or hasRole('ADMIN')")
    public ResponseEntity<String> createRestaurantImage(@RequestBody Image newImage, @PathVariable Integer restaurantId, @CookieValue("foodie") String foodieCookie) throws UserNotFoundException, ImageNotFoundException, RestaurantNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(imageService.createRestaurantImage(newImage, restaurantId, foodieCookie));
        } catch (UserNotFoundException userNotFoundException){
            return new ResponseEntity(userNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ImageNotFoundException imageNotFoundException){
            return new ResponseEntity(imageNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (RestaurantNotFoundException restaurantNotFoundException){
            return new ResponseEntity(restaurantNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/restaurant/delete/{imageId}")
    @PreAuthorize("hasRole('OWNER') or hasRole('ADMIN')")
    public ResponseEntity<String> deleteRestaurantImage(@PathVariable Integer imageId, @CookieValue("foodie") String foodieCookie) throws ImageNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(imageService.deleteRestaurantImage(imageId, foodieCookie));
        } catch(ImageNotFoundException imageNotFoundException){
            return new ResponseEntity(imageNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


}