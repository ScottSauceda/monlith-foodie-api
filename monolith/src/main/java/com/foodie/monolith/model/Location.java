package com.foodie.monolith.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "locations")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id", nullable = false)
    private Integer locationId;

    @Column(name = "location_name", length = 45)
    private String locationName;

    @Column(name = "address", length = 45)
    private String address;

    @Column(name = "city", length = 45)
    private String city;

    @Column(name = "state", length = 45)
    private String state;

    @Column(name = "zip_code", length = 45)
    private Integer zipCode;

    @Column(name = "users_id")
    private Integer userId;

    @Column(name = "location_active")
    private Integer locationActive;

    public Boolean compareValues(Location otherLocation) {
        Boolean same = true;
        if(!this.locationName.equals(otherLocation.getLocationName())){
            same = false;
        }
        if(!this.address.equals(otherLocation.getAddress())){
            same = false;
        }
        if(!this.city.equals(otherLocation.getCity())){
            same = false;
        }
        if(!this.state.equals(otherLocation.getState())){
            same = false;
        }
        if(!this.zipCode.equals(otherLocation.getZipCode())){
            same = false;
        }
        return same;
    }

}
