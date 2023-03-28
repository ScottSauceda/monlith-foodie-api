package com.foodie.monolith.service;

import com.foodie.monolith.model.Review;
import com.foodie.monolith.model.UserProfile;

import java.util.List;
import java.util.Optional;

public interface ReviewService {
    public List<Review> getReviews();

    public List<Review> getUserReviews(Long userId, String foodieCookie);

    public Optional<Review> getReviewById(Integer reviewId, String foodieCookie);

    public String createReview(String foodieCookie, Integer restaurantId, Review newReview);

    public String updateReview(Integer reviewId, Review updateReview, String foodieCookie);

    public String deleteReview(Integer reviewId, String foodieCookie);
}
