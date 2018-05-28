package com.ftn.service.serviceImplementation;

import com.ftn.dto.LoginDTO;
import com.ftn.dto.RegistrationDTO;
import com.ftn.dto.UserDTO;
import com.ftn.entity.User;
import com.ftn.repository.UserRepository;
import com.ftn.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by Jasmina on 15/05/2018.
 */
@Service
public class UserService implements IUserService{

    @Autowired
    private UserRepository userRepository;


    @Override
    public UserDTO register(RegistrationDTO registrationDTO) {
        UserDTO userDTO = null;
        boolean emailOk = checkEmail(registrationDTO.getEmail());
        if(emailOk) {
            try {
                User user = new User();
                user.merge(registrationDTO);
                user = userRepository.save(user);
                userDTO = new UserDTO(user);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return userDTO;
    }

    @Override
    public UserDTO getById(int id) {
        UserDTO userDTO = null;
        long lId = ((long) id);
        try{
           User user = userRepository.findById(lId).orElseThrow(NullPointerException::new);
           userDTO = new UserDTO(user);
           return userDTO;
        }catch (NullPointerException e){
            return userDTO;
        }
    }

    @Override
    public UserDTO getByEmail(String email) {
        UserDTO userDTO = null;
        User user = userRepository.findByEmail(email);
        if(user != null){
            userDTO = new UserDTO(user);
        }
        return userDTO;
    }

    @Override
    public UserDTO getByEmailAndPassword(String email, String password) {
        UserDTO userDTO = null;
        User user = userRepository.findByEmailAndPassword(email, password);
        if(user != null){
            userDTO = new UserDTO(user);
        }
        return userDTO;
    }

    @Override
    public boolean checkCredentials(LoginDTO loginDTO) {
        boolean credentialsOk = true;
        String email = loginDTO.getEmail();
        String password = loginDTO.getPassword();
        UserDTO user = getByEmailAndPassword(email, password);
        if(user == null){
            credentialsOk = false;
        }
        return credentialsOk;
    }

    private boolean checkEmail(String email){
        boolean emailOk = true;
        User user = userRepository.findByEmail(email);
        if(user != null){
            emailOk = false;
        }
        return emailOk;
    }
}