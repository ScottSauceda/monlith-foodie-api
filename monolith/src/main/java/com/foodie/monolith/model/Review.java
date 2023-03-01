package com.foodie.monolith.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id", nullable = false)
    private Integer reviewId;

    @Convert(disableConversion = true)
    @Column(name = "time_created")
    private LocalDateTime timeCreated;

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "review_text", length = 250)
    private String reviewText;

    @Column(name = "users_id")
    private Integer userId;
}
