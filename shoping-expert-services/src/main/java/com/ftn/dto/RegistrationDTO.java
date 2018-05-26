package com.ftn.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

/**
 * Created by Jasmina on 17/05/2018.
 */
@Data
@NoArgsConstructor
public class RegistrationDTO {

    @NotNull
    private String firstName;

    private String lastName;

    @NotNull
    private String email;

    private String password;


}
