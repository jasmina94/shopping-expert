package com.ftn.mdj.threads;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.ftn.mdj.services.ServiceUtils;
import com.ftn.mdj.utils.GenericResponse;
import com.ftn.mdj.utils.UtilHelper;

import lombok.Getter;
import retrofit2.Call;
import retrofit2.Response;

@Getter
public class AddListThread extends Thread {
    private Handler handler;
    public AddListThread(Long userId, String listName, Context context){
        handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                ServiceUtils.listService.create(listName, userId).enqueue(new retrofit2.Callback<GenericResponse>(){

                    @Override
                    public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                        System.out.println("Meesage recieved successfully!");
                    }

                    @Override
                    public void onFailure(Call<GenericResponse> call, Throwable t) {
                        UtilHelper.showToastMessage(context, "Error getting data!", UtilHelper.ToastLength.LONG);
                    }
                });
                super.handleMessage(msg);
            }

        };
    }
}
