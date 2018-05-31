package com.ftn.mdj.threads;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.ftn.mdj.fragments.MainFragment;
import com.ftn.mdj.services.ServiceUtils;
import com.ftn.mdj.utils.GenericResponse;
import com.ftn.mdj.utils.UtilHelper;

import lombok.Getter;
import retrofit2.Call;
import retrofit2.Response;

@Getter
public class SecretListThread extends Thread {
    private Handler handler;
    private Handler responseHandler;

    public SecretListThread(Handler handlerUI, Long shoppingListId, String password, Boolean isPublic){
        responseHandler = handlerUI;
        handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                if(!isPublic) {
                    ServiceUtils.listService.makePublic(shoppingListId, password).enqueue(new retrofit2.Callback<GenericResponse<Boolean>>() {
                        @Override
                        public void onResponse(Call<GenericResponse<Boolean>> call, Response<GenericResponse<Boolean>> response) {
                            System.out.println("Successfully made list public!");
                            responseHandler.sendMessage(ServiceUtils.getHandlerMessageFromResponse(response));
                        }

                        @Override
                        public void onFailure(Call<GenericResponse<Boolean>> call, Throwable t) {
                            System.out.println("Error making list public!");
                            responseHandler.sendMessage(GenericResponse.getGenericServerErrorResponseMessage());
                        }
                    });
                } else {
                    ServiceUtils.listService.makeSecret(shoppingListId, password).enqueue(new retrofit2.Callback<GenericResponse<Boolean>>() {
                        @Override
                        public void onResponse(Call<GenericResponse<Boolean>> call, Response<GenericResponse<Boolean>> response) {
                            System.out.println("Successfully made list private!");
                            responseHandler.sendMessage(ServiceUtils.getHandlerMessageFromResponse(response));
                        }

                        @Override
                        public void onFailure(Call<GenericResponse<Boolean>> call, Throwable t) {
                            System.out.println("Error making list private!");
                            responseHandler.sendMessage(GenericResponse.getGenericServerErrorResponseMessage());
                        }
                    });
                }
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
