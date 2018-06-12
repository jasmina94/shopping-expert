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
public class SecretListThread extends Thread {
    private Handler handler;
    private Handler secretHandler;
    private Context context;

    public SecretListThread(Long shoppingListId, String password, Boolean isPublic){
        this.context = MainActivity.instance.getApplicationContext();
        handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                if(!isPublic) {
                    ServiceUtils.listService.makePublic(shoppingListId).enqueue(new retrofit2.Callback<GenericResponse<Boolean>>() {
                        @Override
                        public void onResponse(Call<GenericResponse<Boolean>> call, Response<GenericResponse<Boolean>> response) {
                            System.out.println("Successfully made list public!");
                            MainFragment.instance.changeListPrivacy(shoppingListId, false, password);
                            secretHandler.sendMessage(ServiceUtils.getHandlerMessageFromResponse(response));
                        }

                        @Override
                        public void onFailure(Call<GenericResponse<Boolean>> call, Throwable t) {
                            System.out.println("Error making list public!");
                            secretHandler.sendMessage(GenericResponse.getGenericServerErrorResponseMessage());
                        }
                    });
                } else {
                    ServiceUtils.listService.makeSecret(shoppingListId, password).enqueue(new retrofit2.Callback<GenericResponse<Boolean>>() {
                        @Override
                        public void onResponse(Call<GenericResponse<Boolean>> call, Response<GenericResponse<Boolean>> response) {
                            System.out.println("Successfully made list private!");
                            MainFragment.instance.changeListPrivacy(shoppingListId, true, password);
                            secretHandler.sendMessage(ServiceUtils.getHandlerMessageFromResponse(response));
                        }

                        @Override
                        public void onFailure(Call<GenericResponse<Boolean>> call, Throwable t) {
                            System.out.println("Error making list private!");
                            secretHandler.sendMessage(GenericResponse.getGenericServerErrorResponseMessage());
                        }
                    });
                }
                super.handleMessage(msg);
            }

        };
    }

    @Override
    public void run() {
        if(Looper.myLooper() == null) {
            Looper.prepare();
        }
        secretHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                GenericResponse<Boolean> response = (GenericResponse<Boolean>) msg.obj;
                if (response.isSuccessfulOperation()) {
                    UtilHelper.showToastMessage(MainFragment.instance.getContext(), "Successfully changed list privacy!", UtilHelper.ToastLength.SHORT);
                    MainFragment.instance.restartFragment();
                } else {
                    UtilHelper.showToastMessage(MainFragment.instance.getContext(), "Error while changing list privacy!", UtilHelper.ToastLength.SHORT);
                }
            }
        };

        Looper.loop();
    }
}
