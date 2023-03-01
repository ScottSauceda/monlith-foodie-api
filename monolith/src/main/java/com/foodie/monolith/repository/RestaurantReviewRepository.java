package com.foodie.monolith.repository;

import com.foodie.monolith.model.RestaurantReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RestaurantReviewRepository extends JpaRepository<RestaurantReview, Integer> {
    List<RestaurantReview> findAllByRestaurantsId(Integer restaurantId);

    Optional<RestaurantReview> findByReviewsId(Integer reviewId);
}