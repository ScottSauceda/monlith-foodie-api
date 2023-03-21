package com.foodie.monolith.service;

import com.foodie.monolith.exception.RestaurantNotFoundException;
import com.foodie.monolith.exception.ReviewNotFoundException;
import com.foodie.monolith.exception.UserNotFoundException;
import com.foodie.monolith.model.Restaurant;
import com.foodie.monolith.model.RestaurantReview;
import com.foodie.monolith.model.Review;
import com.foodie.monolith.model.User;
import com.foodie.monolith.repository.RestaurantRepository;
import com.foodie.monolith.repository.RestaurantReviewRepository;
import com.foodie.monolith.repository.ReviewRepository;
import com.foodie.monolith.repository.UserRepository;
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
    public List<Review> getUserReviews(Integer userId) throws UserNotFoundException, ReviewNotFoundException {
        List<Review> reviews = new ArrayList<Review>();
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
    public Optional<Review> getReviewById(Integer reviewId) throws ReviewNotFoundException {
        Optional<Review> review = null;
        if(reviewRepository.findById(reviewId).isEmpty()){
            throw new ReviewNotFoundException("Review with reviewId: " + reviewId + " does not exists. Please try again.");
        } else {
            review = reviewRepository.findById(reviewId);
        }
        return review;
    }

    @Transactional
    public String createReview(Review newReview, Integer restaurantId) throws UserNotFoundException, ReviewNotFoundException, RestaurantNotFoundException {

        // find user from usersId in newReview
        User newReviewUser = userRepository.findById(newReview.getUserId()).orElse(null);

        // Review
        Review savedReview = new Review();


        // RestaurantReview
        RestaurantReview savedRestaurantReview = new RestaurantReview();

        RestaurantReview newRestaurantReview = new RestaurantReview();
        newRestaurantReview.setRestaurantsId(restaurantId);


        if(newReviewUser == null){
            throw new UserNotFoundException("User not found. Could not create review.");
        } else {
            newReview.setUserName(newReviewUser.getUsername());
            System.out.println("newReview userName");
            System.out.println(newReview.getUserName());
        }

        if(restaurantRepository.getById(restaurantId) == null){
            throw new RestaurantNotFoundException("Restaurant not found. Could not create review.");
        }

        System.out.println("newReview");
        System.out.print(newReview);

        savedReview = reviewRepository.saveAndFlush(newReview);

        if(reviewRepository.findById(savedReview.getReviewId()) == null){
            throw new ReviewNotFoundException("Review not created. Please try again.");
        } else {
            newRestaurantReview.setReviewsId(savedReview.getReviewId());
            savedRestaurantReview = restaurantReviewRepository.saveAndFlush(newRestaurantReview);
        }

        if(savedRestaurantReview.getReviewsId() == null) {
            throw new ReviewNotFoundException("Review not assigned. Please try again.");
        } else {
            return "New Review created with Id: " + savedReview.getReviewId() + " assigned to restaurant: " + savedRestaurantReview.getRestaurantsId();
        }

    }

    @Transactional
    public String updateReview(Integer reviewId, Review updateReview) throws ReviewNotFoundException {
        Review dbReview = reviewRepository.findById(reviewId).orElse(null);;

        if(dbReview == null){
            throw new ReviewNotFoundException("Review with Id: " + reviewId + "does not exists. Please try again.");
        } else {
            dbReview.setRating(updateReview.getRating());
            dbReview.setReviewText(updateReview.getReviewText());

            return "Review has been updated successfully";
        }
    }

    @Transactional
    public String deleteReview(Integer reviewId) throws ReviewNotFoundException {
        Review dbReview = reviewRepository.findById(reviewId).orElse(null);
        RestaurantReview dbRestaurantReview = restaurantReviewRepository.findByReviewsId(reviewId).orElse(null);

        if(reviewRepository.findById(reviewId) == null){
            throw new ReviewNotFoundException("Review with Id: " + reviewId + " does not exists. Please try again.");
        } else {

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