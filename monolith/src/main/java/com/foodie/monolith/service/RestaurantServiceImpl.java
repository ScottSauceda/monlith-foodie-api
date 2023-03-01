package com.foodie.monolith.service;

import com.foodie.monolith.exception.LocationNotFoundException;
import com.foodie.monolith.exception.RestaurantNotFoundException;
import com.foodie.monolith.model.Location;
import com.foodie.monolith.model.Restaurant;
import com.foodie.monolith.repository.RestaurantRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @Transactional
    public Optional<Restaurant> getRestaurantById(Integer restaurantId) throws RestaurantNotFoundException {
        Optional<Restaurant> restaurant = null;
        if(restaurantRepository.findById(restaurantId).isEmpty()){
            throw new RestaurantNotFoundException("Restaurant with Id: " + restaurantId + " does not exists. Please try again.");
        } else {
            restaurant = restaurantRepository.findById(restaurantId);
        }
        return restaurant;
    }

    @Transactional
    public String createRestaurant(Restaurant newRestaurant) {
        Restaurant savedRestaurant = null;
        savedRestaurant = restaurantRepository.saveAndFlush(newRestaurant);

        if(savedRestaurant.getRestaurantId() != null){
            return "New Restaurant created with Id: " + savedRestaurant.getRestaurantId();
        } else {
            return "Something went wrong. Please try again";
        }
    }

    @Transactional
    public String updateRestaurant(Integer restaurantId, Restaurant updateRestaurant) throws RestaurantNotFoundException {
        Restaurant dbRestaurant = restaurantRepository.findById(restaurantId).orElse(null);;

        if(dbRestaurant == null){
            throw new RestaurantNotFoundException("Restaurant with Id: " + restaurantId + " does not exists. Please try again.");
        } else {
            dbRestaurant.setLocationId(updateRestaurant.getLocationId());
            dbRestaurant.setOwnerId(updateRestaurant.getOwnerId());
            dbRestaurant.setName(updateRestaurant.getName());

            return "Restaurant has been updated successfully";
        }
    }

    @Transactional
    public String deleteRestaurant(Integer restaurantId) throws RestaurantNotFoundException {
        Restaurant dbRestaurant = restaurantRepository.findById(restaurantId).orElse(null);

        if(dbRestaurant == null){
            throw new RestaurantNotFoundException("Restaurant with Id: " + restaurantId + " does not exists. Please try again.");
        } else {
            restaurantRepository.delete(dbRestaurant);
            return "Restaurant has been deleted successfully";
        }
    }

}