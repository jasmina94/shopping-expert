package com.ftn.mdj.threads;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.ftn.mdj.activities.TrashActivity;
import com.ftn.mdj.services.ServiceUtils;
import com.ftn.mdj.utils.GenericResponse;
import com.ftn.mdj.utils.SharedPreferencesManager;
import com.ftn.mdj.utils.UtilHelper;

import lombok.Getter;
import retrofit2.Call;
import retrofit2.Response;

@Getter
public class DeleteListThread extends Thread{
    private Handler handler;
    private Handler deleteHandler;
    private Context context;
    private SharedPreferencesManager sharedPreferenceManager;

    public DeleteListThread(Long shoppingListId){
        this.context = TrashActivity.instance.getApplicationContext();
        sharedPreferenceManager = SharedPreferencesManager.getInstance();
        handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                ServiceUtils.listService.deleteList(shoppingListId).enqueue(new retrofit2.Callback<GenericResponse<Boolean>>(){

                    @Override
                    public void onResponse(Call<GenericResponse<Boolean>> call, Response<GenericResponse<Boolean>> response) {
                        System.out.println("Successfully deleted list!");
                        deleteHandler.sendMessage(ServiceUtils.getHandlerMessageFromResponse(response));
                    }

                    @Override
                    public void onFailure(Call<GenericResponse<Boolean>> call, Throwable t) {
                        System.out.println("Error deleting list!");
                        deleteHandler.sendMessage(GenericResponse.getGenericServerErrorResponseMessage());
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
        deleteHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                GenericResponse<Boolean> response = (GenericResponse<Boolean>) msg.obj;
                if (response.isSuccessfulOperation()) {
                    UtilHelper.showToastMessage(context, "Successfully deleted list!", UtilHelper.ToastLength.SHORT);
                    TrashActivity.instance.finish();
                    long userId = sharedPreferenceManager.getInt(SharedPreferencesManager.Key.USER_ID.name());
                    GetArchivedListsThread getArchivedListsThread = new GetArchivedListsThread(userId);
                    getArchivedListsThread.start();
                    Message msg2 = Message.obtain();
                    getArchivedListsThread.getHandler().sendMessage(msg2);
                } else {
                    UtilHelper.showToastMessage(context, "Error while deleting list!", UtilHelper.ToastLength.SHORT);
                }
            }
        };
        Looper.loop();
    }
}
