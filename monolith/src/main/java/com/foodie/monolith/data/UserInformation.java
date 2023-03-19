package com.foodie.monolith.data;

import com.foodie.monolith.model.Image;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInformation {

    // user
    private Integer usersId;
    private String userName;
    private Boolean isActive;

    // profile
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    private Image profileImage;

    // assignedRole
    private String roleName;

}