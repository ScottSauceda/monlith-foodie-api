package com.foodie.monolith.payload.request;

import java.util.Set;

import jakarta.validation.constraints.*;

public class SignupRequest {

    // Core User Details
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    @Size(min=6, max=40)
    private String password;

    private Set<String> role;

    // User Profile Information, Optional on Signup
    // can be entered through user web portal later

    @Size(max=50)
    private String email;

    @Size(max=20)
    private String phone;

    @Size(max=50)
    private String firstName;

    @Size(max=50)
    private String lastName;

    private Boolean isActive;


    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }
    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password = password;
    }

    public Set<String> getRole(){
        return this.role;
    }

    public void setRole(Set<String> role){
        this.role = role;
    }


    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}