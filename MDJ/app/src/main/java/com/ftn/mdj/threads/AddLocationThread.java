package com.ftn.mdj.threads;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import com.ftn.mdj.fragments.MainFragment;
import com.ftn.mdj.services.ServiceUtils;
import com.ftn.mdj.utils.GenericResponse;

import retrofit2.Call;
import retrofit2.Response;
import lombok.Getter;

@Getter
public class AddLocationThread extends Thread {
    private Handler handler;
    private Handler responseHandler;

    public AddLocationThread(Handler handlerUI, Long shoppingListId, Double latitude, Double longitude){
        responseHandler = handlerUI;
        handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                ServiceUtils.listService.updateLocation(shoppingListId, latitude, longitude).enqueue(new retrofit2.Callback<GenericResponse<Boolean>>(){

                    @Override
                    public void onResponse(Call<GenericResponse<Boolean>> call, Response<GenericResponse<Boolean>> response) {
                        System.out.println("Successfully added location!");
                        responseHandler.sendMessage(ServiceUtils.getHandlerMessageFromResponse(response));
                    }

                    @Override
                    public void onFailure(Call<GenericResponse<Boolean>> call, Throwable t) {
                        System.out.println("Error adding location!");
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
