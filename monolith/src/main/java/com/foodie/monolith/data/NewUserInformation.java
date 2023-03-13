package com.foodie.monolith.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewUserInformation {

    // User Model
    private String userName;
    private String password;
    private Boolean isActive;

    // UserProfile Model
    private String email;
    private String phone;
    private String firstName;
    private String lastName;

    // AssignRole Model
    private Integer roleId;

}