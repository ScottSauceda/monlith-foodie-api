package com.foodie.monolith.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@IdClass(RestaurantReviewId.class)
@Table(name = "restaurant_reviews")
public class RestaurantReview {

    @Id
    @Column(name = "reviews_id")
    private Integer reviewsId;

    @Id
    @Column(name = "restaurants_id")
    private Integer restaurantsId;

}