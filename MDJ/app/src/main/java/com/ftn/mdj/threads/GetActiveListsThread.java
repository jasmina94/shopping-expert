package com.ftn.mdj.threads;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;

import com.ftn.mdj.R;
import com.ftn.mdj.activities.MainActivity;
import com.ftn.mdj.dto.ShoppingListDTO;
import com.ftn.mdj.fragments.MainFragment;
import com.ftn.mdj.services.ServiceUtils;
import com.ftn.mdj.utils.GenericResponse;
import com.ftn.mdj.utils.UtilHelper;

import java.util.List;

import lombok.Getter;
import retrofit2.Call;
import retrofit2.Response;

@Getter
public class GetActiveListsThread extends Thread {
    private Handler handler;
    private Handler activeListHandler;
    private Context context;

    public GetActiveListsThread(boolean isArchived, Long userId){
        this.context = MainActivity.instance.getApplicationContext();
        handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                ServiceUtils.listService.listsByStatus(isArchived, userId).enqueue(new retrofit2.Callback<GenericResponse<List<ShoppingListDTO>>>(){

                    @Override
                    public void onResponse(Call<GenericResponse<List<ShoppingListDTO>>> call, Response<GenericResponse<List<ShoppingListDTO>>> response) {
                        System.out.println("Getting lists successfully!");
                        activeListHandler.sendMessage(ServiceUtils.getHandlerMessageFromResponse(response));
                    }

                    @Override
                    public void onFailure(Call<GenericResponse<List<ShoppingListDTO>>> call, Throwable t) {
                        System.out.println("Error getting lists!");
                        activeListHandler.sendMessage(GenericResponse.getGenericServerErrorResponseMessage());
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
        activeListHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                GenericResponse<List<ShoppingListDTO>> response = (GenericResponse<List<ShoppingListDTO>>) msg.obj;
                if(response.isSuccessfulOperation()){
                    MainFragment fragment = new MainFragment();
                    fragment.setActiveShoppingLists(response.getEntity());
                    FragmentTransaction fragmentTransaction = MainActivity.instance.getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, fragment);
                    fragmentTransaction.commit();
                }else{
                    UtilHelper.showToastMessage(context,"Error while getting active list from server!", UtilHelper.ToastLength.LONG);
                }
            }
        };
        Looper.loop();
    }
}
