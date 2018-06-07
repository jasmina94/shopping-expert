package com.ftn.mdj.threads;

import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.ftn.mdj.R;
import com.ftn.mdj.activities.MainActivity;
import com.ftn.mdj.activities.SettingsActivity;
import com.ftn.mdj.fragments.SettingsFragment;
import com.ftn.mdj.services.ServiceUtils;
import com.ftn.mdj.utils.GenericResponse;
import com.ftn.mdj.utils.SharedPreferencesManager;
import com.ftn.mdj.utils.UtilHelper;

import java.util.List;

import lombok.Getter;
import retrofit2.Call;
import retrofit2.Response;

@Getter
public class SaveBlockedUsersThread extends Thread {
    private Handler handler;
    private Handler blockedUsersHandler;
    private Context context;

    public SaveBlockedUsersThread(Long userId, String email, Boolean toBlock) {
        this.context = MainActivity.instance.getApplicationContext();
        handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                ServiceUtils.userService.saveBlockedUsers(userId, email, toBlock).enqueue(new retrofit2.Callback<GenericResponse<List<String>>>() {

                    @Override
                    public void onResponse(Call<GenericResponse<List<String>>> call, Response<GenericResponse<List<String>>> response) {
                        System.out.println("Successfully saved blocked users!");
                        blockedUsersHandler.sendMessage(ServiceUtils.getHandlerMessageFromResponse(response));
                    }

                    @Override
                    public void onFailure(Call<GenericResponse<List<String>>> call, Throwable t) {
                        System.out.println("Error while saving blocked users!");
                        blockedUsersHandler.sendMessage(GenericResponse.getGenericServerErrorResponseMessage());
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
        blockedUsersHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                GenericResponse<List<String>> response = (GenericResponse<List<String>>) msg.obj;
                if (response.isSuccessfulOperation()) {
                    SettingsFragment fragment = new SettingsFragment();
                    fragment.setBlockedUsers(response.getEntity());
                    FragmentTransaction fragmentTransaction = SettingsActivity.instance.getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.settings_layout, fragment);
                    fragmentTransaction.commit();
                    UtilHelper.showToastMessage(context, "Successfully saved blocked users!", UtilHelper.ToastLength.SHORT);
                } else {
                    UtilHelper.showToastMessage(context, "Error while saving blocked users!", UtilHelper.ToastLength.SHORT);
                }
            }
        };

        Looper.loop();
    }
}
