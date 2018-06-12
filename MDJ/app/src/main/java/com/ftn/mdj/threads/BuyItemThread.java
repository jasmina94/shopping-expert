package com.ftn.mdj.threads;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.ftn.mdj.services.ServiceUtils;
import com.ftn.mdj.utils.GenericResponse;

import lombok.Getter;
import retrofit2.Call;
import retrofit2.Response;

@Getter
public class BuyItemThread extends Thread {

    private Handler handler;
    private Handler responseHandler;

    public BuyItemThread(Handler handlerUI, Long shoppingListItemId){
        responseHandler = handlerUI;
        handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                ServiceUtils.shoppingListItemService.buy(shoppingListItemId).enqueue(new retrofit2.Callback<GenericResponse<Boolean>>(){

                    @Override
                    public void onResponse(Call<GenericResponse<Boolean>> call, Response<GenericResponse<Boolean>> response) {
                        System.out.println("Successfully bought item!");
                        responseHandler.sendMessage(ServiceUtils.getHandlerMessageFromResponse(response));
                    }

                    @Override
                    public void onFailure(Call<GenericResponse<Boolean>> call, Throwable t) {
                        System.out.println("Error buying item!");
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
