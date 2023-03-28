package com.foodie.monolith.service;

import com.foodie.monolith.exception.*;
import com.foodie.monolith.model.Restaurant;
import com.foodie.monolith.model.RestaurantReview;
import com.foodie.monolith.model.Review;
import com.foodie.monolith.model.User;
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
    public List<Review> getUserReviews(Long userId, String foodieCookie) throws UserNotFoundException, ReviewNotFoundException {
        List<Review> reviews = new ArrayList<Review>();

        User dbUser = userRepository.getById(userId);

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

        if(reviewRepository.findAll().isEmpty()){
            throw new ReviewNotFoundException("No Reviews to return");
        } else if(reviewRepository.findAllByUserId(userId).isEmpty()) {
            throw new UserNotFoundException("No Reviews for that user to return");
        } else {
            List<Review> dbReviews = reviewRepository.findAllByUserId(userId);
            for(Review review: dbReviews){
                reviews.add(review);
            }
            return reviews;
        }
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
    public String updateReview(Integer reviewId, Review updateReview, String foodieCookie) throws ReviewNotFoundException {
        Review dbReview = reviewRepository.findById(reviewId).orElse(null);;

        if(dbReview == null){
            throw new ReviewNotFoundException("Review with Id: " + reviewId + "does not exists. Please try again.");
        } else {

            User dbUser = userRepository.getById(dbReview.getUserId());

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


            dbReview.setRating(updateReview.getRating());
            dbReview.setReviewText(updateReview.getReviewText());

            return "Review has been updated successfully";
        }
    }

    @Transactional
    public String deleteReview(Integer reviewId, String foodieCookie) throws ReviewNotFoundException {
        Review dbReview = reviewRepository.findById(reviewId).orElse(null);
        RestaurantReview dbRestaurantReview = restaurantReviewRepository.findByReviewsId(reviewId).orElse(null);

        if(reviewRepository.findById(reviewId) == null){
            throw new ReviewNotFoundException("Review with Id: " + reviewId + " does not exists. Please try again.");
        } else {

            User dbUser = userRepository.getById(dbReview.getUserId());

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


            if(restaurantReviewRepository.findByReviewsId(reviewId) == null){
                throw new ReviewNotFoundException("RestaurantReview with Id: " + reviewId + " does not exists. Please try again.");
            } else {
                restaurantReviewRepository.delete(dbRestaurantReview);
                reviewRepository.delete(dbReview);
                return "Review has been deleted successfully";
            }
        }
    }

}