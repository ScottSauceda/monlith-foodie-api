package com.foodie.monolith.model;

import jakarta.persistence.Column;
import jakarta.persistence.Id;

import java.io.Serializable;

public class RestaurantImageId implements Serializable {

    private Integer imagesId;

    private Integer restaurantsId;

    public Integer getImagesId() { return imagesId; }

    public void setImagesId(Integer imagesId) { this.imagesId = imagesId; }

    public Integer getRestaurantsId(){ return restaurantsId; };

    public void setRestaurantsId(Integer restaurantsId) {this.restaurantsId = restaurantsId;}

}