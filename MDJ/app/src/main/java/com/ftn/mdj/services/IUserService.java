package com.ftn.mdj.services;

import com.ftn.mdj.dto.LoginDTO;
import com.ftn.mdj.dto.RegistrationDTO;
import com.ftn.mdj.dto.UserDTO;
import com.ftn.mdj.utils.GenericResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
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

    @POST(USER_PREFIX + "/logout/{userId}/{deviceInstance}")
    Call<GenericResponse> logout(@Path("userId") Long userId, @Path("deviceInstance") String deviceInstance);

    @POST(USER_PREFIX + "/saveShowNotifications/{userId}/{showNotifications}")
    Call<GenericResponse> saveShowNotifications(@Path("userId") Long userId, @Path("showNotifications") Boolean showNotifications);

    @POST(USER_PREFIX + "/saveBlockedUsers/{userId}/{email}/{toBlock}")
    Call<GenericResponse<List<String>>> saveBlockedUsers(@Path("userId") Long userId, @Path("email") String email, @Path("toBlock") Boolean toBlock);

    @GET(USER_PREFIX + "/getBlockedUsers/{userId}")
    Call<GenericResponse<List<String>>> getBlockedUsers(@Path("userId") Long userId);

    @POST(USER_PREFIX + "/saveDistanceForLocation/{userId}/{distanceForLocation}")
    Call<GenericResponse> saveDistanceForLocation(@Path("userId") Long userId, @Path("distanceForLocation") Integer distanceForLocation);

    @DELETE(USER_PREFIX + "/removeUnusedDeviceInstances/{deviceInstance}")
    Call<GenericResponse> removeUnusedDeviceInstances(@Path("deviceInstance") String deviceInstance);
}
