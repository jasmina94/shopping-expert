package com.ftn.mdj.threads;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.ftn.mdj.dto.CategoryItemDTO;
import com.ftn.mdj.dto.ShoppingListItemDTO;
import com.ftn.mdj.services.ServiceUtils;
import com.ftn.mdj.utils.GenericResponse;

import java.util.List;

import lombok.Getter;
import retrofit2.Call;
import retrofit2.Response;

@Getter
public class GetShoppingListItemsThread extends Thread {

    private Handler handler;
    private Handler responseHandler;

    public GetShoppingListItemsThread(Handler handlerUI) {
        this.responseHandler = handlerUI;
        this.handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                long listId =  msg.getData().getLong("listId");
                ServiceUtils.shoppingListItemService.getItemsForList(listId).enqueue(new retrofit2.Callback<GenericResponse<List<ShoppingListItemDTO>>>(){

                    @Override
                    public void onResponse(Call<GenericResponse<List<ShoppingListItemDTO>>> call, Response<GenericResponse<List<ShoppingListItemDTO>>> response) {
                        System.out.println("Successfully read list items!");
                        responseHandler.sendMessage(ServiceUtils.getHandlerMessageFromResponse(response));
                    }

                    @Override
                    public void onFailure(Call<GenericResponse<List<ShoppingListItemDTO>>> call, Throwable t) {
                        System.out.println("Error reading list items!");
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
