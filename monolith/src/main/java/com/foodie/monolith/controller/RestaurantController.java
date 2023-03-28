package com.foodie.monolith.controller;


import com.foodie.monolith.data.RestaurantInformation;
import com.foodie.monolith.exception.RestaurantNotFoundException;
import com.foodie.monolith.exception.UserNotFoundException;
import com.foodie.monolith.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000"})
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/restaurant")
public class RestaurantController {

//    open -n -a /Applications/Google\ Chrome.app/Contents/MacOS/Google\ Chrome --args --user-data-dir="/tmp/chrome_dev_test" --disable-web-security

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
    @PreAuthorize("hasRole('OWNER') or hasRole('ADMIN')")
    public ResponseEntity<List<RestaurantInformation>> getUserRestaurants(@PathVariable Long userId, @CookieValue("foodie") String foodieCookie) throws UserNotFoundException, RestaurantNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(restaurantService.getUserRestaurants(userId, foodieCookie));
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
    @PreAuthorize("hasRole('OWNER') or hasRole('ADMIN')")
    public ResponseEntity<String> createRestaurant(@RequestBody RestaurantInformation newRestaurant, @CookieValue("foodie") String foodieCookie) throws Exception {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(restaurantService.createRestaurant(newRestaurant, foodieCookie));
        } catch(Exception exception){
            return new ResponseEntity(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/update/{restaurantId}")
    @PreAuthorize("hasRole('OWNER') or hasRole('ADMIN')")
    public ResponseEntity<String> updateRestaurant(@PathVariable Integer restaurantId, @RequestBody RestaurantInformation updateRestaurant, @CookieValue("foodie") String foodieCookie) throws RestaurantNotFoundException, UserNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(restaurantService.updateRestaurant(restaurantId, updateRestaurant, foodieCookie));
        } catch(RestaurantNotFoundException restaurantNotFoundException){
            return new ResponseEntity(restaurantNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(UserNotFoundException userNotFoundException){
            return new ResponseEntity(userNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/setActive")
    @PreAuthorize("hasRole('OWNER') or hasRole('ADMIN')")
    public ResponseEntity<String> setRestaurantActive(@RequestBody RestaurantInformation restaurantInformation, @CookieValue("foodie") String foodieCookie) throws RestaurantNotFoundException, UserNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(restaurantService.setRestaurantActive(restaurantInformation, foodieCookie));
        } catch(RestaurantNotFoundException restaurantNotFoundException){
            return new ResponseEntity(restaurantNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(UserNotFoundException userNotFoundException){
            return new ResponseEntity(userNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/delete/{restaurantId}")
    @PreAuthorize("hasRole('OWNER') or hasRole('ADMIN')")
    public ResponseEntity<String> deleteRestaurant(@PathVariable Integer restaurantId, @CookieValue("foodie") String foodieCookie) throws RestaurantNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(restaurantService.deleteRestaurant(restaurantId, foodieCookie));
        } catch(RestaurantNotFoundException restaurantNotFoundException){
            return new ResponseEntity(restaurantNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}