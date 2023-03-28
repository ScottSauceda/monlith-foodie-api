package com.foodie.monolith.data;

import com.foodie.monolith.model.Image;
import com.foodie.monolith.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInformation {
    // user
    private Long usersId;
    private String userName;
    private Boolean isActive;
    private Set<Role> roles = new HashSet<>();
    //    private String roleName;
    // userProfile
    private String email;
    private String phone;
    private String firstName;
    private String lastName;
    private Image profileImage;

}