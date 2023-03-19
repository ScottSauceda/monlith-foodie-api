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
@IdClass(RestaurantImageId.class)
@Table(name = "restaurant_images")
public class RestaurantImage {
    @Id
    @Column(name = "images_id")
    private Integer imagesId;

    @Id
    @Column(name="restaurants_id")
    private Integer restaurantsId;

    @Column(name="is_main")
    private boolean isMain;
}