package com.foodie.monolith.service;

import com.foodie.monolith.exception.RestaurantNotFoundException;
import com.foodie.monolith.exception.ReviewNotFoundException;
import com.foodie.monolith.model.RestaurantReview;
import com.foodie.monolith.repository.AssignedRoleRepository;
import com.foodie.monolith.repository.RestaurantReviewRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RestaurantReviewServiceImpl implements RestaurantReviewService {
    @Autowired
    RestaurantReviewRepository restaurantReviewRepository;

    @Transactional
    public List<RestaurantReview> getRestaurantReviews() throws RuntimeException {
        List<RestaurantReview> restaurantReviews = new ArrayList<RestaurantReview>();
        if(restaurantReviewRepository.findAll().isEmpty()){
            throw new RuntimeException("No RestaurantReviews to return");
        } else {
            List<RestaurantReview> dbRestaurantReviews = restaurantReviewRepository.findAll();
            for(RestaurantReview restaurantReview: dbRestaurantReviews){
                restaurantReviews.add(restaurantReview);
            }
            return restaurantReviews;
        }
    }

    @Transactional
    public List<RestaurantReview> getRestaurantReviewsByRestaurantId(Integer restaurantId) throws RestaurantNotFoundException {
        List<RestaurantReview> restaurantReview = null;
        if(restaurantReviewRepository.findAllByRestaurantsId(restaurantId).isEmpty()){
            throw new RestaurantNotFoundException("RestaurantReview with restaurantId: " + restaurantId + " does not exists. Please try again.");
        } else {
            restaurantReview = restaurantReviewRepository.findAllByRestaurantsId(restaurantId);
        }
        return restaurantReview;
    }

    @Transactional
    public String assignReviewToRestaurant(RestaurantReview newRestaurantReview) {
        RestaurantReview savedRestaurantReview = null;
        savedRestaurantReview = restaurantReviewRepository.saveAndFlush(newRestaurantReview);

        if(savedRestaurantReview.getReviewsId() != null){
            return "Review assigned to restaurantId: " + savedRestaurantReview.getRestaurantsId();
        } else {
            return "Something went wrong. Please try again";
        }
    }

    @Transactional
    public String deleteRestaurantReview(Integer reviewId) throws ReviewNotFoundException {
        RestaurantReview dbRestaurantReview = restaurantReviewRepository.findByReviewsId(reviewId).orElse(null);

        if(dbRestaurantReview == null){
            throw new ReviewNotFoundException("RestaurantReview with reviewId: " + reviewId + " does not exists. Please try again.");
        } else {
            restaurantReviewRepository.delete(dbRestaurantReview);
            return "RestaurantReview has been deleted successfully";
        }
    }

}