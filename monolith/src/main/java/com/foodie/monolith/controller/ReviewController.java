package com.foodie.monolith.controller;

import com.foodie.monolith.exception.ReviewNotFoundException;
import com.foodie.monolith.model.Review;
import com.foodie.monolith.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/review")
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



}