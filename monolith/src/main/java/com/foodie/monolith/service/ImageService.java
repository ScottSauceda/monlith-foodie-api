package com.foodie.monolith.service;

import com.foodie.monolith.model.Image;
import com.foodie.monolith.model.Restaurant;

import java.util.List;
import java.util.Optional;
public interface ImageService {
    public List<Image> getImages();

    public List<Image> getUserImages(Long userId);

    public Optional<Image> getImageById(Integer imageId);
    public String createImage(Image newImage);

    public String deleteImage(Integer imageId);

    public String createUserImage(String foodieCookie, Image newImage);

    public String deleteUserImage(String foodieCookie, Image deleteImage);

    public String createRestaurantImage(String foodieCookie, Integer restaurantId, Image newImage);

    //    public String updateMain(Integer reviewId, Review updateReview);

    public String deleteRestaurantImage(String foodieCookie, Integer restaurantId, Image deleteImage);


}