package com.foodie.monolith.controller;

import com.foodie.monolith.exception.RestaurantNotFoundException;
import com.foodie.monolith.exception.ReviewNotFoundException;
import com.foodie.monolith.model.RestaurantReview;
import com.foodie.monolith.service.RestaurantReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/restaurantReview")
public class RestaurantReviewController {

    @Autowired
    RestaurantReviewService restaurantReviewService;

    @GetMapping(value = "/restaurantReviews")
    public ResponseEntity<List<RestaurantReview>> getRestaurantReviews() throws RuntimeException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(restaurantReviewService.getRestaurantReviews());
        } catch (Exception exception) {
            return new ResponseEntity(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/{restaurantId}")
    public ResponseEntity<List<RestaurantReview>> getRestaurantReviewsByRestaurantId(@PathVariable Integer restaurantId) throws RestaurantNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(restaurantReviewService.getRestaurantReviewsByRestaurantId(restaurantId));
        } catch (RestaurantNotFoundException restaurantNotFoundException) {
            return new ResponseEntity(restaurantNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/assign")
    public ResponseEntity<String> assignReviewToRestaurant(@RequestBody RestaurantReview newRestaurantReview) throws ReviewNotFoundException, RestaurantNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(restaurantReviewService.assignReviewToRestaurant(newRestaurantReview));
        } catch(ReviewNotFoundException reviewNotFoundException){
            return new ResponseEntity(reviewNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(RestaurantNotFoundException restaurantNotFoundException){
            return new ResponseEntity(restaurantNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/delete/{reviewId}")
    public ResponseEntity<String> deleteRestaurantReview(@PathVariable Integer reviewId) throws ReviewNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(restaurantReviewService.deleteRestaurantReview(reviewId));
        } catch(ReviewNotFoundException reviewNotFoundException){
            return new ResponseEntity(reviewNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}