package com.ftn.mdj.threads;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.ftn.mdj.dto.ShoppingListDTO;
import com.ftn.mdj.services.ServiceUtils;
import com.ftn.mdj.utils.GenericResponse;
import com.ftn.mdj.utils.UtilHelper;

import lombok.Getter;
import retrofit2.Call;
import retrofit2.Response;

@Getter
public class CreateListThread extends Thread {
    private Handler handler;
    private Handler responseHandler;

    public CreateListThread(Handler handlerUI, Long userId, String listName) {
        responseHandler = handlerUI;
        handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                ServiceUtils.listService.create(listName, userId).enqueue(new retrofit2.Callback<GenericResponse<ShoppingListDTO>>() {

                    @Override
                    public void onResponse(Call<GenericResponse<ShoppingListDTO>> call, Response<GenericResponse<ShoppingListDTO>> response) {
                        System.out.println("Successfully created new list!");
                        responseHandler.sendMessage(ServiceUtils.getHandlerMessageFromResponse(response));
                    }

                    @Override
                    public void onFailure(Call<GenericResponse<ShoppingListDTO>> call, Throwable t) {
                        System.out.println("Error while creating new list!");
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
