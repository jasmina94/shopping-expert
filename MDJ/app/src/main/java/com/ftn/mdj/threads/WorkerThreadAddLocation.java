package com.ftn.mdj.threads;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.ftn.mdj.fragments.MainFragment;
import com.ftn.mdj.services.ServiceUtils;
import com.ftn.mdj.utils.GenericResponse;

import retrofit2.Call;
import retrofit2.Response;
import lombok.Getter;

@Getter
public class WorkerThreadAddLocation extends Thread {
    private Handler handler;

    public WorkerThreadAddLocation(Long shoppingListId, Double latitude, Double longitude, Context context) {
        handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                ServiceUtils.listService.updateLocation(shoppingListId, latitude, longitude).enqueue(new retrofit2.Callback<GenericResponse>() {

                    @Override
                    public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                        Toast.makeText(context, "location", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<GenericResponse> call, Throwable t) {
                        Toast.makeText(context, "Error while adding location", Toast.LENGTH_LONG).show();
                    }
                });
                super.handleMessage(msg);
            }

        };
    }
}
