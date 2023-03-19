package com.foodie.monolith.repository;

import com.foodie.monolith.model.UserImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserImageRepository extends JpaRepository<UserImage, Integer> {
    Optional<UserImage> findByUsersId(Integer userId);

    Optional<UserImage> findByImagesId(Integer imageId);
}