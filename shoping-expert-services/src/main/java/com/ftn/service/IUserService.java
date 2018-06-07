package com.ftn.service;

import com.ftn.dto.LoginDTO;
import com.ftn.dto.RegistrationDTO;
import com.ftn.dto.UserDTO;
import com.ftn.entity.User;

/**
 * Created by Jasmina on 15/05/2018.
 */
public interface IUserService {

    UserDTO register(RegistrationDTO registrationDTO);
    UserDTO getById(int id);

    User getByEmailRealUser(String email);

    UserDTO getByEmail(String email);
    UserDTO getByEmailAndPassword(String email, String password);
    boolean checkCredentials(LoginDTO loginDTO);

    void removeDeviceInstanceFromUser(Long userId, String deviceInstance);
}
