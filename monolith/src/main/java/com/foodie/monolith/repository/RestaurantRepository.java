package com.foodie.monolith.repository;

import com.foodie.monolith.model.Location;
import com.foodie.monolith.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Integer>, JpaSpecificationExecutor<Restaurant> {
    // Useful only for testing as there can be multiple restaurants under the same name
    Optional<Restaurant> findTopByName(String name);

    List<Restaurant> findAllByOwnerId(Integer userId);
}