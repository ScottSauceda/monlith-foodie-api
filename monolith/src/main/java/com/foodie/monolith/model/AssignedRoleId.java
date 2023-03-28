package com.foodie.monolith.model;

import jakarta.persistence.Column;
import jakarta.persistence.Id;

import java.io.Serializable;

public class AssignedRoleId implements Serializable {
    private Integer rolesId;

    private Long usersId;

    public Integer getRolesId() {
        return rolesId;
    }

    public void setRolesId(Integer rolesId) {
        this.rolesId = rolesId;
    }

    public Long getUsersId() {
        return usersId;
    }

    public void setUsersId(Long usersId) {
        this.usersId = usersId;
    }
}