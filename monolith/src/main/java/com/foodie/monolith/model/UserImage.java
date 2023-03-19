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
@IdClass(UserImageId.class)
@Table(name = "user_images")
public class UserImage {

    @Id
    @Column(name = "images_id")
    private Integer imagesId;

    @Id
    @Column(name="users_id")
    private Integer usersId;

}