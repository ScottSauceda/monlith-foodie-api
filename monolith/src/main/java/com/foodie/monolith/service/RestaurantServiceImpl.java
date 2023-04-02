package com.foodie.monolith.service;

import com.foodie.monolith.data.RestaurantInformation;
import com.foodie.monolith.exception.NotCurrentUserException;
import com.foodie.monolith.exception.RestaurantNotFoundException;
import com.foodie.monolith.exception.ReviewNotFoundException;
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
    public List<RestaurantInformation> getUserRestaurants(String foodieCookie, Long userId) throws  NotCurrentUserException, RestaurantNotFoundException, UserNotFoundException {
        // User: id, username, password, isActive, userRoles
        // Restaurant:
        // RestaurantInformation:

        // 1. Check if restaurant user exists in our database
        // 2. Check current cookie is valid for user.
        // 3. Check if restaurant exists in database.
        // 4. Check if restaurant exists in database for user.
        // 5. Return restaurants.

        // Capturing newReview user to verify userName matches cookie userName
        User dbUser = userRepository.findById(userId).orElse(null);
        List<RestaurantInformation> restaurants = new ArrayList<RestaurantInformation>();

        // Restaurant list to capture restaurants from database.
        List<Restaurant> dbRestaurants = new ArrayList<>();

        // Check user exists.
        if(userRepository.findById(userId).isEmpty())
            throw new UserNotFoundException("User not found. Could not get reviews.");

        System.out.println("username: ....");
        System.out.println(dbUser.getUsername());
        System.out.println("cookie userName: ....");
        System.out.println(jwtUtils.getUserNameFromJwtToken(foodieCookie));

        // Check user has currently active jwtCookie
        if(!dbUser.getUsername().equals(jwtUtils.getUserNameFromJwtToken(foodieCookie)) ) {
            throw new NotCurrentUserException("User cookie not valid.");
        } else {
            System.out.println("User cookie was valid.");
        }

        // check if any restaurants exists in database
        if(restaurantRepository.findAll().isEmpty())
            throw new RestaurantNotFoundException("No Restaurants to return");

        // check if any restaurants exists in database for given user
        if(restaurantRepository.findAllByOwnerUserId(userId).isEmpty())
            throw new RestaurantNotFoundException("No Restaurants for that user to return");

        dbRestaurants = restaurantRepository.findAllByOwnerUserId(userId);
        for(Restaurant restaurant: dbRestaurants){
            restaurants.add(getRestaurantInformation(restaurant.getRestaurantId()));
        }
        return restaurants;
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
    public String createRestaurant(String foodieCookie, RestaurantInformation newRestaurantInformation) throws RestaurantNotFoundException, NotCurrentUserException, UserNotFoundException {
        // User: id, username, password, isActive, userRoles
        // Restaurant: restaurantId, owner, name, address, city, state, zipCode, isActive, reviews, images
        // RestaurantInformation: restaurantId, name, address, city, state, zipCode, ownerId, isActive, restaurantReviews, restaurantImages

        // 1. Check if restaurant user exists in our database
        // 2. Check current cookie is valid for user.
        // 3. Convert restaurantInformation to restaurant,
        // 4. Check if restaurant was saved to database and return ResponseEntity.OK if successful.


        // Capturing restaurant user to verify userName matches cookie userName
        User dbUser = userRepository.getById(newRestaurantInformation.getOwnerId());
        Restaurant newRestaurant = new Restaurant();

        // Will be used further down to check if our restaurant save was successful.
        Restaurant savedRestaurant = new Restaurant();

        // Check user exists.
        if(userRepository.findById(newRestaurantInformation.getOwnerId()).isEmpty())
            throw new UserNotFoundException("User not found. Could not create restaurant.");

        System.out.println("username: ....");
        System.out.println(dbUser.getUsername());
        System.out.println("cookie userName: ....");
        System.out.println(jwtUtils.getUserNameFromJwtToken(foodieCookie));

        // Check user has currently active jwtCookie
        if(!dbUser.getUsername().equals(jwtUtils.getUserNameFromJwtToken(foodieCookie)) ) {
            throw new NotCurrentUserException("User cookie not valid.");
        } else {
            System.out.println("User cookie was valid.");
        }

        newRestaurant.setOwner(dbUser);
        newRestaurant.setName(newRestaurantInformation.getName());
        newRestaurant.setAddress(newRestaurantInformation.getAddress());
        newRestaurant.setCity(newRestaurantInformation.getCity());
        newRestaurant.setState(newRestaurantInformation.getState());
        newRestaurant.setZipCode(newRestaurantInformation.getZipCode());
        newRestaurant.setActive(newRestaurantInformation.getIsActive());

        // capture the newly created restaurant
        savedRestaurant = restaurantRepository.saveAndFlush(newRestaurant);

        // check saved restaurant exists in database and return message to user
        if(restaurantRepository.findById(savedRestaurant.getRestaurantId()).isEmpty()) {
            throw new RestaurantNotFoundException("Restaurant could not be saved.");
        } else {
            return "New Restaurant created with Id: " + savedRestaurant.getRestaurantId();
        }
    }

    @Transactional
    public String updateRestaurant(String foodieCookie, RestaurantInformation updateRestaurantInformation) throws NotCurrentUserException, RestaurantNotFoundException, UserNotFoundException {
        // User: id, username, password, isActive, userRoles
        // Restaurant: restaurantId, owner, name, address, city, state, zipCode, isActive, reviews, images
        // RestaurantInformation: restaurantId, name, address, city, state, zipCode, ownerId, isActive, restaurantReviews, restaurantImages

        // 1. Check if user exists in database.
        // 2. Check if user profile exists in database.
        // 3. Check current cookie is valid for user.
        // 4. Save updated user profile tp database.
        // 5. Return ResponseEntity.OK and success messaage.

        // Capturing user to verify userName matches cookie UserName
        User dbUser = userRepository.findById(updateRestaurantInformation.getOwnerId()).orElse(null);
        Restaurant dbRestaurant = new Restaurant();

        // Check user exists.
        if(userRepository.findById(updateRestaurantInformation.getOwnerId()).isEmpty())
            throw new UserNotFoundException("User not found. Could not create review.");

        System.out.println("username: ....");
        System.out.println(dbUser.getUsername());
        System.out.println("cookie userName: ....");
        System.out.println(jwtUtils.getUserNameFromJwtToken(foodieCookie));

        // Check user has currently active jwtCookie
        if(!dbUser.getUsername().equals(jwtUtils.getUserNameFromJwtToken(foodieCookie)) ) {
            throw new NotCurrentUserException("User cookie not valid.");
        } else {
            System.out.println("User cookie was valid.");
        }

        // Check restaurant exists.
        if(restaurantRepository.findById(updateRestaurantInformation.getRestaurantId()).isEmpty())
            throw new RestaurantNotFoundException("Restaurant not found. Could not update restaurant.");

        dbRestaurant = restaurantRepository.getById(updateRestaurantInformation.getRestaurantId());
        dbRestaurant.setOwner(userRepository.getById(updateRestaurantInformation.getOwnerId()));
        dbRestaurant.setName(updateRestaurantInformation.getName());
        dbRestaurant.setAddress(updateRestaurantInformation.getAddress());
        dbRestaurant.setCity(updateRestaurantInformation.getCity());
        dbRestaurant.setState(updateRestaurantInformation.getState());
        dbRestaurant.setZipCode(updateRestaurantInformation.getZipCode());

        restaurantRepository.save(dbRestaurant);
        return "Restaurant has been updated successfully";


    }

    @Transactional
    public String setRestaurantActive(String foodieCookie, RestaurantInformation updateRestaurantInformation) throws NotCurrentUserException, RestaurantNotFoundException, UserNotFoundException{
        // User: id, username, password, isActive, userRoles
        // Restaurant: restaurantId, owner, name, address, city, state, zipCode, isActive, reviews, images
        // RestaurantInformation: restaurantId, name, address, city, state, zipCode, ownerId, isActive, restaurantReviews, restaurantImages

        // 1. Check if user exists in database.
        // 2. Check current cookie is valid for user.
        // 3. Check if restaurant exists in database.
        // 4. Save updated user active status to database.
        // 5. Return ResponseEntity.OK and success message.

        // Capturing user to verify userName matches cookie UserName and update user
        User dbUser = userRepository.findById(updateRestaurantInformation.getOwnerId()).orElse(null);
        Restaurant dbRestaurant = new Restaurant();

        // Check user exists.
        if(userRepository.findById(updateRestaurantInformation.getOwnerId()).isEmpty())
            throw new UserNotFoundException("User with Id: " +  updateRestaurantInformation.getOwnerId() + " does not exists.");

        // Check user has currently active jwtCookie
        if(!dbUser.getUsername().equals(jwtUtils.getUserNameFromJwtToken(foodieCookie)) ) {
            throw new NotCurrentUserException("User cookie not valid.");
        } else {
            System.out.println("User cookie was valid.");
        }

        // Check user exists.
        if (restaurantRepository.findById(updateRestaurantInformation.getRestaurantId()).isEmpty())
            throw new RestaurantNotFoundException("Restaurant not found. Could not update restaurant. ");

        dbRestaurant = restaurantRepository.getById(updateRestaurantInformation.getRestaurantId());
        dbRestaurant.setOwner(userRepository.getById(updateRestaurantInformation.getOwnerId()));
        dbRestaurant.setActive(updateRestaurantInformation.getIsActive());

        restaurantRepository.save(dbRestaurant);
        return "Restaurant active status has been updated successfully";
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