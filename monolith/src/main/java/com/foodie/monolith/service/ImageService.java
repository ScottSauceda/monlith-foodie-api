package com.foodie.monolith.service;

import com.foodie.monolith.model.Image;
import com.foodie.monolith.model.Restaurant;

import java.util.List;
import java.util.Optional;
public interface ImageService {
    public List<Image> getImages();

    public List<Image> getUserImages(Integer userId);

    public Optional<Image> getImageById(Integer imageId);

    public String createImage(Image newImage);

    public String deleteImage(Integer imageId);

    public String createUserImage(Image newImage);

    public String deleteUserImage(Integer imageId);

    public String createRestaurantImage(Image newImage, Integer restaurantId);

    //    public String updateMain(Integer reviewId, Review updateReview);

    public String deleteRestaurantImage(Integer imageId);


}