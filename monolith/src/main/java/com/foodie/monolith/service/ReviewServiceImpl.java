package com.foodie.monolith.service;

import com.foodie.monolith.exception.LocationNotFoundException;
import com.foodie.monolith.exception.ReviewNotFoundException;
import com.foodie.monolith.model.Location;
import com.foodie.monolith.model.Review;
import com.foodie.monolith.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewServiceImpl implements  ReviewService {

    @Autowired
    ReviewRepository reviewRepository;

    @Transactional
    public List<Review> getReviews() throws ReviewNotFoundException {
        List<Review> reviews = new ArrayList<Review>();
        if(reviewRepository.findAll().isEmpty()){
            throw new ReviewNotFoundException("No Reviews to return");
        } else {
            List<Review> dbReviews = reviewRepository.findAll();
            for(Review review: dbReviews){
                reviews.add(review);
            }
            return reviews;
        }
    }

}