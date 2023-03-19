package com.foodie.monolith.service;

import com.foodie.monolith.model.UserImage;

import java.util.List;
import java.util.Optional;

public interface UserImageService {

    public List<UserImage> getUserImages();

    public Optional<UserImage> getUserImageByUserId(Integer userId);

    public String assignImageToUser(UserImage newUserImage);

    public String deleteUserImage(Integer userId);

}
