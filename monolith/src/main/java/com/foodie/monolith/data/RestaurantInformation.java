package com.foodie.monolith.data;

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
    private Integer location_id;
    private Integer owner_id;
    private String name;

    // matches to location_id
    private String location_name;
    private String address;
    private String city;
    private String state;
    private Integer zip_code;

    // matches to owner_id
    private String owner_name;

    // matches to restaurant reviews;
    private List<Review> restaurantReviews;


}