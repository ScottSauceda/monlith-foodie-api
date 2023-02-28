package com.foodie.monolith.service;

import com.foodie.monolith.model.Location;
import com.foodie.monolith.model.Restaurant;

import java.util.List;

public interface RestaurantService {

    public List<Restaurant> getRestaurants();
}
