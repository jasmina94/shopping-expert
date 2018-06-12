package com.ftn.mdj.threads;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.ftn.mdj.activities.MainActivity;
import com.ftn.mdj.activities.ShoppingListActivity;
import com.ftn.mdj.dto.ShoppingListDTO;
import com.ftn.mdj.dto.ShoppingListItemDTO;
import com.ftn.mdj.fragments.MainFragment;
import com.ftn.mdj.services.ServiceUtils;
import com.ftn.mdj.utils.GenericResponse;
import com.ftn.mdj.utils.UtilHelper;

import lombok.Getter;
import retrofit2.Call;
import retrofit2.Response;

@Getter
public class EditItemThread extends Thread{
    private Handler handler;
    private Handler editItemHandler;
    private Context context;

    public EditItemThread(ShoppingListItemDTO shoppingListItemDTO) {
        this.context = context;
        handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                ServiceUtils.shoppingListItemService.updateItem(shoppingListItemDTO).enqueue(new retrofit2.Callback<GenericResponse<Boolean>>() {

                    @Override
                    public void onResponse(Call<GenericResponse<Boolean>> call, Response<GenericResponse<Boolean>> response) {
                        System.out.println("Successfully edited item!");
                        editItemHandler.sendMessage(ServiceUtils.getHandlerMessageFromResponse(response));
                    }

                    @Override
                    public void onFailure(Call<GenericResponse<Boolean>> call, Throwable t) {
                        System.out.println("Error while editing item!");
                        editItemHandler.sendMessage(GenericResponse.getGenericServerErrorResponseMessage());
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
        editItemHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                GenericResponse<Boolean> response = (GenericResponse<Boolean>) msg.obj;
                if (response.isSuccessfulOperation()) {
                    UtilHelper.showToastMessage(MainFragment.instance.getContext(), "Successfully edited item! Go back.", UtilHelper.ToastLength.SHORT);
                } else {
                    UtilHelper.showToastMessage(MainFragment.instance.getContext(), "Error while editing item!", UtilHelper.ToastLength.SHORT);
                }
                Intent i = new Intent();
            }
        };
        Looper.loop();
    }
}
