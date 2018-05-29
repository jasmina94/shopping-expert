package com.ftn.mdj.threads;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

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

    public SecretListThread(Long shoppingListId, String password, Boolean isPublic, Context context, MainFragment mainFragment){
        handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                if(!isPublic) {
                    ServiceUtils.listService.makePublic(shoppingListId, password).enqueue(new retrofit2.Callback<GenericResponse>() {
                        @Override
                        public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                            mainFragment.restartFragment();
                            UtilHelper.showToastMessage(context, "Your list is now public!", UtilHelper.ToastLength.LONG);
                        }

                        @Override
                        public void onFailure(Call<GenericResponse> call, Throwable t) {
                            UtilHelper.showToastMessage(context, "Error while making list public!", UtilHelper.ToastLength.LONG);
                        }
                    });
                } else {
                    ServiceUtils.listService.makeSecret(shoppingListId, password).enqueue(new retrofit2.Callback<GenericResponse>() {
                        @Override
                        public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                            mainFragment.restartFragment();
                            UtilHelper.showToastMessage(context, "Your list is now private!", UtilHelper.ToastLength.LONG);
                        }

                        @Override
                        public void onFailure(Call<GenericResponse> call, Throwable t) {
                            UtilHelper.showToastMessage(context, "Error while making list private!", UtilHelper.ToastLength.LONG);
                        }
                    });
                }
                super.handleMessage(msg);
            }

        };
    }
}
