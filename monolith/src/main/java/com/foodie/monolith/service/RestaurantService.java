package com.foodie.monolith.service;

import com.foodie.monolith.model.Location;
import com.foodie.monolith.model.Restaurant;

import java.util.List;
import java.util.Optional;

public interface RestaurantService {

    public List<Restaurant> getRestaurants();
    List<Restaurant> getUserRestaurants(Integer userId);

    public Optional<Restaurant> getRestaurantById(Integer restaurantId);

    public String createRestaurant(Restaurant newRestaurant);

    public String updateRestaurant(Integer restaurantId, Restaurant updateRestaurant);

    public String deleteRestaurant(Integer restaurantId);

}
