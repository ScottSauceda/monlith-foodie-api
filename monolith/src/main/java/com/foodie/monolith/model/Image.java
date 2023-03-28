package com.foodie.monolith.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "images")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "img_id")
    private int imgId;

    @Column(name ="img_name")
    private String imgName;

    @Column(name ="img_src")
    private String imgSrc;

    @Column(name ="img_type")
    private String imgType;

    @Column(name = "users_id")
    private Long usersId;
}