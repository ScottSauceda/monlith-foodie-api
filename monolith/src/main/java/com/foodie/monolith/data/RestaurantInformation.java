package com.foodie.monolith.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantInformation {

    private Integer restaurantId;
    private Integer location_id;
    private Integer owner_id;
    private String name;

    // matches to location_id

    // matches to owner_id

    // matches to restaurant Tags;


}