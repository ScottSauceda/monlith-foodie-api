package com.foodie.monolith.repository;

import com.foodie.monolith.model.RestaurantImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RestaurantImageRepository extends JpaRepository<RestaurantImage, Integer> {
   List<RestaurantImage> findAllByRestaurantsId(Integer restaurantId);

   Optional<RestaurantImage> findByImagesId(Integer imageId);
}
