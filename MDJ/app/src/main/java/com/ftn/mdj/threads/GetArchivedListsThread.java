package com.ftn.mdj.threads;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;

import com.ftn.mdj.R;
import com.ftn.mdj.activities.MainActivity;
import com.ftn.mdj.activities.TrashActivity;
import com.ftn.mdj.dto.ShoppingListDTO;
import com.ftn.mdj.fragments.MainFragment;
import com.ftn.mdj.services.ServiceUtils;
import com.ftn.mdj.utils.GenericResponse;
import com.ftn.mdj.utils.SharedPreferencesManager;
import com.ftn.mdj.utils.UtilHelper;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import retrofit2.Call;
import retrofit2.Response;

@Getter
public class GetArchivedListsThread extends Thread{
    private Handler handler;
    private Handler archivedListHandler;
    private Context context;

    public GetArchivedListsThread(Long userId){
        this.context = MainActivity.instance.getApplicationContext();
        handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                ServiceUtils.listService.listsByStatus(true, userId).enqueue(new retrofit2.Callback<GenericResponse<List<ShoppingListDTO>>>(){

                    @Override
                    public void onResponse(Call<GenericResponse<List<ShoppingListDTO>>> call, Response<GenericResponse<List<ShoppingListDTO>>> response) {
                        System.out.println("Getting archived lists successfully!");
                        archivedListHandler.sendMessage(ServiceUtils.getHandlerMessageFromResponse(response));
                    }

                    @Override
                    public void onFailure(Call<GenericResponse<List<ShoppingListDTO>>> call, Throwable t) {
                        System.out.println("Error getting archived lists!");
                        archivedListHandler.sendMessage(GenericResponse.getGenericServerErrorResponseMessage());
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
        archivedListHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                GenericResponse<ArrayList<ShoppingListDTO>> response = (GenericResponse<ArrayList<ShoppingListDTO>>) msg.obj;
                if(response.isSuccessfulOperation()){
                    Intent intent = new Intent(context, TrashActivity.class);
                    ArrayList<ShoppingListDTO> ddd = response.getEntity();

                    intent.putExtra("archivedShoppingLists", ddd);
                    context.startActivity(intent);
                }else{
                    UtilHelper.showToastMessage(context,"Error while getting archived list from server!", UtilHelper.ToastLength.LONG);
                }
            }
        };
        Looper.loop();
    }
}
