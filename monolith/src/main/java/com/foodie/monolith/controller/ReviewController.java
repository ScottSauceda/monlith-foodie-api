package com.foodie.monolith.controller;

import com.foodie.monolith.exception.NotCurrentUserException;
import com.foodie.monolith.exception.RestaurantNotFoundException;
import com.foodie.monolith.exception.ReviewNotFoundException;
import com.foodie.monolith.exception.UserNotFoundException;
import com.foodie.monolith.model.Review;
import com.foodie.monolith.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/review")
public class ReviewController {

    @Autowired
    ReviewService reviewService;

    @GetMapping(value = "/reviews")
    public ResponseEntity<List<Review>> getReviews() throws ReviewNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(reviewService.getReviews());
        } catch(ReviewNotFoundException reviewNotFoundException){
            return new ResponseEntity(reviewNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/reviews/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Review>> getUserReviews(@PathVariable Long userId, @CookieValue("foodie") String foodieCookie) throws UserNotFoundException, ReviewNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(reviewService.getUserReviews(userId, foodieCookie));
        } catch(UserNotFoundException userNotFoundException){
            return new ResponseEntity(userNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(ReviewNotFoundException reviewNotFoundException){
        return new ResponseEntity(reviewNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/{reviewId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Optional<Review>> getReviewById(@PathVariable Integer reviewId, @CookieValue("foodie") String foodieCookie) throws NotCurrentUserException, RestaurantNotFoundException, ReviewNotFoundException, UserNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(reviewService.getReviewById(reviewId, foodieCookie));
        } catch(NotCurrentUserException notCurrentUserException){
            return new ResponseEntity(notCurrentUserException.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(RestaurantNotFoundException restaurantNotFoundException){
            return new ResponseEntity(restaurantNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(ReviewNotFoundException reviewNotFoundException){
            return new ResponseEntity(reviewNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(UserNotFoundException userNotFoundException){
            return new ResponseEntity(userNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/create/{restaurantId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<String> createReview(@CookieValue("foodie") String foodieCookie, @PathVariable Integer restaurantId, @RequestBody Review newReview) throws NotCurrentUserException, RestaurantNotFoundException, ReviewNotFoundException, UserNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(reviewService.createReview(foodieCookie, restaurantId, newReview));
        } catch(NotCurrentUserException notCurrentUserException){
            return new ResponseEntity(notCurrentUserException.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(RestaurantNotFoundException restaurantNotFoundException){
            return new ResponseEntity(restaurantNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(ReviewNotFoundException reviewNotFoundException){
            return new ResponseEntity(reviewNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(UserNotFoundException userNotFoundException){
            return new ResponseEntity(userNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/update/{reviewId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<String> updateReview(@PathVariable Integer reviewId, @RequestBody Review updateReview, @CookieValue("foodie") String foodieCookie) throws ReviewNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(reviewService.updateReview(reviewId, updateReview, foodieCookie));
        } catch(ReviewNotFoundException reviewNotFoundException){
            return new ResponseEntity(reviewNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/delete/{reviewId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<String> deleteReview(@PathVariable Integer reviewId, @CookieValue("foodie") String foodieCookie) throws ReviewNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(reviewService.deleteReview(reviewId, foodieCookie));
        } catch(ReviewNotFoundException reviewNotFoundException){
            return new ResponseEntity(reviewNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}