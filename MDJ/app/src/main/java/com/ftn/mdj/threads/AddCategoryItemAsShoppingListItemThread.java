package com.ftn.mdj.threads;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.ftn.mdj.services.ServiceUtils;
import com.ftn.mdj.utils.GenericResponse;

import lombok.Getter;
import retrofit2.Call;
import retrofit2.Response;

@Getter
public class AddCategoryItemAsShoppingListItemThread extends Thread {

    private Handler handler;
    private Handler responseHandler;

    public AddCategoryItemAsShoppingListItemThread(Handler handlerUI, Long categoryItemId, Long shoppingListId){
        responseHandler = handlerUI;
        handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                ServiceUtils.categoryItemService.addShoppingListItem(categoryItemId, shoppingListId).enqueue(new retrofit2.Callback<GenericResponse<Boolean>>(){

                    @Override
                    public void onResponse(Call<GenericResponse<Boolean>> call, Response<GenericResponse<Boolean>> response) {
                        System.out.println("Successfully added location!");
                        responseHandler.sendMessage(ServiceUtils.getHandlerMessageFromResponse(response));
                    }

                    @Override
                    public void onFailure(Call<GenericResponse<Boolean>> call, Throwable t) {
                        System.out.println("Error adding location!");
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
