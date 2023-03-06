package com.foodie.monolith.service;

import com.foodie.monolith.data.RestaurantInformation;
import com.foodie.monolith.exception.LocationNotFoundException;
import com.foodie.monolith.exception.RestaurantNotFoundException;
import com.foodie.monolith.exception.UserNotFoundException;
import com.foodie.monolith.model.Location;
import com.foodie.monolith.model.Restaurant;
import com.foodie.monolith.repository.RestaurantRepository;
import com.foodie.monolith.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    @Autowired
    RestaurantRepository restaurantRepository;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public List<RestaurantInformation> getRestaurants() throws RestaurantNotFoundException {
        List<RestaurantInformation> restaurants = new ArrayList<RestaurantInformation>();
        if(restaurantRepository.findAll().isEmpty()){
            throw new RestaurantNotFoundException("No Restaurants to return");
        } else {
            List<Restaurant> dbRestaurants = restaurantRepository.findAll();
            for(Restaurant restaurant: dbRestaurants){
                restaurants.add(getRestaurantInformation(restaurant.getRestaurantId()));
            }
            return restaurants;
        }
    }

    @Transactional
    public List<RestaurantInformation> getUserRestaurants(Integer userId) throws UserNotFoundException, RestaurantNotFoundException {
        List<RestaurantInformation> restaurants = new ArrayList<RestaurantInformation>();
        if(restaurantRepository.findAll().isEmpty()){
            throw new RestaurantNotFoundException("No Restaurants to return");
        } else if(restaurantRepository.findAllByOwnerUserId(userId).isEmpty()) {
            throw new UserNotFoundException("No Restaurants to return for that user");
        } else {
            List<Restaurant> dbRestaurants = restaurantRepository.findAllByOwnerUserId(userId);
            for(Restaurant restaurant: dbRestaurants){
                restaurants.add(getRestaurantInformation(restaurant.getRestaurantId()));
            }
            return restaurants;
        }
    }

    @Transactional
    public RestaurantInformation getRestaurantById(Integer restaurantId) throws RestaurantNotFoundException {
        RestaurantInformation restaurant = null;
        if(restaurantRepository.findById(restaurantId).isEmpty()){
            throw new RestaurantNotFoundException("Restaurant with Id: " + restaurantId + " does not exists. Please try again.");
        } else {
            return getRestaurantInformation(restaurantId);
        }
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
//        Restaurant dbRestaurant = restaurantRepository.findById(restaurantId).orElse(null);;
//
//        if(dbRestaurant == null){
//            throw new RestaurantNotFoundException("Restaurant with Id: " + restaurantId + " does not exists. Please try again.");
//        } else {
//            dbRestaurant.setLocationId(updateRestaurant.getLocationId());
//            dbRestaurant.setOwnerId(updateRestaurant.getOwnerId());
//            dbRestaurant.setName(updateRestaurant.getName());
//
            return "Restaurant has been updated successfully";
//        }
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


    @Transactional
    public RestaurantInformation getRestaurantInformation(Integer restaurantId){
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(restaurantId);
        Restaurant restaurant = optionalRestaurant.get();
        RestaurantInformation restaurantInformation = new RestaurantInformation();

        restaurantInformation.setRestaurantId(restaurant.getRestaurantId());
        restaurantInformation.setName(restaurant.getName());
        // set restaurant owner information
        restaurantInformation.setOwner_id(restaurant.getOwner().getUserId());
        restaurantInformation.setOwner_name(restaurant.getOwner().getUsername());
        // set restaurant location information
        restaurantInformation.setLocation_id(restaurant.getLocation().getLocationId());
        restaurantInformation.setLocation_name(restaurant.getLocation().getLocationName());
        restaurantInformation.setAddress(restaurant.getLocation().getAddress());
        restaurantInformation.setCity(restaurant.getLocation().getCity());
        restaurantInformation.setState(restaurant.getLocation().getState());
        restaurantInformation.setZip_code(restaurant.getLocation().getZipCode());
        // set restaurantTags information
        if(restaurant.getReviews() != null){
            restaurantInformation.setRestaurantReviews(restaurant.getReviews());
        }

        return restaurantInformation;
    }

    @Transactional
    public Restaurant buildRestaurant(RestaurantInformation information, Location location){
        Restaurant createdRestaurant = new Restaurant();

        // TODO: Do proper error throwing if Id, name etc are not provided
        createdRestaurant.setOwner(userRepository.getById(information.getOwner_id()));
        createdRestaurant.setName(information.getName());
        createdRestaurant.setLocation(location);
        // TODO: Set Tags / Reviews

        return createdRestaurant;
    }

}