package com.foodie.monolith.controller;

import com.foodie.monolith.exception.ImageNotFoundException;
import com.foodie.monolith.exception.RestaurantNotFoundException;
import com.foodie.monolith.exception.ReviewNotFoundException;
import com.foodie.monolith.model.RestaurantImage;
import com.foodie.monolith.model.RestaurantReview;
import com.foodie.monolith.service.RestaurantImageService;
import com.foodie.monolith.service.RestaurantReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000"})
@RestController
@RequestMapping("/restaurantImage")
public class RestaurantImageController {

    @Autowired
    RestaurantImageService restaurantImageService;

    @GetMapping(value = "/restaurantImages")
    public ResponseEntity<List<RestaurantImage>> getRestaurantImages() throws RuntimeException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(restaurantImageService.getRestaurantImages());
        } catch(Exception exception){
            return new ResponseEntity(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Get images by restaurant
    @GetMapping(value = "/{restaurantId}")
    public ResponseEntity<List<RestaurantImage>> getRestaurantImagesByRestaurantId(@PathVariable Integer restaurantId) throws RestaurantNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(restaurantImageService.getRestaurantImagesByRestaurantId(restaurantId));
        } catch (RestaurantNotFoundException restaurantNotFoundException) {
            return new ResponseEntity(restaurantNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Assign image to restaurant
    @PostMapping(value = "/assign")
    public ResponseEntity<String> assignImageToRestaurant(@RequestBody RestaurantImage newRestaurantImage) throws ImageNotFoundException, RestaurantNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(restaurantImageService.assignImageToRestaurant(newRestaurantImage));
        } catch(ImageNotFoundException imageNotFoundException){
            return new ResponseEntity(imageNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(RestaurantNotFoundException restaurantNotFoundException){
            return new ResponseEntity(restaurantNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Delete image from restaurant
    @DeleteMapping(value = "/delete/{imageId}")
    public ResponseEntity<String> deleteRestaurantImage(@PathVariable Integer imageId) throws ImageNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(restaurantImageService.deleteRestaurantImage(imageId));
        } catch(ImageNotFoundException imageNotFoundException){
            return new ResponseEntity(imageNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


}
