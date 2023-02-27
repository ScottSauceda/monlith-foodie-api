package com.foodie.monolith.service;

import com.foodie.monolith.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RestaurantServiceImpl {

    @Autowired
    RestaurantRepository restaurantRepository;

}