package com.foodie.monolith.controller;


import com.foodie.monolith.data.RestaurantInformation;
import com.foodie.monolith.exception.RestaurantNotFoundException;
import com.foodie.monolith.exception.UserNotFoundException;
import com.foodie.monolith.model.Restaurant;
import com.foodie.monolith.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000"})
@RestController
@RequestMapping("/restaurant")
public class RestaurantController {

    @Autowired
    RestaurantService restaurantService;

    @GetMapping(value = "/restaurants")
    public ResponseEntity<List<RestaurantInformation>> getRestaurants() throws RestaurantNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(restaurantService.getRestaurants());
        } catch(RestaurantNotFoundException restaurantNotFoundException){
            return new ResponseEntity(restaurantNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/restaurants/{userId}")
    public ResponseEntity<List<RestaurantInformation>> getUserRestaurants(@PathVariable Integer userId) throws UserNotFoundException, RestaurantNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(restaurantService.getUserRestaurants(userId));
        } catch(UserNotFoundException userNotFoundException){
            return new ResponseEntity(userNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(RestaurantNotFoundException restaurantNotFoundException){
            return new ResponseEntity(restaurantNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }



    @GetMapping(value = "/{restaurantId}")
    public ResponseEntity<RestaurantInformation> getRestaurantById(@PathVariable Integer restaurantId) throws RestaurantNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(restaurantService.getRestaurantById(restaurantId));
        } catch(RestaurantNotFoundException restaurantNotFoundException){
            return new ResponseEntity(restaurantNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/create")
    public ResponseEntity<String> createRestaurant(@RequestBody RestaurantInformation newRestaurant) throws Exception {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(restaurantService.createRestaurant(newRestaurant));
        } catch(Exception exception){
            return new ResponseEntity(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @PutMapping(value = "/update/{restaurantId}")
    public ResponseEntity<String> updateRestaurant(@PathVariable Integer restaurantId, @RequestBody RestaurantInformation updateRestaurant) throws RestaurantNotFoundException, UserNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(restaurantService.updateRestaurant(restaurantId, updateRestaurant));
        } catch(RestaurantNotFoundException restaurantNotFoundException){
            return new ResponseEntity(restaurantNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(UserNotFoundException userNotFoundException){
            return new ResponseEntity(userNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/setActive")
    public ResponseEntity<String> setRestaurantActive(@RequestBody RestaurantInformation restaurantInformation) throws RestaurantNotFoundException, UserNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(restaurantService.setRestaurantActive(restaurantInformation));
        } catch(RestaurantNotFoundException restaurantNotFoundException){
            return new ResponseEntity(restaurantNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(UserNotFoundException userNotFoundException){
            return new ResponseEntity(userNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/delete/{restaurantId}")
    public ResponseEntity<String> deleteRestaurant(@PathVariable Integer restaurantId) throws RestaurantNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(restaurantService.deleteRestaurant(restaurantId));
        } catch(RestaurantNotFoundException restaurantNotFoundException){
            return new ResponseEntity(restaurantNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}