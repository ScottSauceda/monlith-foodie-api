package com.foodie.monolith.repository;

import com.foodie.monolith.model.Location;
import com.foodie.monolith.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Integer> {
    List<Location> findAllByLocationName(String name);

    List<Location> findAllByUserId(Integer userId);
}