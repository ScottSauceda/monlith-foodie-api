package com.foodie.monolith.service;

import com.foodie.monolith.model.Review;
import com.foodie.monolith.model.UserProfile;

import java.util.List;
import java.util.Optional;

public interface ReviewService {
    public List<Review> getReviews();

    public List<Review> getUserReviews(Integer userId);

    public Optional<Review> getReviewById(Integer reviewId);

    public String createReview(Review newReview, Integer restaurantId);

    public String updateReview(Integer reviewId, Review updateReview);

    public String deleteReview(Integer reviewId);
}
