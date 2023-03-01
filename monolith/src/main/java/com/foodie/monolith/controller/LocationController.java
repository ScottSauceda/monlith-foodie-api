package com.foodie.monolith.controller;

import com.foodie.monolith.exception.LocationNotFoundException;
import com.foodie.monolith.exception.UserNotFoundException;
import com.foodie.monolith.model.Location;
import com.foodie.monolith.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

    @GetMapping(value = "/locations/{userId}")
    public ResponseEntity<List<Location>>getUserLocations(@PathVariable Integer userId) throws UserNotFoundException, LocationNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(locationService.getUserLocations(userId));
        } catch(UserNotFoundException userNotFoundException){
            return new ResponseEntity(userNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(LocationNotFoundException locationNotFoundException){
            return new ResponseEntity(locationNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/{locationId}")
    public ResponseEntity<Optional<Location>> getLocationById(@PathVariable Integer locationId) throws LocationNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(locationService.getLocationById(locationId));
        } catch(LocationNotFoundException locationNotFoundException){
            return new ResponseEntity(locationNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/create")
    public ResponseEntity<String> createLocation(@RequestBody Location newLocation) throws Exception {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(locationService.createLocation(newLocation));
        } catch(Exception exception){
            return new ResponseEntity(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @PutMapping(value = "/update/{locationId}")
    public ResponseEntity<String> updateLocation(@PathVariable Integer locationId, @RequestBody Location updateLocation) throws LocationNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(locationService.updateLocation(locationId, updateLocation));
        } catch(LocationNotFoundException locationNotFoundException){
            return new ResponseEntity(locationNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/delete/{locationId}")
    public ResponseEntity<String> deleteLocation(@PathVariable Integer locationId) throws LocationNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(locationService.deleteLocation(locationId));
        } catch(LocationNotFoundException locationNotFoundException){
            return new ResponseEntity(locationNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}