package com.foodie.monolith.controller;


import com.foodie.monolith.exception.ImageNotFoundException;
import com.foodie.monolith.exception.RestaurantNotFoundException;
import com.foodie.monolith.exception.UserNotFoundException;
import com.foodie.monolith.model.Image;
import com.foodie.monolith.model.Review;
import com.foodie.monolith.service.ImageService;
import com.foodie.monolith.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000"})
@RestController
@RequestMapping("/image")
public class ImageController {

    @Autowired
    ImageService imageService;

    @GetMapping(value = "/images")
    public ResponseEntity<List<Image>> getImages() throws ImageNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(imageService.getImages());
        } catch(ImageNotFoundException imageNotFoundException){
            return new ResponseEntity(imageNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/images/{userId}")
    public ResponseEntity<List<Image>> getUserImages(@PathVariable Integer userId) throws UserNotFoundException, ImageNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(imageService.getUserImages(userId));
        } catch (UserNotFoundException userNotFoundException){
            return new ResponseEntity(userNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(ImageNotFoundException imageNotFoundException){
            return new ResponseEntity(imageNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/{imageId}")
    public ResponseEntity<Optional<Image>> getImageById(@PathVariable Integer imageId) throws ImageNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(imageService.getImageById(imageId));
        } catch(ImageNotFoundException imageNotFoundException){
            return new ResponseEntity(imageNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }




    @PostMapping(value = "/create")
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
    public ResponseEntity<String> deleteImage(@PathVariable Integer imageId) throws ImageNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(imageService.deleteImage(imageId));
        } catch(ImageNotFoundException imageNotFoundException){
            return new ResponseEntity(imageNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/user/create")
    public ResponseEntity<String> createUserImage(@RequestBody Image newImage) throws UserNotFoundException, ImageNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(imageService.createUserImage(newImage));
        } catch (UserNotFoundException userNotFoundException){
            return new ResponseEntity(userNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ImageNotFoundException imageNotFoundException){
            return new ResponseEntity(imageNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/user/delete/{imageId}")
    public ResponseEntity<String> deleteUserImage(@PathVariable Integer imageId) throws ImageNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(imageService.deleteUserImage(imageId));
        } catch(ImageNotFoundException imageNotFoundException){
            return new ResponseEntity(imageNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/restaurant/create/{restaurantId}")
    public ResponseEntity<String> createRestaurantImage(@RequestBody Image newImage, @PathVariable Integer restaurantId) throws UserNotFoundException, ImageNotFoundException, RestaurantNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(imageService.createRestaurantImage(newImage, restaurantId));
        } catch (UserNotFoundException userNotFoundException){
            return new ResponseEntity(userNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ImageNotFoundException imageNotFoundException){
            return new ResponseEntity(imageNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (RestaurantNotFoundException restaurantNotFoundException){
            return new ResponseEntity(restaurantNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/restaurant/delete/{imageId}")
    public ResponseEntity<String> deleteRestaurantImage(@PathVariable Integer imageId) throws ImageNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(imageService.deleteRestaurantImage(imageId));
        } catch(ImageNotFoundException imageNotFoundException){
            return new ResponseEntity(imageNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


}