package com.ftn.mdj.threads;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.ftn.mdj.dto.RegistrationDTO;
import com.ftn.mdj.dto.UserDTO;
import com.ftn.mdj.services.ServiceUtils;
import com.ftn.mdj.utils.GenericResponse;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Jasmina on 24/05/2018.
 */

public class RegisterThread extends Thread {
    private Handler handler;
    private Handler responseHandler;

    public RegisterThread(Handler handlerUI){
        responseHandler = handlerUI;
        handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                RegistrationDTO registrationDTO = (RegistrationDTO)msg.obj;
                ServiceUtils.userService.register(registrationDTO).enqueue(new retrofit2.Callback<GenericResponse<UserDTO>>(){

                    @Override
                    public void onResponse(Call<GenericResponse<UserDTO>> call, Response<GenericResponse<UserDTO>> response) {
                        System.out.println("Meesage recieved successfully!");
                        responseHandler.sendMessage(ServiceUtils.getHandlerMessageFromResponse(response));
                    }

                    @Override
                    public void onFailure(Call<GenericResponse<UserDTO>> call, Throwable t) {
                        System.out.println("Error sending registration data!");
                        responseHandler.sendMessage(GenericResponse.getGenericServerErrorResponseMessage());
                    }
                });
                super.handleMessage(msg);
            }

        };
    }

    @Override
    public void run() {
        if(Looper.myLooper() == null) {
            Looper.prepare();
        }

        Looper.loop();
    }

    public Handler getHandler(){
        return handler;
    }
}
