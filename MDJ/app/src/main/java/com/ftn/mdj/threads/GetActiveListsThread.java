package com.ftn.mdj.threads;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.ftn.mdj.dto.ShoppingListDTO;
import com.ftn.mdj.services.ServiceUtils;
import com.ftn.mdj.utils.GenericResponse;

import java.util.List;

import lombok.Getter;
import retrofit2.Call;
import retrofit2.Response;

@Getter
public class GetActiveListsThread extends Thread {
    private Handler handler;
    private Handler responseHandler;

    public GetActiveListsThread(Handler handlerUI, boolean isArchived, Long userId){
        responseHandler = handlerUI;
        handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                ServiceUtils.listService.listsByStatus(isArchived, userId).enqueue(new retrofit2.Callback<GenericResponse<List<ShoppingListDTO>>>(){

                    @Override
                    public void onResponse(Call<GenericResponse<List<ShoppingListDTO>>> call, Response<GenericResponse<List<ShoppingListDTO>>> response) {
                        System.out.println("Getting lists successfully!");
                        responseHandler.sendMessage(ServiceUtils.getHandlerMessageFromResponse(response));
                    }

                    @Override
                    public void onFailure(Call<GenericResponse<List<ShoppingListDTO>>> call, Throwable t) {
                        System.out.println("Error getting lists!");
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
