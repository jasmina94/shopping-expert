package com.ftn.mdj.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Jasmina on 17/04/2018.
 */
@Data
@NoArgsConstructor
public class RegisterDTO {

    private String username;
    private String password;
    private String email;

    public RegisterDTO(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
}
