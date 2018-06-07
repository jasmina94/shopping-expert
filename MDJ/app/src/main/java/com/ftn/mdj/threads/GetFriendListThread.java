package com.ftn.mdj.threads;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.ftn.mdj.activities.MainActivity;
import com.ftn.mdj.activities.ShareListActivity;
import com.ftn.mdj.activities.TrashActivity;
import com.ftn.mdj.adapters.ShareListAdapter;
import com.ftn.mdj.dto.ShoppingListDTO;
import com.ftn.mdj.fragments.MainFragment;
import com.ftn.mdj.services.ServiceUtils;
import com.ftn.mdj.utils.GenericResponse;
import com.ftn.mdj.utils.UtilHelper;

import java.util.ArrayList;
import java.util.Map;

import lombok.Getter;
import retrofit2.Call;
import retrofit2.Response;

@Getter
public class GetFriendListThread extends Thread {
    private Handler handler;
    private Handler getFriendListHandler;
    private Context context;


    public GetFriendListThread(Long listId, Long userId){
        this.context = MainActivity.instance.getApplicationContext();
        handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                ServiceUtils.listService.getFriendList(listId, userId).enqueue(new retrofit2.Callback<GenericResponse<Map<String, Boolean>>>() {

                    @Override
                    public void onResponse(Call<GenericResponse<Map<String, Boolean>>> call, Response<GenericResponse<Map<String, Boolean>>> response) {
                        System.out.println("Successfully gotten list!");
                        getFriendListHandler.sendMessage(ServiceUtils.getHandlerMessageFromResponse(response));
                    }

                    @Override
                    public void onFailure(Call<GenericResponse<Map<String, Boolean>>> call, Throwable t) {
                        System.out.println("Error while getting list!");
                        getFriendListHandler.sendMessage(GenericResponse.getGenericServerErrorResponseMessage());
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
        getFriendListHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                GenericResponse<Map<String, Boolean>> response = (GenericResponse<Map<String, Boolean>>) msg.obj;
                if(response.isSuccessfulOperation()){
                    ShareListActivity.instance.setFriendList(response.getEntity());
                    UtilHelper.showToastMessage(context,"Success!", UtilHelper.ToastLength.LONG);
                }else{
                    UtilHelper.showToastMessage(context,"Error while getting archived list from server!", UtilHelper.ToastLength.LONG);
                }
            }
        };
        Looper.loop();
    }
}
