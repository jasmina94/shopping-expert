package com.ftn.entity;

import com.ftn.dto.RegistrationDTO;
import com.ftn.dto.UserDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by milca on 4/24/2018.
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@Component
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column(nullable = false)
    private String password;

    //email is at the same time username
    @Column(unique = true, nullable = false)
    @Email
    private String email;

    @Column
    @ElementCollection(targetClass=String.class)
    private List<String> instancesOfUserDevices = new ArrayList<>();

    @Column
    private Boolean showNotifications = true;

    @Column
    @ElementCollection(targetClass=String.class)
    private List<String> blockedUsers = new ArrayList<>();
    
    @Column
    private Integer distanceForLocation = 100;

    public User(UserDTO userDTO){
        this.id = userDTO.getId();
        this.email = userDTO.getEmail();
        this.password = userDTO.getPassword();
        this.firstName = userDTO.getFirstName();
        this.lastName = userDTO.getLastName();
        this.showNotifications = userDTO.getShowNotifications();
        this.distanceForLocation = userDTO.getDistanceForLocation();
    }

    public void merge(RegistrationDTO registrationDTO){
        this.email = registrationDTO.getEmail();
        this.password = registrationDTO.getPassword();
        this.firstName = registrationDTO.getFirstName();
        this.lastName = registrationDTO.getLastName();
    }
}
