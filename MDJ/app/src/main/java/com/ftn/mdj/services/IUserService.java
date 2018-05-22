package com.ftn.mdj.services;

import com.ftn.mdj.dto.LoginDTO;
import com.ftn.mdj.dto.RegistrationDTO;
import com.ftn.mdj.dto.UserDTO;
import com.ftn.mdj.utils.GenericResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Jasmina on 17/05/2018.
 */

public interface IUserService {

    String USER_PREFIX = "/api/users";

    @POST(USER_PREFIX)
    Call<GenericResponse<UserDTO>> register(@Body RegistrationDTO registrationDTO);

    @POST(USER_PREFIX + "/login")
    Call<GenericResponse<String>> login(@Body LoginDTO loginData);

    @GET(USER_PREFIX)
    Call<GenericResponse<UserDTO>> getLoggedInUser();

    @GET(USER_PREFIX + "/forgetPass/{email}")
    Call<GenericResponse<Boolean>> forgetPassword(@Path("email") String email);
}
