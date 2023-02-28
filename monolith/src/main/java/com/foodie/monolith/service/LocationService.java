package com.foodie.monolith.service;


import com.foodie.monolith.model.Location;

import java.util.List;
import java.util.Optional;

public interface LocationService {
    public List<Location> getLocations();

    public Optional<Location> getLocationById(Integer locationId);

    public String createLocation(Location newLocation);

    public String updateLocation(Integer locationId, Location updateLocation);

    public String deleteLocation(Integer locationId);



}