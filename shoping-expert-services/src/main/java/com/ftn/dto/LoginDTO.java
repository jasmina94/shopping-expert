package com.ftn.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Jasmina on 20/05/2018.
 */

@Data
@NoArgsConstructor
public class LoginDTO {

    private String email;
    private String password;
    private String deviceInstance;
}
