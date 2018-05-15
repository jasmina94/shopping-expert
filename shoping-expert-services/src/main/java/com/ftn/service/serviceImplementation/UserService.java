package com.ftn.service.serviceImplementation;

import com.ftn.dto.UserDTO;
import com.ftn.entity.User;
import com.ftn.repository.UserRepository;
import com.ftn.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Jasmina on 15/05/2018.
 */
@Service
public class UserService implements IUserService{

    @Autowired
    private UserRepository userRepository;


    @Override
    public UserDTO register(UserDTO userDTO) {
        UserDTO registered = null;
        boolean emailOk = checkEmail(userDTO.getEmail());
        if(emailOk) {
            try {
                User user = new User();
                user.merge(userDTO);
                user = userRepository.save(user);
                registered = new UserDTO(user);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return registered;
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
    public boolean checkCredentials(UserDTO userDTO) {
        boolean credentialsOk = true;
        String email = userDTO.getEmail();
        String password = userDTO.getPassword();
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
