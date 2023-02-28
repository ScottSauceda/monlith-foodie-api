package com.foodie.monolith.service;

import com.foodie.monolith.exception.UserProfileNotFoundException;
import com.foodie.monolith.model.UserProfile;
import com.foodie.monolith.repository.UserProfileRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserProfileServiceImpl implements UserProfileService {

    @Autowired
    UserProfileRepository userProfileRepository;

    @Transactional
    public List<UserProfile> getUserProfiles() throws UserProfileNotFoundException {
        List<UserProfile>  userProfiles = new ArrayList<UserProfile>();
        if(userProfileRepository.findAll().isEmpty()){
            throw new UserProfileNotFoundException("No Users to return");
        } else {
            List<UserProfile> dbUserProfiles = userProfileRepository.findAll();
            for(UserProfile userProfile: dbUserProfiles){
                userProfiles.add(userProfile);
            }
            return userProfiles;
        }
    }

}
