package com.foodie.monolith.model;

import jakarta.persistence.Column;
import jakarta.persistence.Id;

import java.io.Serializable;

public class RestaurantReviewId implements Serializable {

    private Integer reviewsId;

    private Integer restaurantsId;

    public Integer getReviewsId() {
        return reviewsId;
    }

    public void setReviewsId(Integer reviewsId) {
        this.reviewsId = reviewsId;
    }

    public Integer getRestaurantsId() {
        return restaurantsId;
    }

    public void setRestaurantsId(Integer restaurantsId) {
        this.restaurantsId = restaurantsId;
    }
}
