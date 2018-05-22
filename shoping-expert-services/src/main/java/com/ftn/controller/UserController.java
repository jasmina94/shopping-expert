package com.ftn.controller;

import com.ftn.dto.LoginDTO;
import com.ftn.dto.RegistrationDTO;
import com.ftn.dto.UserDTO;
import com.ftn.service.IMailService;
import com.ftn.service.IUserService;
import com.ftn.util.GenericResponse;
import com.ftn.util.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;


/**
 * Created by Jasmina on 15/05/2018.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IMailService mailService;


    @Transactional
    @PostMapping
    public GenericResponse<UserDTO> register(@RequestBody RegistrationDTO registrationDTO){
        GenericResponse<UserDTO> response = new GenericResponse<>();
        UserDTO registeredUser = userService.register(registrationDTO);

        if(registeredUser != null){
            response.success(registeredUser);
        }else {
            response.error("Registration failed. Try with another email.");
        }
        return response;
    }

    @Transactional
    @PostMapping(value = "/login")
    public GenericResponse<String> login(@RequestBody LoginDTO loginDTO){
        GenericResponse<String> response = new GenericResponse<>();
        boolean credentialsOk = userService.checkCredentials(loginDTO);
        String jwt = "";
        if(credentialsOk){
            UserDTO userToLogin = userService.getByEmailAndPassword(loginDTO.getEmail(), loginDTO.getPassword());
            jwt = JWTUtils.makeJWT(userToLogin.getId().intValue());

            if(jwt != null && !jwt.isEmpty()) {
                response.success(jwt);
            } else {
                response.error("Failed to form JWT.");
            }
        } else {
            response.error("Credentials are not valid.");
        }
        return response;
    }

    @Transactional
    @GetMapping
    public GenericResponse<UserDTO> getLoggedUser(@RequestHeader(value="Authorization") String jwt) {
        GenericResponse<UserDTO> retVal = new GenericResponse<>();
        UserDTO user;
        int id;
        id = JWTUtils.unpackJWT(jwt);
        if(id > -1) {
            user = userService.getById(id);
            if(user != null) {
                retVal.success(user);
            } else {
                retVal.error("There is no user with id: " + id);
            }
        } else {
            retVal.error("There is no logged in user.");
        }
        return retVal;
    }

    @Transactional
    @GetMapping(value = "/forgetPass/{email}")
    public GenericResponse<Boolean> recoverPassword(@PathVariable("email") String email){
        GenericResponse<Boolean> response = new GenericResponse<>();
        UserDTO userDTO = userService.getByEmail(email);
        if(userDTO != null){
            boolean emailSent = mailService.sendForgottenPassword(email, userDTO.getPassword(), userDTO.getFirstName(), userDTO.getLastName());
            if(emailSent){
                response.success(true);
            }else {
                response.error("Error while sending forgotten password mail!");
            }
        }else {
            response.error("There is no user with email: " + email);
        }
        return response;
    }
}
