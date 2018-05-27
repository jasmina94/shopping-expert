package com.ftn.mdj.threads;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.ftn.mdj.fragments.MainFragment;
import com.ftn.mdj.utils.GenericResponse;
import com.ftn.mdj.utils.ServiceUtils;

import lombok.Getter;
import retrofit2.Call;
import retrofit2.Response;

@Getter
public class WorkerThreadSecretList extends Thread {

    private Handler handler;

    public WorkerThreadSecretList(Long shoppingListId, String password, Boolean isPublic, Context context, MainFragment mainFragment){
        handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                if(!isPublic) {
                    ServiceUtils.listService.makePublic(shoppingListId, password).enqueue(new retrofit2.Callback<GenericResponse>() {

                        @Override
                        public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                            mainFragment.restartFragment();
                            Toast.makeText(context, "Renamed", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(Call<GenericResponse> call, Throwable t) {
                            Toast.makeText(context, "Error while renaming", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    ServiceUtils.listService.makeSecret(shoppingListId, password).enqueue(new retrofit2.Callback<GenericResponse>() {

                        @Override
                        public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                            mainFragment.restartFragment();
                            Toast.makeText(context, "Renamed", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(Call<GenericResponse> call, Throwable t) {
                            Toast.makeText(context, "Error while renaming", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                super.handleMessage(msg);
            }

        };
    }
}
