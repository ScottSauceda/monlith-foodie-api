package com.foodie.monolith.controller;


import com.foodie.monolith.exception.RestaurantNotFoundException;
import com.foodie.monolith.model.Restaurant;
import com.foodie.monolith.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/restaurant")
public class RestaurantController {

    @Autowired
    RestaurantService restaurantService;

    @GetMapping(value = "/restaurants")
    public ResponseEntity<List<Restaurant>> getRestaurants() throws RestaurantNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(restaurantService.getRestaurants());
        } catch(RestaurantNotFoundException restaurantNotFoundException){
            return new ResponseEntity(restaurantNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Get restaurant by id

    // Create userProfile

    // Edit restaurant

    // Delete restaurant

}