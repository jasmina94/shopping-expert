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
public class RenameListThread extends Thread {
    private Handler handler;

    public RenameListThread(Long shoppingListId, String newListName, Context context, MainFragment mainFragment){
        handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                ServiceUtils.listService.updateName(shoppingListId, newListName).enqueue(new retrofit2.Callback<GenericResponse>(){

                    @Override
                    public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                        mainFragment.renameListInArray(shoppingListId, newListName);
                        UtilHelper.showToastMessage(context, "Renamed!", UtilHelper.ToastLength.LONG);
                    }

                    @Override
                    public void onFailure(Call<GenericResponse> call, Throwable t) {
                        UtilHelper.showToastMessage(context, "Error while renaming!", UtilHelper.ToastLength.LONG);

                    }
                });
                super.handleMessage(msg);
            }

        };
    }

}
