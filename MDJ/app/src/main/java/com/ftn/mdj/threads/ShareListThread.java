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

import java.util.List;

import lombok.Getter;
import retrofit2.Call;
import retrofit2.Response;

@Getter
public class ShareListThread extends Thread {
    private Handler handler;
    private Handler shareListHandler;
    private Context context;

    public ShareListThread(Long shoppingListId, String shareEmail) {
        this.context = MainActivity.instance.getApplicationContext();
        handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                ServiceUtils.listService.shareList(shoppingListId, shareEmail).enqueue(new retrofit2.Callback<GenericResponse<List<String>>>() {

                    @Override
                    public void onResponse(Call<GenericResponse<List<String>>> call, Response<GenericResponse<List<String>>> response) {
                        shareListHandler.sendMessage(ServiceUtils.getHandlerMessageFromResponse(response));
                    }

                    @Override
                    public void onFailure(Call<GenericResponse<List<String>>> call, Throwable t) {
                        System.out.println("Error while archiving list!");
                        shareListHandler.sendMessage(GenericResponse.getGenericServerErrorResponseMessage());
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
        shareListHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                GenericResponse<List<String>> response = (GenericResponse<List<String>>) msg.obj;
                if (response.isSuccessfulOperation()) {
                    ShareListActivity.instance.sendNotifications(response.getEntity());
                    UtilHelper.showToastMessage(ShareListActivity.instance, "List successfully shared!", UtilHelper.ToastLength.SHORT);
                } else {
                    UtilHelper.showToastMessage(ShareListActivity.instance, "User doesn't exist or is blocked!", UtilHelper.ToastLength.SHORT);
                }
            }
        };
        Looper.loop();
    }
}
