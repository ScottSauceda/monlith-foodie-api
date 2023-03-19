package com.foodie.monolith.data;

import com.foodie.monolith.model.Image;
import com.foodie.monolith.model.Review;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantInformation {

    private Integer restaurantId;
    private String name;
    private String address;
    private String city;
    private String state;
    private Integer zipCode;
    private Integer ownerId;
    private Boolean isActive;

    // matches to owner_id
    private String ownerName;

    // matches to restaurant reviews
    private List<Review> restaurantReviews;

    // matches to restaurant images
    private List<Image> restaurantImages;

}