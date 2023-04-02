package com.foodie.monolith.service;

import com.foodie.monolith.data.RestaurantInformation;
import com.foodie.monolith.data.UserInformation;
import com.foodie.monolith.model.Restaurant;

import java.util.List;
import java.util.Optional;

public interface RestaurantService {

    public List<RestaurantInformation> getRestaurants();

    List<RestaurantInformation> getUserRestaurants(String foodieCookie, Long userId);

    public RestaurantInformation getRestaurantById(Integer restaurantId);

    public String createRestaurant(String foodieCookie, RestaurantInformation newRestaurantInformation);

    public String updateRestaurant(String foodieCookie, RestaurantInformation updateRestaurant);

    public String setRestaurantActive(String foodieCookie, RestaurantInformation updateRestaurantInformation);

    public String deleteRestaurant(Integer restaurantId, String foodieCookie);

}
