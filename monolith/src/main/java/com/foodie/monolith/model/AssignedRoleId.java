package com.foodie.monolith.model;

import jakarta.persistence.Column;
import jakarta.persistence.Id;

import java.io.Serializable;

public class AssignedRoleId implements Serializable {
    private Integer rolesId;

    private Integer usersId;

    public Integer getRolesId() {
        return rolesId;
    }

    public void setRolesId(Integer rolesId) {
        this.rolesId = rolesId;
    }

    public Integer getUsersId() {
        return usersId;
    }

    public void setUsersId(Integer usersId) {
        this.usersId = usersId;
    }
}