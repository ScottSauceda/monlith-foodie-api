package com.foodie.monolith.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.WhereJoinTable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "profiles")
public class UserProfile {
    @Id
    @Column(name = "users_id")
    private Long usersId;
    @Column(name = "email", length = 50)
    private String email;
    @Column(name = "phone", length = 50)
    private String phone;
    @Column(name = "first_name", length = 50)
    private String firstName;
    @Column(name = "last_name", length = 50)
    private String lastName;
    @OneToOne
    @JoinTable(name = "user_images",
            joinColumns = @JoinColumn(name = "users_id"),
            inverseJoinColumns = @JoinColumn(name="images_id"))
    private Image profileImage;

}
