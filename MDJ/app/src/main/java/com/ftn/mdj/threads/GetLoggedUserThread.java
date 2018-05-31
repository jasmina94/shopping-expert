package com.ftn.mdj.threads;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.ftn.mdj.dto.UserDTO;
import com.ftn.mdj.services.ServiceUtils;
import com.ftn.mdj.utils.GenericResponse;

import lombok.Getter;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Jasmina on 24/05/2018.
 */
@Getter
public class GetLoggedUserThread extends Thread {
    private Handler handler;
    private Handler responseHandler;

    public GetLoggedUserThread(Handler handlerUI) {
        responseHandler = handlerUI;
        handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                ServiceUtils.userService.getLoggedInUser().enqueue(new retrofit2.Callback<GenericResponse<UserDTO>>() {

                    @Override
                    public void onResponse(Call<GenericResponse<UserDTO>> call, Response<GenericResponse<UserDTO>> response) {
                        System.out.println("Successfully received logged user!");
                        responseHandler.sendMessage(ServiceUtils.getHandlerMessageFromResponse(response));
                    }

                    @Override
                    public void onFailure(Call<GenericResponse<UserDTO>> call, Throwable t) {
                        System.out.println("Error receiving logged user data!");
                        responseHandler.sendMessage(GenericResponse.getGenericServerErrorResponseMessage());
                    }
                });
                super.handleMessage(msg);
            }

        };
    }

    @Override
    public void run() {
        if (Looper.myLooper() == null) {
            Looper.prepare();
        }

        Looper.loop();
    }
}
