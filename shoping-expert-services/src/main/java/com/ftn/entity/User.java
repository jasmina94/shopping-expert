package com.ftn.entity;

import com.ftn.dto.RegistrationDTO;
import com.ftn.dto.UserDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Email;
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

    public User(UserDTO userDTO){
        this.id = userDTO.getId();
        this.email = userDTO.getEmail();
        this.password = userDTO.getPassword();
        this.firstName = userDTO.getFirstName();
        this.lastName = userDTO.getLastName();
    }

    public void merge(RegistrationDTO registrationDTO){
        this.email = registrationDTO.getEmail();
        this.password = registrationDTO.getPassword();
        this.firstName = registrationDTO.getFirstName();
        this.lastName = registrationDTO.getLastName();
    }
}
