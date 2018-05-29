package com.ftn.mdj.threads;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.ftn.mdj.dto.LoginDTO;
import com.ftn.mdj.services.ServiceUtils;
import com.ftn.mdj.utils.GenericResponse;

import lombok.Getter;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Jasmina on 24/05/2018.
 */
@Getter
public class LoginThread extends Thread {
    private Handler handler;
    private Handler responseHandler;

    public LoginThread(Handler handlerUI) {
        this.responseHandler = handlerUI;
        this.handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                LoginDTO loginDTO = (LoginDTO) msg.obj;
                ServiceUtils.userService.login(loginDTO).enqueue(new retrofit2.Callback<GenericResponse<String>>(){

                    @Override
                    public void onResponse(Call<GenericResponse<String>> call, Response<GenericResponse<String>> response) {
                        System.out.println("Meesage recieved successfully!");
                        responseHandler.sendMessage(ServiceUtils.getHandlerMessageFromResponse(response));
                    }

                    @Override
                    public void onFailure(Call<GenericResponse<String>> call, Throwable t) {
                        System.out.println("Error sending login data!");
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
}
