package com.ftn.mdj.threads;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.ftn.mdj.fragments.MainFragment;
import com.ftn.mdj.services.ServiceUtils;
import com.ftn.mdj.utils.GenericResponse;

import lombok.Getter;
import retrofit2.Call;
import retrofit2.Response;

@Getter
public class WorkerThreadRenameList extends Thread {
    private Handler handler;

    public WorkerThreadRenameList(Long shoppingListId, String newListName, Context context, MainFragment mainFragment){
        handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                ServiceUtils.listService.updateName(shoppingListId, newListName).enqueue(new retrofit2.Callback<GenericResponse>(){

                    @Override
                    public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                        mainFragment.renameListInArray(shoppingListId, newListName);
                        Toast.makeText(context, "Renamed", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<GenericResponse> call, Throwable t) {
                        Toast.makeText(context, "Error while renaming", Toast.LENGTH_LONG).show();
                        }
                });
                super.handleMessage(msg);
            }

        };
    }

}
