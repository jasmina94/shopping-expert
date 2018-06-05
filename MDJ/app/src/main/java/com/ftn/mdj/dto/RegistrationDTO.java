package com.ftn.mdj.dto;

import com.google.firebase.auth.FirebaseUser;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Jasmina on 17/04/2018.
 */
@Data
@NoArgsConstructor
public class RegistrationDTO {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String deviceInstance;
    private String registerType;


    public RegistrationDTO(String email, String password, String firstName, String lastName ) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public RegistrationDTO(FirebaseUser firebaseUser){
        this.email = firebaseUser.getEmail();
        this.firstName = firebaseUser.getDisplayName();
        this.password = "";
        this.lastName = "";
    }
}
