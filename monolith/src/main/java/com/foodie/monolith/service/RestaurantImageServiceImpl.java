package com.foodie.monolith.service;

import com.foodie.monolith.exception.ImageNotFoundException;
import com.foodie.monolith.exception.RestaurantNotFoundException;

import com.foodie.monolith.model.RestaurantImage;
import com.foodie.monolith.repository.RestaurantImageRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RestaurantImageServiceImpl implements RestaurantImageService {

    @Autowired
    RestaurantImageRepository restaurantImageRepository;

    @Transactional
    public List<RestaurantImage> getRestaurantImages() throws RuntimeException {
        List<RestaurantImage> restaurantImages = new ArrayList<>();
        if(restaurantImageRepository.findAll().isEmpty()){
            throw new RuntimeException("No ReestaurantImages to return");
        } else {
            List<RestaurantImage> dbRestaurantImages = restaurantImageRepository.findAll();
            for(RestaurantImage restaurantImage: dbRestaurantImages){
                restaurantImages.add(restaurantImage);
            }
            return restaurantImages;
        }
    }

    @Transactional
    public List<RestaurantImage> getRestaurantImagesByRestaurantId(Integer restaurantId) throws RestaurantNotFoundException {
        List<RestaurantImage> restaurantImage = null;
        if(restaurantImageRepository.findAllByRestaurantsId(restaurantId).isEmpty()){
            throw new RestaurantNotFoundException("RestaurantReview with restaurantId: " + restaurantId + " does not exists. Please try again.");
        } else {
            restaurantImage = restaurantImageRepository.findAllByRestaurantsId(restaurantId);
        }
        return restaurantImage;
    }

    @Transactional
    public String assignImageToRestaurant(RestaurantImage newRestaurantImage) {
        RestaurantImage savedRestaurantImage = null;
        savedRestaurantImage = restaurantImageRepository.saveAndFlush(newRestaurantImage);

        if(savedRestaurantImage.getImagesId() != null){
            return "Image assigned to restaurantId: " + savedRestaurantImage.getRestaurantsId();
        } else {
            return "Something went wrong. Please try again";
        }
    }

    @Transactional
    public String deleteRestaurantImage(Integer imageId) throws ImageNotFoundException {
        RestaurantImage dbRestaurantImage = restaurantImageRepository.findByImagesId(imageId).orElse(null);


        if(dbRestaurantImage == null){
            throw new ImageNotFoundException("RestaurantImage with imageId: " + imageId + " does not exists. Please try again.");
        } else {
            restaurantImageRepository.delete(dbRestaurantImage);
            return "RestaurantImage has been deleted successfully";
        }
    }

}