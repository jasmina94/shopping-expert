package com.ftn.mdj.threads;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.ftn.mdj.activities.MainActivity;
import com.ftn.mdj.dto.ShoppingListDTO;
import com.ftn.mdj.services.ServiceUtils;
import com.ftn.mdj.utils.DummyCollection;
import com.ftn.mdj.utils.GenericResponse;
import com.ftn.mdj.utils.SharedPreferencesManager;
import com.ftn.mdj.utils.UtilHelper;

import java.util.List;

import lombok.Getter;
import retrofit2.Call;
import retrofit2.Response;

@Getter
public class UploadListThread extends Thread {
    private Handler handler;
    private Handler uploadListHandler;
    private Context context;

    public UploadListThread(Long userId, List<ShoppingListDTO> lists){
        this.context = MainActivity.instance.getApplicationContext();
        this.handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                ServiceUtils.listService.uploadList(userId, lists).enqueue(new retrofit2.Callback<GenericResponse>(){

                    @Override
                    public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                        System.out.println("Successfully uploaded lists!");
                        uploadListHandler.sendMessage(ServiceUtils.getHandlerMessageFromResponse(response));
                    }

                    @Override
                    public void onFailure(Call<GenericResponse> call, Throwable t) {
                        System.out.println("Error uploading list to server!");
                        uploadListHandler.sendMessage(GenericResponse.getGenericServerErrorResponseMessage());
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
        uploadListHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                GenericResponse<Boolean> response = (GenericResponse<Boolean>) msg.obj;
                if(response.isSuccessfulOperation()){
                    //Ako je uspesno obavio cuvanje moram pozvati nit koja radi na dobavljanju aktivnih listi za tog korisnika
                    UtilHelper.showToastMessage(context, "Successfully uploaded lists from local storage to server!", UtilHelper.ToastLength.LONG);
                    DummyCollection.emptyList(context);
                    long userId = SharedPreferencesManager.getInstance(context).getInt(SharedPreferencesManager.Key.USER_ID.name());
                    GetActiveListsThread getActiveListsThread = new GetActiveListsThread(false, userId);
                    getActiveListsThread.start();
                    Message message = Message.obtain();
                    getActiveListsThread.getHandler().sendMessage(message);
                }else {
                    UtilHelper.showToastMessage(context, "Error while uploading lists from local storage to server!",
                            UtilHelper.ToastLength.LONG);
                }
            }
        };

        Looper.loop();
    }
}
