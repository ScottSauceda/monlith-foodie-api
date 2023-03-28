package com.foodie.monolith.payload.response;

import java.util.List;

public class UserInfoResponse {
    private Long id;
    private String username;
//    private String email;
    private List<String> roles;

    public UserInfoResponse(Long id, String username, List<String> roles){
        this.id = id;
        this.username = username;
//        this.email = email;
        this.roles = roles;
    }

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

//    public String getEmail(){
//        return email;
//    }

//    public void setEmail(String email){
//        this.email = email;
//    }

    public String getUsername(){
        return username;
    }

    public List<String> getRoles() {
        return roles;
    }

}