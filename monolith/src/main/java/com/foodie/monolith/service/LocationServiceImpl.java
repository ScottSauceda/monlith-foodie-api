package com.foodie.monolith.service;

import com.foodie.monolith.exception.LocationNotFoundException;
import com.foodie.monolith.model.Location;
import com.foodie.monolith.repository.LocationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
}
