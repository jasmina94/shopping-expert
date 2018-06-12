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
public class RemoveDeviceInstanceThread extends Thread {
    private Handler handler;
    private Handler removeDeviceInstaneHandler;
    private Context context;

    public RemoveDeviceInstanceThread(String deviceInstance){
        this.context = MainActivity.instance.getApplicationContext();
        handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                ServiceUtils.userService.removeUnusedDeviceInstances(deviceInstance).enqueue(new retrofit2.Callback<GenericResponse>(){

                    @Override
                    public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                        removeDeviceInstaneHandler.sendMessage(ServiceUtils.getHandlerMessageFromResponse(response));
                    }

                    @Override
                    public void onFailure(Call<GenericResponse> call, Throwable t) {
                        removeDeviceInstaneHandler.sendMessage(GenericResponse.getGenericServerErrorResponseMessage());
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
        removeDeviceInstaneHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                GenericResponse response = (GenericResponse) msg.obj;
                if (response.isSuccessfulOperation()) {
                    UtilHelper.showToastMessage(MainFragment.instance.getContext(), "Removed device instance!", UtilHelper.ToastLength.SHORT);
                    MainFragment.instance.restartFragment();
                } else {
                    UtilHelper.showToastMessage(MainFragment.instance.getContext(), "Error while removing device instance!", UtilHelper.ToastLength.SHORT);
                }
            }
        };
        Looper.loop();
    }

}
