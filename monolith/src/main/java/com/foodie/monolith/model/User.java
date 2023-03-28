package com.foodie.monolith.model;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name ="username", unique = true)
    private String username;

    @Column(name ="password")
    private String password;

    @Column(name = "is_active")
    private boolean isActive;

//    @OneToOne
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "assigned_roles",
            joinColumns = @JoinColumn(name = "users_id"),
            inverseJoinColumns = @JoinColumn(name = "roles_id"))
    private Set<Role> userRoles = new HashSet<>();

    public User(String username, String password){
        this.username = username;
        this.password = password;
    }

//    @ManyToMany()
//    @JoinTable(name = "assigned_roles",
//            joinColumns = @JoinColumn(name = "users_id"),
//            inverseJoinColumns = @JoinColumn(name = "roles_id"))
//    private List<Role> userRoles;
//
//    public void addRole(Role toAdd){
//        if(userRoles == null){
//            userRoles = new ArrayList<>();
//        }
//        userRoles.add(toAdd);
//    }





}