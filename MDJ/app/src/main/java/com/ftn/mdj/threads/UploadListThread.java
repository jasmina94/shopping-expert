package com.ftn.mdj.threads;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.ftn.mdj.activities.MainActivity;
import com.ftn.mdj.dto.ShoppingListDTO;
import com.ftn.mdj.services.ServiceUtils;
import com.ftn.mdj.utils.GenericResponse;
import com.ftn.mdj.utils.UtilHelper;

import java.util.List;

import lombok.Getter;
import retrofit2.Call;
import retrofit2.Response;

@Getter
public class UploadListThread extends Thread {
    private Handler handler;

    public UploadListThread(Long userId, List<ShoppingListDTO> shoppingListShowDtos, Context context, MainActivity mainActivity){
        handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                ServiceUtils.listService.uploadList(userId, shoppingListShowDtos).enqueue(new retrofit2.Callback<GenericResponse>(){

                    @Override
                    public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                        GetActiveListsThread getActiveListsThread = new GetActiveListsThread(false, userId, mainActivity);
                        getActiveListsThread.start();
                        Message msg = Message.obtain();
                        getActiveListsThread.getHandler().sendMessage(msg);
                        UtilHelper.showToastMessage(context, "Uploaded list!", UtilHelper.ToastLength.LONG);
                    }

                    @Override
                    public void onFailure(Call<GenericResponse> call, Throwable t) {
                        UtilHelper.showToastMessage(context, "Error uploading data!", UtilHelper.ToastLength.LONG);
                    }
                });
                super.handleMessage(msg);
            }

        };
    }
}
