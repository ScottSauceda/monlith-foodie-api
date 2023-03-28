package com.foodie.monolith.service;

import com.foodie.monolith.exception.*;
import com.foodie.monolith.model.Image;
import com.foodie.monolith.model.RestaurantImage;
import com.foodie.monolith.model.User;
import com.foodie.monolith.model.UserImage;
import com.foodie.monolith.repository.*;
import com.foodie.monolith.security.jwt.JwtUtils;
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

    @Autowired
    JwtUtils jwtUtils;

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
    public List<Image> getUserImages(Long userId) throws UserNotFoundException, ImageNotFoundException {
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



    @Transactional
    public String createUserImage(String foodieCookie, Image newImage) throws ImageNotFoundException, ImageTypeException, NotCurrentUserException, UserNotFoundException {
//        User: id, username, password, isActive, userRoles
//        Image: imgId, imgName, imgSrc, imgType, usersId
//        UserImage: imagesId, usersId

        // 1. Check if newImage user exists in our database
        // 2. Check current cookie is valid for user.
        // 3. Check if user already has a profile pic, user is only allowed one profile pic.
        // 4. Check image is of type "profile".
        // 5. Save Image to database if all checks pass.
        // 6. Create UserImage using the new image id.
        // 7. Check if userImage was saved to database and return ResponseEntity.OK if successful.

//        System.out.println("newImage submitted");
//        System.out.println(newImage.getUsersId());
//        System.out.println(newImage.getImgName());
//        System.out.println(newImage.getImgType());
//        System.out.println(newImage.getImgSrc());

        // Capturing newImage user to verify userName matches cookie userName
        User dbUser = userRepository.getById(newImage.getUsersId());
        // if uploading profile pic, save to UserImage table
        UserImage newUserImage = new UserImage();

        // Will be used further down to check if our image and userImage save was successful.
        Image savedImage = new Image();
        UserImage savedUserImage = new UserImage();

        // Check user exists.
        if(userRepository.findById(newImage.getUsersId()).isEmpty())
            throw new UserNotFoundException("User not found. Could not create image.");

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


        // Check if user already has a profile pic. We only allow one profile pic.
        if (!userImageRepository.findByUsersId(newImage.getUsersId()).isEmpty()) {
            // System.out.println("userImageRepo return: " + dbUserImage);
            System.out.println("userId submitted: " + newImage.getUsersId());
            throw new ImageTypeException("Profile image already exists for this user. Please erase profile image before uploading a new one.");
        } else {
            System.out.println("User does not have a current profile image.");
        }

        // Check image type is "profile"
        if(!newImage.getImgType().equals("profile"))
            throw new ImageTypeException("Image type not valid.");


        // capture the newly created image
        savedImage = imageRepository.saveAndFlush(newImage);

        // check saved image exists in database
        if(imageRepository.findById(newImage.getImgId()).isEmpty())
            throw new ImageNotFoundException("Image could not be saved.");

        System.out.println("New Image created with Id: " + savedImage.getImgId());

        // populate newUserImage for newly created image
        // uses the new image id
        newUserImage.setUsersId(newImage.getUsersId());
        newUserImage.setImagesId(savedImage.getImgId());
        // capture newly created userImage
        savedUserImage = userImageRepository.saveAndFlush(newUserImage);

        // check saved userImage exists in database and return message to user
        if(userImageRepository.findByImagesId(savedUserImage.getImagesId()).isEmpty()){
            throw new ImageNotFoundException("Image could not be assigned to user.");
        } else {
            return "New Image create with Id: " + savedUserImage.getImagesId() + " for user " + savedUserImage.getUsersId();
        }
    }

    @Transactional
    public String deleteUserImage(String foodieCookie, Image deleteImage) throws ImageNotFoundException, NotCurrentUserException, UserNotFoundException {
        // User: id, username, password, isActive, userRoles
        // Image: imgId, imgName, imgSrc, imgType, usersId
        // UserImage: imagesId, usersId

        // 1. Check if user exists in database.
        // 2. Check current cookie is valid for user.
        // 3. Save updated user active status to database.
        // 4. Return ResponseEntity.OK and success message.

        System.out.println("deleteImage");
        System.out.println("deleteImage imgId");
        System.out.println(deleteImage.getImgId());
        System.out.println("deleteImage userId");
        System.out.println(deleteImage.getUsersId());

        Image dbImage = imageRepository.findById(deleteImage.getImgId()).orElse(null);
        UserImage dbUserImage = userImageRepository.findByImagesId(deleteImage.getImgId()).orElse(null);

        // Capturing user to verify userName matches cookie UserName and update user
        User dbUser = userRepository.getById(deleteImage.getUsersId());

        // Check if image exists.
        if(imageRepository.findById(deleteImage.getImgId()).isEmpty())
            throw new ImageNotFoundException("Image with Id: " + deleteImage.getImgId()+ " does not exists.");

        // Check if userImage exists.
        if(userImageRepository.findByImagesId(deleteImage.getImgId()).isEmpty())
            throw new ImageNotFoundException("UserImage with imgId: " + deleteImage.getImgId() + " does not exists.");

        // Check user exists.
        if(userRepository.findById(dbUser.getUserId()).isEmpty())
            throw new UserNotFoundException("User with Id: " +  deleteImage.getUsersId() + " does not exists.");

        // Check user has currently active jwtCookie
        if(!dbUser.getUsername().equals(jwtUtils.getUserNameFromJwtToken(foodieCookie)) ) {
            throw new NotCurrentUserException("User cookie not valid.");
        } else {
            System.out.println("User cookie was valid.");
        }


        // image must be deleted from userImage table first, then deleted from image table
        userImageRepository.delete(dbUserImage);
        imageRepository.delete(dbImage);

        return "Image has been deleted successfully";
    }


    // creates RestaurantImage
    // 1. create image on image table
    // 2. grab id from new image and insert into RestaurantImage table
    @Transactional
    public String createRestaurantImage(Image newImage, Integer restaurantId, String foodieCookie) throws UserNotFoundException, ImageNotFoundException, RestaurantNotFoundException {

        System.out.println("newImage submitted");
        System.out.println(newImage.getUsersId());
        System.out.println(newImage.getImgName());
        System.out.println(newImage.getImgType());
        System.out.println(newImage.getImgSrc());

        User dbUser = userRepository.findById(newImage.getUsersId()).orElse(null);

        if(!jwtUtils.cookieUserNameMatchesRequestUser(foodieCookie, dbUser.getUsername())){
            throw new UserNotFoundException("cookie does not match user.userName");
        }

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
    public String deleteRestaurantImage(Integer imageId, String foodieCookie) throws ImageNotFoundException {
        Image dbImage = imageRepository.findById(imageId).orElse(null);
        RestaurantImage dbRestaurantImage = restaurantImageRepository.findByImagesId(imageId).orElse(null);

        User dbUser = userRepository.findById(dbImage.getUsersId()).orElse(null);

        if(!jwtUtils.cookieUserNameMatchesRequestUser(foodieCookie, dbUser.getUsername())){
            throw new UserNotFoundException("cookie does not match user.userName");
        }

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