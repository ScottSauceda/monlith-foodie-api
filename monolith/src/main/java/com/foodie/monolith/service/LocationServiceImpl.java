package com.foodie.monolith.service;

import com.foodie.monolith.exception.LocationNotFoundException;
import com.foodie.monolith.exception.UserNotFoundException;
import com.foodie.monolith.model.Location;
import com.foodie.monolith.repository.LocationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LocationServiceImpl implements LocationService {

    @Autowired
    LocationRepository locationRepository;

    @Transactional
    public List<Location> getLocations() throws LocationNotFoundException {
        List<Location> locations = new ArrayList<Location>();
        if(locationRepository.findAll().isEmpty()){
            throw new LocationNotFoundException("No Locations to return");
        } else {
            List<Location> dbLocations = locationRepository.findAll();
            for(Location location: dbLocations){
                locations.add(location);
            }
            return locations;
        }
    }

    @Transactional
    public List<Location> getUserLocations(Integer userId) throws UserNotFoundException, LocationNotFoundException {
        List<Location> locations = new ArrayList<Location>();
        if(locationRepository.findAll().isEmpty()){
            throw new LocationNotFoundException("No Locations to return");
        } else if(locationRepository.findAllByUserId(userId).isEmpty()) {
            throw new UserNotFoundException("No Locations for that user to return");
        } else {
            List<Location> dbLocations = locationRepository.findAllByUserId(userId);
            for(Location location: dbLocations){
                locations.add(location);
            }
            return locations;
        }
    }

    @Transactional
    public Optional<Location> getLocationById(Integer locationId) throws LocationNotFoundException {
        Optional<Location> location = null;
        if(locationRepository.findById(locationId).isEmpty()){
            throw new LocationNotFoundException("Location with Id: " + locationId + " does not exists. Please try again.");
        } else {
            location = locationRepository.findById(locationId);
        }
        return location;
    }

    @Transactional
    public String createLocation(Location newLocation) {
        Location savedLocation = null;
        savedLocation = locationRepository.saveAndFlush(newLocation);

        if(savedLocation.getLocationId() != null){
            return "New Location created with Id: " + savedLocation.getLocationId();
        } else {
            return "Something went wrong. Please try again";
        }
    }

    @Transactional
    public String updateLocation(Integer locationId, Location updateLocation) throws LocationNotFoundException {
        Location dbLocation = locationRepository.findById(locationId).orElse(null);;

        if(dbLocation == null){
            throw new LocationNotFoundException("Location with Id: " + locationId + "does not exists. Please try again.");
        } else {
            dbLocation.setLocationName(updateLocation.getLocationName());
            dbLocation.setAddress(updateLocation.getAddress());
            dbLocation.setCity(updateLocation.getCity());
            dbLocation.setState(updateLocation.getState());
            dbLocation.setZipCode(updateLocation.getZipCode());

            return "Location has been updated successfully";
        }
    }

    @Transactional
    public String deleteLocation(Integer locationId) throws LocationNotFoundException {
        Location dbLocation = locationRepository.findById(locationId).orElse(null);

        if(dbLocation == null){
            throw new LocationNotFoundException("Location with Id: " + locationId + " does not exists. Please try again.");
        } else {
            locationRepository.delete(dbLocation);
            return "Location has been deleted successfully";
        }
    }

}
