package com.foodie.monolith.service;

import com.foodie.monolith.model.User;
import com.foodie.monolith.model.UserProfile;

import java.util.List;
import java.util.Optional;

public interface UserProfileService {
    public List<UserProfile> getUserProfiles();

    public Optional<UserProfile> getUserProfileByUserId(Long userId);

    public String createUserProfile(UserProfile newUserProfile);

    public String updateUserProfile(String foodieCookie, UserProfile updateUserProfile);

    public String deleteUserProfile(Long userId);

}
