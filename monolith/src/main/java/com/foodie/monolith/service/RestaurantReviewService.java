package com.foodie.monolith.service;

import com.foodie.monolith.model.RestaurantReview;

import java.util.List;
import java.util.Optional;

public interface RestaurantReviewService {
        public List<RestaurantReview> getRestaurantReviews();

    public List<RestaurantReview> getRestaurantReviewsByRestaurantId(Integer restaurantId);

    public String assignReviewToRestaurant(RestaurantReview newRestaurantReview);

    public String deleteRestaurantReview(Integer restaurantReviewId);
}
