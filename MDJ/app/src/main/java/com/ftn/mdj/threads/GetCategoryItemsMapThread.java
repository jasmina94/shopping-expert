package com.ftn.mdj.threads;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.ftn.mdj.dto.CategoryItemDTO;
import com.ftn.mdj.services.ServiceUtils;
import com.ftn.mdj.utils.GenericResponse;

import java.util.HashMap;
import java.util.List;

import lombok.Getter;
import retrofit2.Call;
import retrofit2.Response;

@Getter
public class GetCategoryItemsMapThread extends Thread {

    private Handler handler;
    private Handler responseHandler;

    public GetCategoryItemsMapThread(Handler handlerUI) {
        this.responseHandler = handlerUI;
        this.handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                ServiceUtils.categoryItemService.getMap().enqueue(new retrofit2.Callback<GenericResponse<HashMap<String, List<CategoryItemDTO>>>>(){

                    @Override
                    public void onResponse(Call<GenericResponse<HashMap<String, List<CategoryItemDTO>>>> call, Response<GenericResponse<HashMap<String, List<CategoryItemDTO>>>> response) {
                        System.out.println("Successfully read list items!");
                        responseHandler.sendMessage(ServiceUtils.getHandlerMessageFromResponse(response));
                    }

                    @Override
                    public void onFailure(Call<GenericResponse<HashMap<String, List<CategoryItemDTO>>>> call, Throwable t) {
                        System.out.println("Error reading list items!");
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
