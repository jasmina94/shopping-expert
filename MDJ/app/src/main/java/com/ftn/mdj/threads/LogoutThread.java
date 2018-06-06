package com.ftn.mdj.threads;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.ftn.mdj.activities.MainActivity;
import com.ftn.mdj.dto.ShoppingListDTO;
import com.ftn.mdj.fragments.MainFragment;
import com.ftn.mdj.services.ServiceUtils;
import com.ftn.mdj.utils.GenericResponse;
import com.ftn.mdj.utils.UtilHelper;

import lombok.Getter;
import retrofit2.Call;
import retrofit2.Response;

@Getter
public class LogoutThread extends Thread {
    private Handler handler;
    private Handler logoutHandler;
    private Context context;

    public LogoutThread(Long userId, String deviceInstance) {
        this.context = MainActivity.instance.getApplicationContext();
        handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                ServiceUtils.userService.logout(userId, deviceInstance).enqueue(new retrofit2.Callback<GenericResponse>() {

                    @Override
                    public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                        System.out.println("Successfully logout user!");
                        logoutHandler.sendMessage(ServiceUtils.getHandlerMessageFromResponse(response));
                    }

                    @Override
                    public void onFailure(Call<GenericResponse> call, Throwable t) {
                        System.out.println("Error while logging out user!");
                        logoutHandler.sendMessage(GenericResponse.getGenericServerErrorResponseMessage());
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
        logoutHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                GenericResponse<ShoppingListDTO> response = (GenericResponse) msg.obj;
                if (response.isSuccessfulOperation()) {
                    UtilHelper.showToastMessage(MainFragment.instance.getContext(), "Successfully removed deviceInstance!", UtilHelper.ToastLength.SHORT);
                } else {
                    UtilHelper.showToastMessage(MainFragment.instance.getContext(), "Error while removing deviceInstance!", UtilHelper.ToastLength.SHORT);
                }
            }
        };
        Looper.loop();
    }
}
