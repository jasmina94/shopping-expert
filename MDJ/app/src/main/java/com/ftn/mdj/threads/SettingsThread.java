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

import java.util.List;

import lombok.Getter;
import retrofit2.Call;
import retrofit2.Response;

@Getter
public class SettingsThread extends Thread {
    private Handler handler;
    private Handler settingsHandler;
    private Context context;

    public SettingsThread(Long userId, Boolean showNotifications){
        this.context = MainActivity.instance.getApplicationContext();
        handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                ServiceUtils.userService.saveSettings(userId, showNotifications).enqueue(new retrofit2.Callback<GenericResponse>() {

                    @Override
                    public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                        System.out.println("Successfully saved settings!");
                        settingsHandler.sendMessage(ServiceUtils.getHandlerMessageFromResponse(response));
                    }

                    @Override
                    public void onFailure(Call<GenericResponse> call, Throwable t) {
                        System.out.println("Error while saving settings!");
                        settingsHandler.sendMessage(GenericResponse.getGenericServerErrorResponseMessage());
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
        settingsHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                GenericResponse<Boolean> response = (GenericResponse<Boolean>) msg.obj;
                if (response.isSuccessfulOperation()) {
                    UtilHelper.showToastMessage(context, "Successfully saved settings!", UtilHelper.ToastLength.SHORT);
                } else {
                    UtilHelper.showToastMessage(context, "Error while saving settings!", UtilHelper.ToastLength.SHORT);
                }
            }
        };

        Looper.loop();
    }
}
