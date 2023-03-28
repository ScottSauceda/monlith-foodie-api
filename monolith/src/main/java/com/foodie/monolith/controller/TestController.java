package com.foodie.monolith.controller;

import com.foodie.monolith.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    JwtUtils jwtUtils;

    @GetMapping("/all")
    public String allAccess(){
        return "Public Content.";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public String userAccess(){
        return "User Content.";
    }

    @GetMapping("/user/getCookie/{userName}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public String userNameAccess(@PathVariable String userName, @CookieValue("foodie") String foodieCookie){
//        return "User  Content.";

        System.out.println("User supplied");
        System.out.println(userName);
        System.out.println("Cookie user");
        System.out.println(jwtUtils.getUserNameFromJwtToken(foodieCookie));

        if(userName.equals(jwtUtils.getUserNameFromJwtToken(foodieCookie))){
            return "User "+ jwtUtils.getUserNameFromJwtToken(foodieCookie) +  " Content.";
        } else {
            return "User does not match the current session user.";
        }
    }

    @GetMapping("/owner")
    @PreAuthorize("hasRole('OWNER') or hasRole('USER') or hasRole('ADMIN')")
    public String ownerAccess(){
        return "Owner Board";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess(){
        return "Admin Board";
    }

}