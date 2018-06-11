package com.ftn.mdj.threads;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.ftn.mdj.activities.MainActivity;
import com.ftn.mdj.fragments.MainFragment;
import com.ftn.mdj.services.ServiceUtils;
import com.ftn.mdj.utils.GenericResponse;
import com.ftn.mdj.utils.UtilHelper;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import retrofit2.Call;
import retrofit2.Response;

@Getter
public class AddReminderThread extends Thread {
    private Handler handler;
    private Handler reminderHandler;
    private Context context;

    public AddReminderThread(Long shoppingListId, String date, String time) {
        this.context = MainActivity.instance.getApplicationContext();
        handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                Map<String, String> timeAndDate = new HashMap<>();
                timeAndDate.put("date", date);
                timeAndDate.put("time", time);
                ServiceUtils.listService.addReminder(shoppingListId, timeAndDate).enqueue(new retrofit2.Callback<GenericResponse<Boolean>>() {

                    @Override
                    public void onResponse(Call<GenericResponse<Boolean>> call, Response<GenericResponse<Boolean>> response) {

                        reminderHandler.sendMessage(ServiceUtils.getHandlerMessageFromResponse(response));
                    }

                    @Override
                    public void onFailure(Call<GenericResponse<Boolean>> call, Throwable t) {
                        System.out.println("Error while archiving list!");
                        reminderHandler.sendMessage(GenericResponse.getGenericServerErrorResponseMessage());
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
        reminderHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                GenericResponse<Boolean> response = (GenericResponse<Boolean>) msg.obj;
                if (response.isSuccessfulOperation()) {
                    Intent intent = new Intent(context, MainActivity.class);
                    context.startActivity(intent);
                    UtilHelper.showToastMessage(MainFragment.instance.getContext(), "Successfully added reminder!", UtilHelper.ToastLength.SHORT);
                } else {
                    UtilHelper.showToastMessage(MainFragment.instance.getContext(), "Error while adding reminder!", UtilHelper.ToastLength.SHORT);
                }
            }
        };
        Looper.loop();
    }

}
