package com.foodie.monolith.service;

import com.foodie.monolith.exception.ImageNotFoundException;
import com.foodie.monolith.exception.RestaurantNotFoundException;
import com.foodie.monolith.exception.UserNotFoundException;
import com.foodie.monolith.model.Image;
import com.foodie.monolith.model.RestaurantImage;
import com.foodie.monolith.model.UserImage;
import com.foodie.monolith.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    ImageRepository imageRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RestaurantImageRepository restaurantImageRepository;
    @Autowired
    private UserImageRepository userImageRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;

    @Transactional
    public List<Image> getImages() throws ImageNotFoundException {
        List<Image> images = new ArrayList<>();
        if(imageRepository.findAll().isEmpty()){
            throw new ImageNotFoundException("No Images to return");
        } else {
            List<Image> dbImages = imageRepository.findAll();
            for(Image image: dbImages){
                images.add(image);
            }
        }
        return images;
    }

    @Transactional
    public List<Image> getUserImages(Integer userId) throws UserNotFoundException, ImageNotFoundException {
        List<Image> images = new ArrayList<>();
        if(imageRepository.findAll().isEmpty()){
            throw new ImageNotFoundException("No Images to return");
        } else if(imageRepository.findAllByUsersId(userId).isEmpty()){
            throw new UserNotFoundException("No Images for that user to return");
        } else {
            List<Image> dbImages = imageRepository.findAllByUsersId(userId);
            for(Image image: dbImages){
                images.add(image);
            }
            return images;
        }
    }

    @Transactional
    public Optional<Image> getImageById(Integer imageId) throws ImageNotFoundException {
        Optional<Image> image = null;
        if(imageRepository.findById(imageId).isEmpty()){
            throw new ImageNotFoundException("Image with imageId: " + imageId + " image does not exists. Please try again");
        } else {
            image = imageRepository.findById(imageId);
        }
        return image;
    }

    // only creates image, does not link image to restaurant or user profile
    // for testing purposes
    @Transactional
    public String createImage(Image newImage) throws UserNotFoundException, ImageNotFoundException {

        Image savedImage = imageRepository.saveAndFlush(newImage);


        if(userRepository.findById(newImage.getUsersId()) == null){
            throw new UserNotFoundException("User not found. Could not create image.");
        }

        if(imageRepository.findById(newImage.getImgId()) == null){
            throw new ImageNotFoundException("Image not created. Please try again.");
        } else {
            return "New Image created with Id: " + savedImage.getImgId();
        }
    }

    // only deletes image
    // does not delete from a join table: RestaurantImage or UserImage
    // for testing purposes
    @Transactional
    public String deleteImage(Integer imageId) throws ImageNotFoundException {
        Image dbImage = imageRepository.findById(imageId).orElse(null);

        if(imageRepository.findById(imageId) == null){
            throw new ImageNotFoundException("Image with Id: " + imageId + " does not exists. Please try again.");
        } else {
            imageRepository.delete(dbImage);
            return "Image has been deleted successfully";
        }

    }



    // creates UserImage
    // 1. create image on image table
    // 2. grab id from new image and insert into UserImage table
    @Transactional
    public String createUserImage(Image newImage) throws UserNotFoundException, ImageNotFoundException {

//        System.out.println("newImage submitted");
//        System.out.println(newImage.getUsersId());
//        System.out.println(newImage.getImgName());
//        System.out.println(newImage.getImgType());
//        System.out.println(newImage.getImgSrc());

        UserImage dbUserImage = userImageRepository.findByUsersId(newImage.getUsersId()).orElse(null);

        if(dbUserImage != null){
            System.out.println("userId submitted: " + newImage.getUsersId());
            System.out.println("userImageRepo return: " + dbUserImage);
            throw new UserNotFoundException("Profile image already exists for this user. Please erase profile image before uploading a new one.");
        }

        if(userRepository.findById(newImage.getUsersId()) == null){
            throw new UserNotFoundException("User not found. Could not create image.");
        }

        Image savedImage = imageRepository.saveAndFlush(newImage);

        if(imageRepository.findById(newImage.getImgId()) == null){
            throw new ImageNotFoundException("Image not created. Please try again.");
        }


        if(newImage.getImgType().equals("profile")){
            // if uploading profile pic, save to UserImage table
            UserImage savedUserImage = new UserImage();

            UserImage newUserImage = new UserImage();
            newUserImage.setUsersId(newImage.getUsersId());
            newUserImage.setImagesId(savedImage.getImgId());

            savedUserImage = userImageRepository.saveAndFlush(newUserImage);

            if(savedUserImage.getImagesId() == null){
                throw new ImageNotFoundException("Image not assigned to user. Please try again");
            } else {
                return "New Image create with Id: " + savedUserImage.getImagesId() + " for user " + savedUserImage.getUsersId();
            }
        } else {
            return "Type given was not profile. Please try again";
        }
    }

    // delete UserImage
    // 1. delete from UserImage table
    // 2. delete from Image table


    @Transactional
    public String deleteUserImage(Integer imageId) throws ImageNotFoundException {
        UserImage dbUserImage = userImageRepository.findByImagesId(imageId).orElse(null);
        Image dbImage = imageRepository.findById(imageId).orElse(null);

        if(dbUserImage == null){
            throw new ImageNotFoundException("UserImage with Id: " + imageId + "does not exists. Please try again.");
        } else {
            if(dbImage == null){
                throw new ImageNotFoundException("Image with Id: " + imageId + " does not exists. Please try again.");
            } else {
                userImageRepository.delete(dbUserImage);
                imageRepository.delete(dbImage);
                return "Image has been deleted successfully";
            }
        }

    }


    // creates RestaurantImage
    // 1. create image on image table
    // 2. grab id from new image and insert into RestaurantImage table
    @Transactional
    public String createRestaurantImage(Image newImage, Integer restaurantId) throws UserNotFoundException, ImageNotFoundException, RestaurantNotFoundException {

        System.out.println("newImage submitted");
        System.out.println(newImage.getUsersId());
        System.out.println(newImage.getImgName());
        System.out.println(newImage.getImgType());
        System.out.println(newImage.getImgSrc());

        if(userRepository.findById(newImage.getUsersId()) == null){
            throw new UserNotFoundException("User not found. Could not create image.");
        }

        if(restaurantRepository.findById(restaurantId) == null){
            throw new RestaurantNotFoundException("Restaurant not found. Could not RestaurantImage. Please try again.");
        }

        Image savedImage = imageRepository.saveAndFlush(newImage);


        if(imageRepository.findById(newImage.getImgId()) == null){
            throw new ImageNotFoundException("Image not created. Please try again.");
        }

        if(newImage.getImgType().equals("restaurant")){
            // if uploading profile pic, save to UserImage table
            RestaurantImage savedRestaurantImage = new RestaurantImage();

            RestaurantImage newRestaurantImage = new RestaurantImage();
            newRestaurantImage.setRestaurantsId(restaurantId);
            newRestaurantImage.setImagesId(savedImage.getImgId());
            newRestaurantImage.setMain(false);

            savedRestaurantImage = restaurantImageRepository.saveAndFlush(newRestaurantImage);

            if(savedRestaurantImage.getImagesId() == null){
                throw new ImageNotFoundException("Image not assigned to user. Please try again");
            } else {
                return "New Image create with Id: " + savedRestaurantImage.getImagesId() + " for restaurant " + savedRestaurantImage.getRestaurantsId();
            }
        } else {
            return "Type given was not restaurant. Please try again";
        }
    }

    // delete RestaurantImage
    // 1. delete from RestaurantImage table
    // 2. delete from Image table

    @Transactional
    public String deleteRestaurantImage(Integer imageId) throws ImageNotFoundException {
        Image dbImage = imageRepository.findById(imageId).orElse(null);
        RestaurantImage dbRestaurantImage = restaurantImageRepository.findByImagesId(imageId).orElse(null);

        if(dbRestaurantImage == null){
            throw new ImageNotFoundException("RestaurantImage with Id: " + imageId + "does not exists. Please try again.");
        } else {
            if(dbImage == null){
                throw new ImageNotFoundException("Image with Id: " + imageId + " does not exists. Please try again.");
            } else {
                restaurantImageRepository.delete(dbRestaurantImage);
                imageRepository.delete(dbImage);
                return "Image has been deleted successfully";
            }
        }

    }


}