package com.ftn.mdj.threads;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.ftn.mdj.dto.CategoryDTO;
import com.ftn.mdj.dto.LoginDTO;
import com.ftn.mdj.services.ServiceUtils;
import com.ftn.mdj.utils.GenericResponse;

import java.util.List;

import lombok.Getter;
import retrofit2.Call;
import retrofit2.Response;
@Getter
public class GetCategoriesThread extends Thread {

    private Handler handler;
    private Handler responseHandler;

    public GetCategoriesThread(Handler handlerUI) {
        this.responseHandler = handlerUI;
        this.handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                ServiceUtils.categoryService.getCategories().enqueue(new retrofit2.Callback<GenericResponse<List<CategoryDTO>>>(){

                    @Override
                    public void onResponse(Call<GenericResponse<List<CategoryDTO>>> call, Response<GenericResponse<List<CategoryDTO>>> response) {
                        System.out.println("Successfully read categories!");
                        responseHandler.sendMessage(ServiceUtils.getHandlerMessageFromResponse(response));
                    }

                    @Override
                    public void onFailure(Call<GenericResponse<List<CategoryDTO>>> call, Throwable t) {
                        System.out.println("Error reading categories!");
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
