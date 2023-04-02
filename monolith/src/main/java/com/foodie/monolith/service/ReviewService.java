package com.foodie.monolith.service;

import com.foodie.monolith.model.Image;
import com.foodie.monolith.model.Review;
import com.foodie.monolith.model.UserProfile;

import java.util.List;
import java.util.Optional;

public interface ReviewService {
    public List<Review> getReviews();
    public List<Review> getUserReviews(String foodieCookie, Long userId);

    public Optional<Review> getReviewById(Integer reviewId, String foodieCookie);

    public String createReview(String foodieCookie, Integer restaurantId, Review newReview);

    public String updateReview(String foodieCookie, Review updateReview);

    public String deleteReview(String foodieCookie, Review deleteReview);
}
