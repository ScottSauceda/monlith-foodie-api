package com.foodie.monolith.service;

import com.foodie.monolith.exception.ReviewNotFoundException;
import com.foodie.monolith.exception.UserNotFoundException;
import com.foodie.monolith.model.Review;
import com.foodie.monolith.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @Transactional
    public List<Review> getUserReviews(Integer userId) throws UserNotFoundException, ReviewNotFoundException {
        List<Review> reviews = new ArrayList<Review>();
        if(reviewRepository.findAll().isEmpty()){
            throw new ReviewNotFoundException("No Reviews to return");
        } else if(reviewRepository.findAllByUserId(userId).isEmpty()) {
            throw new UserNotFoundException("No Reviews for that user to return");
        } else {
            List<Review> dbReviews = reviewRepository.findAllByUserId(userId);
            for(Review review: dbReviews){
                reviews.add(review);
            }
            return reviews;
        }
    }


    @Transactional
    public Optional<Review> getReviewById(Integer reviewId) throws ReviewNotFoundException {
        Optional<Review> review = null;
        if(reviewRepository.findById(reviewId).isEmpty()){
            throw new ReviewNotFoundException("Review with reviewId: " + reviewId + " does not exists. Please try again.");
        } else {
            review = reviewRepository.findById(reviewId);
        }
        return review;
    }

    @Transactional
    public String createReview(Review newReview) throws ReviewNotFoundException {
        Review savedReview = null;
        savedReview = reviewRepository.saveAndFlush(newReview);

        if(savedReview.getReviewId() != null){
            return "New Review created with Id: " + savedReview.getReviewId();
        } else {
            return "Something went wrong. Please try again";
        }
    }

    @Transactional
    public String updateReview(Integer reviewId, Review updateReview) throws ReviewNotFoundException {
        Review dbReview = reviewRepository.findById(reviewId).orElse(null);;

        if(dbReview == null){
            throw new ReviewNotFoundException("Review with Id: " + reviewId + "does not exists. Please try again.");
        } else {
            dbReview.setRating(updateReview.getRating());
            dbReview.setReviewText(updateReview.getReviewText());

            return "Review has been updated successfully";
        }
    }

    @Transactional
    public String deleteReview(Integer reviewId) throws ReviewNotFoundException {
        Review dbReview = reviewRepository.findById(reviewId).orElse(null);

        if(dbReview == null){
            throw new ReviewNotFoundException("Review with Id: " + reviewId + " does not exists. Please try again.");
        } else {
            reviewRepository.delete(dbReview);
            return "Review has been deleted successfully";
        }
    }

}