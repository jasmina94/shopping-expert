package com.ftn.mdj.dto;

import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Jasmina on 17/04/2018.
 */
public class RegistrationDTO {

    private String firstName;
    private String lastName;
    private String email;
    private String password;


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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
