package com.ftn.mdj.threads;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.ftn.mdj.fragments.MainFragment;
import com.ftn.mdj.services.ServiceUtils;
import com.ftn.mdj.utils.GenericResponse;

import lombok.Getter;
import retrofit2.Call;
import retrofit2.Response;

@Getter
public class RenameListThread extends Thread {
    private Handler handler;
    private Handler responseHandler;

    public RenameListThread(Handler handlerUI, Long shoppingListId, String newListName, MainFragment mainFragment){
        responseHandler = handlerUI;
        handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                ServiceUtils.listService.rename(shoppingListId, newListName).enqueue(new retrofit2.Callback<GenericResponse<Boolean>>(){

                    @Override
                    public void onResponse(Call<GenericResponse<Boolean>> call, Response<GenericResponse<Boolean>> response) {
                        System.out.println("Successfully renamed list!");
                        mainFragment.renameListInArray(shoppingListId, newListName);
                        responseHandler.sendMessage(ServiceUtils.getHandlerMessageFromResponse(response));
                    }

                    @Override
                    public void onFailure(Call<GenericResponse<Boolean>> call, Throwable t) {
                        System.out.println("Error renaming list!");
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
