package com.foodie.monolith.service;

import com.foodie.monolith.model.RestaurantImage;

import java.util.List;
import java.util.Optional;
public interface RestaurantImageService {
    public List<RestaurantImage> getRestaurantImages();

    public List<RestaurantImage> getRestaurantImagesByRestaurantId(Integer restaurantId);

    public String assignImageToRestaurant(RestaurantImage newRestaurantImage);

    public String deleteRestaurantImage(Integer restaurantImageId);

}
