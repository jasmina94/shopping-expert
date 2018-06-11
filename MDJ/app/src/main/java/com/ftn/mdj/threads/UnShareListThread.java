package com.ftn.mdj.threads;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.ftn.mdj.activities.MainActivity;
import com.ftn.mdj.activities.ShareListActivity;
import com.ftn.mdj.services.ServiceUtils;
import com.ftn.mdj.utils.GenericResponse;
import com.ftn.mdj.utils.UtilHelper;

import lombok.Getter;
import retrofit2.Call;
import retrofit2.Response;

@Getter
public class UnShareListThread extends Thread {
    private Handler handler;
    private Handler unShareListHandler;
    private Context context;

    public UnShareListThread(Long shoppingListId, String shareEmail) {
        this.context = MainActivity.instance.getApplicationContext();
        handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                ServiceUtils.listService.unShareList(shoppingListId, shareEmail).enqueue(new retrofit2.Callback<GenericResponse<Boolean>>() {

                    @Override
                    public void onResponse(Call<GenericResponse<Boolean>> call, Response<GenericResponse<Boolean>> response) {
                        unShareListHandler.sendMessage(ServiceUtils.getHandlerMessageFromResponse(response));
                    }

                    @Override
                    public void onFailure(Call<GenericResponse<Boolean>> call, Throwable t) {
                        System.out.println("Error while archiving list!");
                        unShareListHandler.sendMessage(GenericResponse.getGenericServerErrorResponseMessage());
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
        unShareListHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                GenericResponse<Boolean> response = (GenericResponse<Boolean>) msg.obj;
                if (response.isSuccessfulOperation()) {
                    ShareListActivity.instance.restart();
                    UtilHelper.showToastMessage(ShareListActivity.instance, "List successfully unshared!", UtilHelper.ToastLength.SHORT);
                } else {
                    UtilHelper.showToastMessage(ShareListActivity.instance, "User doesn't exist or is blocked!", UtilHelper.ToastLength.SHORT);
                }
            }
        };
        Looper.loop();
    }
}
