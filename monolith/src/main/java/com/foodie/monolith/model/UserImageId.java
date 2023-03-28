package com.foodie.monolith.model;

import jakarta.persistence.Column;
import jakarta.persistence.Id;

import java.io.Serializable;

public class UserImageId implements Serializable {
    private Integer imagesId;

    private Long usersId;

    public Integer getImagesId() {
        return imagesId;
    }

    public void setImagesId(Integer imagesId) {
        this.imagesId = imagesId;
    }

    public Long getUsersId() {
        return usersId;
    }

    public void setUsersId(Long usersId) {
        this.usersId = usersId;
    }
}