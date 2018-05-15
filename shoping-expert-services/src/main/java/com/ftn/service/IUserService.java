package com.ftn.service;

import com.ftn.dto.UserDTO;

/**
 * Created by Jasmina on 15/05/2018.
 */
public interface IUserService {

    UserDTO register(UserDTO userDTO);
    UserDTO getByEmail(String email);
    UserDTO getByEmailAndPassword(String email, String password);
    boolean checkCredentials(UserDTO userDTO);
}
