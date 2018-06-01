package com.ftn.mdj.threads;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.ftn.mdj.activities.MainActivity;
import com.ftn.mdj.fragments.MainFragment;
import com.ftn.mdj.services.ServiceUtils;
import com.ftn.mdj.utils.GenericResponse;
import com.ftn.mdj.utils.UtilHelper;

import lombok.Getter;
import retrofit2.Call;
import retrofit2.Response;

@Getter
public class RenameListThread extends Thread {
    private Handler handler;
    private Handler renameHandler;
    private Context context;

    public RenameListThread(Long shoppingListId, String newListName){
        this.context = MainActivity.instance.getApplicationContext();
        handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                ServiceUtils.listService.rename(shoppingListId, newListName).enqueue(new retrofit2.Callback<GenericResponse<Boolean>>(){

                    @Override
                    public void onResponse(Call<GenericResponse<Boolean>> call, Response<GenericResponse<Boolean>> response) {
                        System.out.println("Successfully renamed list!");
                        MainFragment.instance.renameListInArray(shoppingListId, newListName);
                        renameHandler.sendMessage(ServiceUtils.getHandlerMessageFromResponse(response));
                    }

                    @Override
                    public void onFailure(Call<GenericResponse<Boolean>> call, Throwable t) {
                        System.out.println("Error renaming list!");
                        renameHandler.sendMessage(GenericResponse.getGenericServerErrorResponseMessage());
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
        renameHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                GenericResponse<Boolean> response = (GenericResponse<Boolean>) msg.obj;
                if (response.isSuccessfulOperation()) {
                    UtilHelper.showToastMessage(MainFragment.instance.getContext(), "Successfully renamed list!", UtilHelper.ToastLength.SHORT);
                    MainFragment.instance.restartFragment();
                } else {
                    UtilHelper.showToastMessage(MainFragment.instance.getContext(), "Error while renaming list!", UtilHelper.ToastLength.SHORT);
                }
            }
        };
        Looper.loop();
    }

}
