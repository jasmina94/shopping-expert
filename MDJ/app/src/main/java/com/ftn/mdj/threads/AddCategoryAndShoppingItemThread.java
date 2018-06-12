package com.ftn.mdj.threads;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.ftn.mdj.dto.CategoryItemDTO;
import com.ftn.mdj.dto.ShoppingListItemDTO;
import com.ftn.mdj.services.ServiceUtils;
import com.ftn.mdj.utils.GenericResponse;

import lombok.Getter;
import retrofit2.Call;
import retrofit2.Response;

@Getter
public class AddCategoryAndShoppingItemThread extends Thread {

    private Handler handler;
    private Handler responseHandler;

    public AddCategoryAndShoppingItemThread(Handler handlerUI) {
        this.responseHandler = handlerUI;
        this.handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                CategoryItemDTO newItem = (CategoryItemDTO)msg.obj;
                long listId =  msg.getData().getLong("listId");

                ServiceUtils.categoryItemService.addNewCategoryAndShoppingListItem(newItem, listId).enqueue(new retrofit2.Callback<GenericResponse<Boolean>>(){

                    @Override
                    public void onResponse(Call<GenericResponse<Boolean>> call, Response<GenericResponse<Boolean>> response) {
                        System.out.println("Successfully added category item and shopping item to list!");
                        responseHandler.sendMessage(ServiceUtils.getHandlerMessageFromResponse(response));
                    }

                    @Override
                    public void onFailure(Call<GenericResponse<Boolean>> call, Throwable t) {
                        System.out.println("Error adding  category item and shopping item to list!");
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
