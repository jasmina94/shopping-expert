package com.ftn.mdj.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Jasmina on 17/04/2018.
 */
@Data
@NoArgsConstructor
public class LoginDTO {

    private String username;
    private String password;

    public LoginDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
