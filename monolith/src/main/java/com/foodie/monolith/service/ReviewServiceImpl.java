package com.foodie.monolith.service;

import com.foodie.monolith.exception.*;
import com.foodie.monolith.model.*;
import com.foodie.monolith.repository.RestaurantRepository;
import com.foodie.monolith.repository.RestaurantReviewRepository;
import com.foodie.monolith.repository.ReviewRepository;
import com.foodie.monolith.repository.UserRepository;
import com.foodie.monolith.security.jwt.JwtUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements  ReviewService {

    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private RestaurantReviewRepository restaurantReviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    JwtUtils jwtUtils;

    @Transactional
    public List<Review> getReviews() throws ReviewNotFoundException {
        List<Review> reviews = new ArrayList<Review>();
        if(reviewRepository.findAll().isEmpty()){
            throw new ReviewNotFoundException("No Reviews to return");
        } else {
            List<Review> dbReviews = reviewRepository.findAll();
            for(Review review: dbReviews){
                reviews.add(review);
            }
            return reviews;
        }
    }

    @Transactional
    public List<Review> getUserReviews(String foodieCookie, Long userId) throws NotCurrentUserException, ReviewNotFoundException, UserNotFoundException {
        // User: id, username, password, isActive, userRoles
        // Review: reviewId, timeCreated, rating, reviewText, userId, userName

        // 1. Check if review user exists in our database
        // 2. Check current cookie is valid for user.
        // 3. Check if reviews exists in database.
        // 4. Check if reviews exists in database for user.
        // 5. Return reviews.

        // Capturing newReview user to verify userName matches cookie userName
        User dbUser = userRepository.findById(userId).orElse(null);
        List<Review> reviews = new ArrayList<Review>();

        // Check user exists.
        if(userRepository.findById(userId).isEmpty())
            throw new UserNotFoundException("User not found. Could not get reviews.");

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

        // check if any reviews exists in database
        if(reviewRepository.findAll().isEmpty())
            throw new ReviewNotFoundException("No Reviews to return");

        // check if any reviews exists in database for given user
        if(reviewRepository.findAllByUserId(userId).isEmpty())
            throw new ReviewNotFoundException("No Reviews for that user to return");

        // grab all reviews that exists for user and return
        reviews = reviewRepository.findAllByUserId(userId);
        return reviews;
    }


    @Transactional
    public Optional<Review> getReviewById(Integer reviewId, String foodieCookie) throws ReviewNotFoundException {
        Optional<Review> review = null;


        if(reviewRepository.findById(reviewId).isEmpty()){
            throw new ReviewNotFoundException("Review with reviewId: " + reviewId + " does not exists. Please try again.");
        } else {
            review = reviewRepository.findById(reviewId);

            User dbUser = userRepository.getById(review.get().getUserId());

            if(dbUser != null) {
                System.out.println("username: ....");
                System.out.println(dbUser.getUsername());

                System.out.println("cookie userName: ....");
                System.out.println(jwtUtils.getUserNameFromJwtToken(foodieCookie));

                if (dbUser.getUsername().equals(jwtUtils.getUserNameFromJwtToken(foodieCookie))) {
                    System.out.println("userName from cookie matches user.userName");
                } else {
                    System.out.println("cookie does not match user.userName");
                    throw new UserNotFoundException("cookie does not match user.userName");
                }

            }
        }
        return review;
    }

    @Transactional
    public String createReview(String foodieCookie, Integer restaurantId, Review newReview) throws NotCurrentUserException, RestaurantNotFoundException, ReviewNotFoundException, UserNotFoundException {
        // User: id, username, password, isActive, userRoles
        // Review: reviewId, timeCreated, rating, reviewText, userId, userName
        // RestaurantReview: reviewsId, restaurantsId

        // 1. Check if review user exists in our database
        // 2. Check current cookie is valid for user.
        // 3. Check if restaurant exists.
        // 4. Capture userName from dbUser and add to newReview.
        //      userName not sent with review(to avoid user mistyping userName),
        // 5. Save Review to database if all checks pass.
        // 6. Create RestaurantReview using the new review id.
        // 7. Check if restaurantReview was saved to database and return ResponseEntity.OK if successful.


        // Capturing newReview user to verify userName matches cookie userName
        User dbUser = userRepository.findById(newReview.getUserId()).orElse(null);
        // review is paired to restaurant via RestaurantReview  table
        RestaurantReview newRestaurantReview = new RestaurantReview();

        // Will be used further down to check if our review and restaurantReview save was successful.
        Review savedReview = new Review();
        RestaurantReview savedRestaurantReview = new RestaurantReview();

        // Check user exists.
        if(userRepository.findById(newReview.getUserId()).isEmpty())
            throw new UserNotFoundException("User not found. Could not create review.");
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

        // Check restaurant exists.
        if(restaurantRepository.findById(restaurantId).isEmpty())
            throw new RestaurantNotFoundException("Restaurant not found. Could not create review.");

        // must capture userName before saving review to database
        newReview.setUserName(dbUser.getUsername());
        System.out.println("newReview userName");
        System.out.println(newReview.getUserName());

        // capture the newly created review
        savedReview = reviewRepository.saveAndFlush(newReview);

        // check saved review exists in database
        if(reviewRepository.findById(savedReview.getReviewId()).isEmpty())
            throw new ReviewNotFoundException("Review could not be saved.");

        System.out.println("New Review created with Id: " + savedReview.getReviewId());

        // populate newRestaurantReview for newly created review
        // uses the new review id
        newRestaurantReview.setRestaurantsId(restaurantId);
        newRestaurantReview.setReviewsId(savedReview.getReviewId());
        // capture newly created userImage
        savedRestaurantReview = restaurantReviewRepository.saveAndFlush(newRestaurantReview);

        // check saved userImage exists in database and return message to user
        if(restaurantReviewRepository.findByReviewsId(savedRestaurantReview.getReviewsId()).isEmpty()) {
            throw new ReviewNotFoundException("Review could not be assigned to restaurant.");
        } else {
            return "New Review created with Id: " + savedReview.getReviewId() + " assigned to restaurant: " + savedRestaurantReview.getRestaurantsId();
        }

    }

    @Transactional
    public String updateReview(String foodieCookie, Review updateReview) throws NotCurrentUserException, ReviewNotFoundException, UserNotFoundException {
        // User: id, username, password, isActive, userRoles
        // Review: reviewId, timeCreated, rating, reviewText, userId, userName

        // 1. Check if user exists in database.
        // 2. Check if user profile exists in database.
        // 3. Check current cookie is valid for user.
        // 4. Save updated user profile tp database.
        // 5. Return ResponseEntity.OK and success message.

        // Capturing user to verify userName matches cookie UserName
        User dbUser = userRepository.findById(updateReview.getUserId()).orElse(null);
        Review dbReview = reviewRepository.findById(updateReview.getReviewId()).orElse(null);;

        // Check user exists.
        if(userRepository.findById(updateReview.getUserId()).isEmpty())
            throw new UserNotFoundException("User with Id: " +  updateReview.getUserId() + " does not exists.");

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

        if(reviewRepository.findById(updateReview.getReviewId()).isEmpty())
            throw new ReviewNotFoundException("Review with Id: " + updateReview.getReviewId() + " does not exists.");

        dbReview.setReviewText(updateReview.getReviewText());
        dbReview.setRating(updateReview.getRating());

        reviewRepository.save(dbReview);
        return "Review has been updated successfully";

    }

    @Transactional
    public String deleteReview(String foodieCookie, Review deleteReview) throws ImageNotFoundException, NotCurrentUserException, ReviewNotFoundException, UserNotFoundException {
        // User: id, username, password, isActive, userRoles
        // Review: reviewId, timeCreated, rating, reviewText, userId, userName
        // RestaurantReview: reviewsId, restaurantsId

        // 1. Check if deleteImage user exists in database.
        // 2. Check current cookie is valid for user.
        // 3. Check review exists in database.
        // 4. Delete RestaurantReview from restaurantReview table in database.
        // 5. Delete Review from review table in database.
        // 5. Return ResponseEntity.OK and success message.

        Review dbReview = reviewRepository.findById(deleteReview.getReviewId()).orElse(null);
        RestaurantReview dbRestaurantReview = restaurantReviewRepository.findByReviewsId(deleteReview.getReviewId()).orElse(null);

        // Capturing user to verify userName matches cookie UserName and deleteImage user
        User dbUser = userRepository.getById(deleteReview.getUserId());

        // Check user exists.
        if(userRepository.findById(dbUser.getUserId()).isEmpty())
            throw new UserNotFoundException("User with Id: " +  deleteReview.getUserId() + " does not exists.");

        // Check user has currently active jwtCookie
        if(!dbUser.getUsername().equals(jwtUtils.getUserNameFromJwtToken(foodieCookie)) ) {
            throw new NotCurrentUserException("User cookie not valid.");
        } else {
            System.out.println("User cookie was valid.");
        }

        // Check review exists
        if(reviewRepository.findById(deleteReview.getReviewId()).isEmpty())
            throw new ReviewNotFoundException("Review with Id: " + deleteReview.getReviewId() + " does not exists.");

        // Check if restaurantReview exists.
        if(restaurantReviewRepository.findByReviewsId(deleteReview.getReviewId()).isEmpty())
            throw new ReviewNotFoundException("RestaurantReview with Id: " + deleteReview.getReviewId() + " does not exists.");

        // review must be deleted from restaurantReview table first, then deleted from review table
        restaurantReviewRepository.delete(dbRestaurantReview);
        reviewRepository.delete(dbReview);
        return "Review has been deleted successfully";


    }

}