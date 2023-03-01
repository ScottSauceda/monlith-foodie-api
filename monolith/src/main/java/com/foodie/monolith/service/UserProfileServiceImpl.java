package com.foodie.monolith.service;

import com.foodie.monolith.exception.UserNotFoundException;
import com.foodie.monolith.exception.UserProfileNotFoundException;
import com.foodie.monolith.model.User;
import com.foodie.monolith.model.UserProfile;
import com.foodie.monolith.repository.UserProfileRepository;
import com.foodie.monolith.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserProfileServiceImpl implements UserProfileService {

    @Autowired
    UserProfileRepository userProfileRepository;
    @Autowired
    private UserRepository userRepository;

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

    @Transactional
    public Optional<UserProfile> getUserProfileByUserId(Integer userId) throws UserProfileNotFoundException {
        Optional<UserProfile> userProfile = null;
        if(userProfileRepository.findById(userId).isEmpty()){
            throw new UserProfileNotFoundException("UserProfile with userId: " + userId + " does not exists. Please try again.");
        } else {
            userProfile = userProfileRepository.findById(userId);
        }
        return userProfile;
    }

    @Transactional
    public String createUserProfile(UserProfile newUserProfile) throws UserNotFoundException {
        UserProfile savedUserProfile = new UserProfile();
        if(userRepository.findById(newUserProfile.getUsersId()).isEmpty()){
            throw new UserNotFoundException("User not found for userId: " + newUserProfile.getUsersId() + ". Could not create UserProfile");
        } else {
            savedUserProfile = userProfileRepository.saveAndFlush(newUserProfile);
            if(userProfileRepository.findById(newUserProfile.getUsersId()).isEmpty()){
                throw new UserNotFoundException("UserProfile not found for userId: " + newUserProfile.getUsersId() + ". Please try again.");
            } else {
                return "New UserProfile created from userId: " + savedUserProfile.getUsersId();
            }
        }
    }

    @Transactional
    public String updateUserProfile(Integer userId, UserProfile updateUserProfile) throws UserProfileNotFoundException {
        UserProfile dbUserProfile = userProfileRepository.findById(userId).orElse(null);;

        if(dbUserProfile == null){
            throw new UserProfileNotFoundException("UserProfile with Id: " + userId + "does not exists. Please try again.");
        } else {
            dbUserProfile.setFirstName(updateUserProfile.getFirstName());
            dbUserProfile.setLastName(updateUserProfile.getLastName());
            dbUserProfile.setEmail(updateUserProfile.getEmail());
            dbUserProfile.setPhone(updateUserProfile.getPhone());

            return "UserProfile has been updated successfully";
        }
    }

    @Transactional
    public String deleteUserProfile(Integer userId) throws UserProfileNotFoundException {
        UserProfile dbUserProfile = userProfileRepository.findById(userId).orElse(null);

        if(dbUserProfile == null){
            throw new UserProfileNotFoundException("UserProfile with Id: " + userId + " does not exists. Please try again.");
        } else {
            userProfileRepository.delete(dbUserProfile);
            return "User has been deleted successfully";
        }
    }

}
