package com.foodie.monolith.service;

import com.foodie.monolith.exception.RestaurantNotFoundException;
import com.foodie.monolith.model.Restaurant;
import com.foodie.monolith.repository.RestaurantRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    @Autowired
    RestaurantRepository restaurantRepository;

    @Transactional
    public List<Restaurant> getRestaurants() throws RestaurantNotFoundException {
        List<Restaurant> restaurants = new ArrayList<Restaurant>();
        if(restaurantRepository.findAll().isEmpty()){
            throw new RestaurantNotFoundException("No Restaurants to return");
        } else {
            List<Restaurant> dbRestaurants = restaurantRepository.findAll();
            for(Restaurant restaurant: dbRestaurants){
                restaurants.add(restaurant);
            }
            return restaurants;
        }
    }

}