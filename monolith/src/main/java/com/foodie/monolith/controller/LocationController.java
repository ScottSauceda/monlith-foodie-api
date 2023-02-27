package com.foodie.monolith.controller;

import com.foodie.monolith.exception.LocationNotFoundException;
import com.foodie.monolith.model.Location;
import com.foodie.monolith.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/location")
public class LocationController {

    @Autowired
    LocationService locationService;

    @GetMapping(value = "/locations")
    public ResponseEntity<List<Location>>getLocations() throws LocationNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(locationService.getLocations());
        } catch(LocationNotFoundException locationNotFoundException){
            return new ResponseEntity(locationNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}