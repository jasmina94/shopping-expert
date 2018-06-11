package com.ftn.mdj.threads;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.ftn.mdj.dto.CategoryItemDTO;
import com.ftn.mdj.services.ServiceUtils;
import com.ftn.mdj.utils.GenericResponse;

import java.util.List;

import lombok.Getter;
import retrofit2.Call;
import retrofit2.Response;

@Getter
public class GetCategoryItemsThread extends Thread {

    private Handler handler;
    private Handler responseHandler;

    public GetCategoryItemsThread(Handler handlerUI) {
        this.responseHandler = handlerUI;
        this.handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                ServiceUtils.categoryItemService.getAllCategoryItems().enqueue(new retrofit2.Callback<GenericResponse<List<CategoryItemDTO>>>(){

                    @Override
                    public void onResponse(Call<GenericResponse<List<CategoryItemDTO>>> call, Response<GenericResponse<List<CategoryItemDTO>>> response) {
                        System.out.println("Successfully read category items!");
                        responseHandler.sendMessage(ServiceUtils.getHandlerMessageFromResponse(response));
                    }

                    @Override
                    public void onFailure(Call<GenericResponse<List<CategoryItemDTO>>> call, Throwable t) {
                        System.out.println("Error reading category items!");
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
