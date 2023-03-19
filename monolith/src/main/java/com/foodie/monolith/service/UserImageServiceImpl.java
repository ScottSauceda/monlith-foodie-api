package com.foodie.monolith.service;

import com.foodie.monolith.exception.ImageNotFoundException;
import com.foodie.monolith.exception.UserNotFoundException;
import com.foodie.monolith.model.UserImage;
import com.foodie.monolith.repository.UserImageRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserImageServiceImpl implements UserImageService {

    @Autowired
    UserImageRepository userImageRepository;

    @Transactional
    public List<UserImage> getUserImages() throws RuntimeException {
        List<UserImage> userImages = new ArrayList<>();
        if(userImageRepository.findAll().isEmpty()){
            throw new RuntimeException("No UserImages to return");
        } else {
            List<UserImage> dbUserImages = userImageRepository.findAll();
            for(UserImage userImage: dbUserImages){
                userImages.add(userImage);
            }
            return userImages;
        }
    }

    @Transactional
    public Optional<UserImage> getUserImageByUserId(Integer userId) throws UserNotFoundException {
        Optional<UserImage> userImage = null;
        if(userImageRepository.findByUsersId(userId).isEmpty()){
            throw new UserNotFoundException("UserImage with userId: " + userId + " does not exists. Please add an image for user");
        } else {
            userImage = userImageRepository.findByUsersId(userId);
        }

        return userImage;
    }

    @Transactional
    public String assignImageToUser(UserImage newUserImage) {
        UserImage savedUserImage = null;
        savedUserImage = userImageRepository.saveAndFlush(newUserImage);

        if(savedUserImage.getUsersId() != null){
            return "Image assigned to user Id: " + savedUserImage.getUsersId();
        } else {
            return "Something went wrong. Please try again";
        }

        // add in checks for UserNotFoundException
        // image not found exception
    }

    @Transactional
    public String deleteUserImage(Integer userId) throws UserNotFoundException {
        UserImage dbUserImage = userImageRepository.findByUsersId(userId).orElse(null);

        if(dbUserImage == null){
            throw new UserNotFoundException("UserImage with usersId: " + userId + " does not exists. Please try again.");
        } else {
            userImageRepository.delete(dbUserImage);
            return "UserImage has been deleted successfully";
        }
    }

}