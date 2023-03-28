package com.foodie.monolith.service;

import com.foodie.monolith.data.RestaurantInformation;
import com.foodie.monolith.exception.RestaurantNotFoundException;
import com.foodie.monolith.exception.UserNotFoundException;
import com.foodie.monolith.model.Restaurant;
import com.foodie.monolith.model.User;
import com.foodie.monolith.repository.RestaurantRepository;
import com.foodie.monolith.repository.UserRepository;
import com.foodie.monolith.security.jwt.JwtUtils;
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

    @Autowired
    JwtUtils jwtUtils;

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
    public List<RestaurantInformation> getUserRestaurants(Long userId, String foodieCookie) throws UserNotFoundException, RestaurantNotFoundException {
        List<RestaurantInformation> restaurants = new ArrayList<RestaurantInformation>();

        User dbUser = userRepository.getById(userId);

        if(dbUser != null) {
            System.out.println("username: ....");
            System.out.println(dbUser.getUsername());

            System.out.println("cookie userName: ....");
            System.out.println(jwtUtils.getUserNameFromJwtToken(foodieCookie));

            if (dbUser.getUsername().equals(jwtUtils.getUserNameFromJwtToken(foodieCookie))) {
                System.out.println("userName from cookie matches user.userName");
            } else {
                System.out.println("cookie does not match user.userName");
                throw new UserNotFoundException("cookie does not match user.userName");
            }

        }


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
    public String createRestaurant(RestaurantInformation newRestaurant, String foodieCookie) throws UserNotFoundException {
        Restaurant savedRestaurant = new Restaurant();

        User dbUser = userRepository.getById(newRestaurant.getOwnerId());

        if(dbUser != null) {
            System.out.println("username: ....");
            System.out.println(dbUser.getUsername());

            System.out.println("cookie userName: ....");
            System.out.println(jwtUtils.getUserNameFromJwtToken(foodieCookie));

            if (dbUser.getUsername().equals(jwtUtils.getUserNameFromJwtToken(foodieCookie))) {
                System.out.println("userName from cookie matches user.userName");
            } else {
                System.out.println("cookie does not match user.userName");
                throw new UserNotFoundException("cookie does not match user.userName");
            }

        }


        System.out.println("newRestaurant");
        System.out.println(newRestaurant);

        if(newRestaurant == null){
            System.out.println("new Restaurant is null");
        }

        Long ownerId = newRestaurant.getOwnerId();

        System.out.println("newRestaurant ownerId");
        System.out.println(newRestaurant.getOwnerId());


        if(userRepository.findById(ownerId).isEmpty()){
            throw new UserNotFoundException("#01 Something went wrong. Please try again");
        } else {
            savedRestaurant.setOwner(userRepository.findById(ownerId).orElse(null));
            savedRestaurant.setName(newRestaurant.getName());
            savedRestaurant.setAddress(newRestaurant.getAddress());
            savedRestaurant.setCity(newRestaurant.getCity());
            savedRestaurant.setState(newRestaurant.getState());
            savedRestaurant.setZipCode(newRestaurant.getZipCode());
            savedRestaurant.setActive(newRestaurant.getIsActive());
            savedRestaurant = restaurantRepository.saveAndFlush(savedRestaurant);

            if(savedRestaurant == null){
                System.out.println("savedRestaurant is null");
            }

            if(savedRestaurant.getRestaurantId() != null){
                return "New Restaurant created with Id: " + savedRestaurant.getRestaurantId();
            } else {
                return "Something went wrong. Please try again";
            }
        }

    }

    @Transactional
    public String updateRestaurant(Integer restaurantId, RestaurantInformation updateRestaurant, String foodieCookie) throws RestaurantNotFoundException {
        Restaurant dbRestaurant = null;
        Long ownerId = updateRestaurant.getOwnerId();

        User dbUser = userRepository.getById(updateRestaurant.getOwnerId());

        if(dbUser != null) {
            System.out.println("username: ....");
            System.out.println(dbUser.getUsername());

            System.out.println("cookie userName: ....");
            System.out.println(jwtUtils.getUserNameFromJwtToken(foodieCookie));

            if (dbUser.getUsername().equals(jwtUtils.getUserNameFromJwtToken(foodieCookie))) {
                System.out.println("userName from cookie matches user.userName");
            } else {
                System.out.println("cookie does not match user.userName");
                throw new UserNotFoundException("cookie does not match user.userName");
            }

        } else {
            System.out.println("User was null");
        }


        if(restaurantRepository.findById(restaurantId).isEmpty()){
            throw new RestaurantNotFoundException("#02 Restaurant with Id: " + restaurantId + " does not exists. Please try again.");
        } else {
            if(userRepository.findById(ownerId).isEmpty()){
                throw new UserNotFoundException("#01 Something went wrong. Please try again");
            } else {
                dbRestaurant = restaurantRepository.getById(restaurantId);
                dbRestaurant.setOwner(userRepository.getById(ownerId));
                dbRestaurant.setName(updateRestaurant.getName());
                dbRestaurant.setAddress(updateRestaurant.getAddress());
                dbRestaurant.setCity(updateRestaurant.getCity());
                dbRestaurant.setState(updateRestaurant.getState());
                dbRestaurant.setZipCode(updateRestaurant.getZipCode());
                dbRestaurant.setActive(updateRestaurant.getIsActive());
                restaurantRepository.save(dbRestaurant);

                return "Restaurant has been updated successfully";
            }
        }
    }

    @Transactional
    public String setRestaurantActive(RestaurantInformation restaurantInformation, String foodieCookie) throws RestaurantNotFoundException, UserNotFoundException {
        Restaurant restaurantToUpdate = restaurantRepository.findById(restaurantInformation.getRestaurantId()).orElse(null);
        User user = userRepository.findById(restaurantInformation.getOwnerId()).orElse(null);


        if(user != null) {
            System.out.println("username: ....");
            System.out.println(user.getUsername());

            System.out.println("cookie userName: ....");
            System.out.println(jwtUtils.getUserNameFromJwtToken(foodieCookie));

            if (user.getUsername().equals(jwtUtils.getUserNameFromJwtToken(foodieCookie))) {
                System.out.println("userName from cookie matches user.userName");
            } else {
                System.out.println("cookie does not match user.userName");
                throw new UserNotFoundException("cookie does not match user.userName");
            }

        }


        if (restaurantRepository.findById(restaurantInformation.getRestaurantId()).isEmpty()) {
            throw new RestaurantNotFoundException("Something went wrong. Please try again");
        } else {
            if (userRepository.findById(restaurantInformation.getOwnerId()).isEmpty()) {
                throw new UserNotFoundException("Something went wrong. Please try again");
            } else {
                Restaurant updateRestaurant = restaurantRepository.getById(restaurantInformation.getRestaurantId());
                updateRestaurant.setOwner(user);
                updateRestaurant.setActive(restaurantInformation.getIsActive());
                restaurantRepository.save(updateRestaurant);
                return "Restaurant active status has been updated successfully";
            }
        }
    }



    @Transactional
    public String deleteRestaurant(Integer restaurantId, String foodieCookie) throws RestaurantNotFoundException {
        Restaurant dbRestaurant = restaurantRepository.findById(restaurantId).orElse(null);

        User dbUser = userRepository.getById(dbRestaurant.getOwner().getUserId());

        if(dbUser != null) {
            System.out.println("username: ....");
            System.out.println(dbUser.getUsername());

            System.out.println("cookie userName: ....");
            System.out.println(jwtUtils.getUserNameFromJwtToken(foodieCookie));

            if (dbUser.getUsername().equals(jwtUtils.getUserNameFromJwtToken(foodieCookie))) {
                System.out.println("userName from cookie matches user.userName");
            } else {
                System.out.println("cookie does not match user.userName");
                throw new UserNotFoundException("cookie does not match user.userName");
            }

        }

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
        restaurantInformation.setIsActive(restaurant.isActive());
        // set restaurant owner information
        restaurantInformation.setOwnerId(restaurant.getOwner().getUserId());
        restaurantInformation.setOwnerName(restaurant.getOwner().getUsername());
        // set restaurant location information
        restaurantInformation.setAddress(restaurant.getAddress());
        restaurantInformation.setCity(restaurant.getCity());
        restaurantInformation.setState(restaurant.getState());
        restaurantInformation.setZipCode(restaurant.getZipCode());
        // set restaurantTags information
        if(restaurant.getReviews() != null){
            restaurantInformation.setRestaurantReviews(restaurant.getReviews());
        }

        if(restaurant.getImages() != null){
            restaurantInformation.setRestaurantImages(restaurant.getImages());
        }

        return restaurantInformation;
    }

    @Transactional
    public Restaurant buildRestaurant(RestaurantInformation information){
        Restaurant createdRestaurant = new Restaurant();

        // TODO: Do proper error throwing if Id, name etc are not provided
        createdRestaurant.setOwner(userRepository.getById(information.getOwnerId()));
        createdRestaurant.setName(information.getName());
//        createdRestaurant.setLocation(location);
        // TODO: Set Tags / Reviews

        return createdRestaurant;
    }

}