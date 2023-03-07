package com.foodie.monolith.service;

import com.foodie.monolith.data.RestaurantInformation;
import com.foodie.monolith.model.Restaurant;

import java.util.List;
import java.util.Optional;

public interface RestaurantService {

    public List<RestaurantInformation> getRestaurants();
    List<RestaurantInformation> getUserRestaurants(Integer userId);

    public RestaurantInformation getRestaurantById(Integer restaurantId);

    public String createRestaurant(Restaurant newRestaurant);

    public String updateRestaurant(Integer restaurantId, RestaurantInformation updateRestaurant);

    public String deleteRestaurant(Integer restaurantId);

}
