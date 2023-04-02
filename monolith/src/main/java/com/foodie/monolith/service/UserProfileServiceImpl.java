package com.foodie.monolith.service;

import com.foodie.monolith.exception.*;
import com.foodie.monolith.model.User;
import com.foodie.monolith.model.UserProfile;
import com.foodie.monolith.repository.UserProfileRepository;
import com.foodie.monolith.repository.UserRepository;
import com.foodie.monolith.security.jwt.JwtUtils;
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

    @Autowired
    JwtUtils jwtUtils;

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
    public Optional<UserProfile> getUserProfileByUserId(Long userId) throws UserProfileNotFoundException {
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
    public String updateUserProfile(String foodieCookie , UserProfile updateUserProfile) throws EmailTakenException, PhoneTakenException, NotCurrentUserException, UserNotFoundException, UserProfileNotFoundException {
        // User: id, username, password, isActive, userRoles
        // UserProfile: usersId, email, phone, firstName, lastName, profileImage

        // 1. Check if user exists in database.
        // 2. Check if user profile exists in database.
        // 3. Check current cookie is valid for user.
        // 4. Check if updateUserProfile email, or phone already exists in our database, since these should all be unique.
        // 5. Save updated user profile tp database.
        // 6. Return ResponseEntity.OK and success message.

         System.out.println("update user profile");
         System.out.println(updateUserProfile.getUsersId());
         System.out.println(updateUserProfile.getEmail());
         System.out.println(updateUserProfile.getPhone());
         System.out.println(updateUserProfile.getFirstName());
         System.out.println(updateUserProfile.getLastName());

        // Capturing user to verify userName matches cookie UserName
        User dbUser = userRepository.findById(updateUserProfile.getUsersId()).orElse(null);
        UserProfile dbUserProfile = userProfileRepository.findById(updateUserProfile.getUsersId()).orElse(null);

        System.out.println("db user profile");
        System.out.println(dbUserProfile.getUsersId());
        System.out.println(dbUserProfile.getEmail());
        System.out.println(dbUserProfile.getPhone());
        System.out.println(dbUserProfile.getFirstName());
        System.out.println(dbUserProfile.getLastName());

        // Check if unique user information already exists in our database

        // Check user exists.
        if(userRepository.findById(updateUserProfile.getUsersId()).isEmpty())
            throw new UserNotFoundException("User with Id: " +  updateUserProfile.getUsersId() + " does not exists.");

        // Check user has profile.
        if(userProfileRepository.findById(updateUserProfile.getUsersId()).isEmpty())
            throw new UserProfileNotFoundException("Profile was not found for given user.");

        System.out.println("username: ....");
        System.out.println(dbUser.getUsername());

        System.out.println("cookie userName: ....");
        System.out.println(jwtUtils.getUserNameFromJwtToken(foodieCookie));

        // Check user has currently active jwtCookie
        if(!dbUser.getUsername().equals(jwtUtils.getUserNameFromJwtToken(foodieCookie)) ) {
            throw new NotCurrentUserException("User cookie not valid.");
        } else {
            System.out.println("User cookie was valid.");
        }

        // check if user has supplied a new email first, then check if the new email already exists in our database
        if(!updateUserProfile.getEmail().equals(dbUserProfile.getEmail())){
            System.out.println("User has supplied a new email");
            if(userProfileRepository.existsByEmail(updateUserProfile.getEmail())) {
                throw new EmailTakenException("Email is already taken!");
            } else {
                System.out.println("Email does not exists in database");
            }
        } else {
            System.out.println("New email does not match original email");
        }

        // check if user has supplied a new email first, then check if the new email already exists in our database
        if(!updateUserProfile.getPhone().equals(dbUserProfile.getPhone())){
            System.out.println("User has supplied a new phone");
            if(userProfileRepository.existsByPhone(updateUserProfile.getPhone())) {
                System.out.println("Phone already exists!");
                throw new PhoneTakenException("Phone is already taken!");
            } else {
                System.out.println("Phone does not exists in database");
            }
        } else {
            System.out.println("New phone does not match original phone");
        }

        dbUserProfile.setEmail(updateUserProfile.getEmail());
        dbUserProfile.setPhone(updateUserProfile.getPhone());
        dbUserProfile.setFirstName(updateUserProfile.getFirstName());
        dbUserProfile.setLastName(updateUserProfile.getLastName());

        userProfileRepository.save(dbUserProfile);
        return "UserProfile has been updated successfully";
    }

    @Transactional
    public String deleteUserProfile(Long userId) throws UserProfileNotFoundException {
        UserProfile dbUserProfile = userProfileRepository.findById(userId).orElse(null);

        if(dbUserProfile == null){
            throw new UserProfileNotFoundException("UserProfile with Id: " + userId + " does not exists. Please try again.");
        } else {
            userProfileRepository.delete(dbUserProfile);
            return "User has been deleted successfully";
        }
    }

}
