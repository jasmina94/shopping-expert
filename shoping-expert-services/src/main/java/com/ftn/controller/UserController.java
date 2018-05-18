package com.ftn.controller;

import com.ftn.dto.RegistrationDTO;
import com.ftn.dto.UserDTO;
import com.ftn.service.IUserService;
import com.ftn.util.GenericResponse;
import com.ftn.util.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;

/**
 * Created by Jasmina on 15/05/2018.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private IUserService userService;

    @Transactional
    @PostMapping
    public GenericResponse<UserDTO> register(@Valid @RequestBody RegistrationDTO registrationDTO){
        System.out.println("so cool");
        GenericResponse<UserDTO> response = new GenericResponse<>();
        UserDTO registeredUser = userService.register(registrationDTO);

        if(registeredUser != null){
            System.out.println("dobro je");
            response.success(registeredUser);
        }else {
            System.out.println("greska");
            response.error("Registration failed. Try with another email.");
        }

        return response;
    }

    @Transactional
    @PostMapping(value = "/login")
    public GenericResponse<String> login(@RequestBody UserDTO userDTO){
        GenericResponse<String> response = new GenericResponse<>();
        boolean credentialsOk = userService.checkCredentials(userDTO);
        String jwt = "";

        if(credentialsOk){
            UserDTO userToLogin = userService.getByEmailAndPassword(userDTO.getEmail(), userDTO.getPassword());
            jwt = JWTUtils.makeJWT(userToLogin.getId().intValue());

            if(jwt != null && !jwt.isEmpty()) {
                response.success(jwt);
            } else {
                response.error("Failed to form JTW.");
            }
        } else {
            response.error("Credentials are not valid.");
        }
        return response;
    }

    @GetMapping
    public String test(){
        return "OK";
    }

}
