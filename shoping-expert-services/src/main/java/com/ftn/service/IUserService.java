package com.ftn.service;

import com.ftn.dto.RegistrationDTO;
import com.ftn.dto.UserDTO;

/**
 * Created by Jasmina on 15/05/2018.
 */
public interface IUserService {

    UserDTO register(RegistrationDTO registrationDTO);
    UserDTO getByEmail(String email);
    UserDTO getByEmailAndPassword(String email, String password);
    boolean checkCredentials(UserDTO userDTO);
}
