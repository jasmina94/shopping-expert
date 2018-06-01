package com.ftn.mdj.threads;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.ftn.mdj.activities.MainActivity;
import com.ftn.mdj.dto.ShoppingListDTO;
import com.ftn.mdj.fragments.MainFragment;
import com.ftn.mdj.utils.GenericResponse;
import com.ftn.mdj.services.ServiceUtils;
import com.ftn.mdj.utils.UtilHelper;

import lombok.Getter;
import retrofit2.Call;
import retrofit2.Response;

@Getter
public class ArchiveListThread extends Thread {
    private Handler handler;
    private Handler archiveHandler;
    private Context context;

    public ArchiveListThread(Long shoppingListId) {
        this.context = MainActivity.instance.getApplicationContext();
        handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                ServiceUtils.listService.archive(shoppingListId).enqueue(new retrofit2.Callback<GenericResponse<ShoppingListDTO>>() {

                    @Override
                    public void onResponse(Call<GenericResponse<ShoppingListDTO>> call, Response<GenericResponse<ShoppingListDTO>> response) {
                        System.out.println("Successfully archived list!");
                        MainFragment.instance.archiveListUI(shoppingListId);
                        archiveHandler.sendMessage(ServiceUtils.getHandlerMessageFromResponse(response));
                    }

                    @Override
                    public void onFailure(Call<GenericResponse<ShoppingListDTO>> call, Throwable t) {
                        System.out.println("Error while archiving list!");
                        archiveHandler.sendMessage(GenericResponse.getGenericServerErrorResponseMessage());
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
        archiveHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                GenericResponse<ShoppingListDTO> response = (GenericResponse<ShoppingListDTO>) msg.obj;
                if (response.isSuccessfulOperation()) {
                    MainFragment.instance.restartFragment();
                    UtilHelper.showToastMessage(MainFragment.instance.getContext(), "Successfully archived list!", UtilHelper.ToastLength.SHORT);
                } else {
                    UtilHelper.showToastMessage(MainFragment.instance.getContext(), "Error while archiving list!", UtilHelper.ToastLength.SHORT);
                }
            }
        };
        Looper.loop();
    }
}
