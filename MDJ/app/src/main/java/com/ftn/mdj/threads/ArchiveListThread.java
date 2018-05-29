package com.ftn.mdj.threads;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.ftn.mdj.fragments.MainFragment;
import com.ftn.mdj.utils.GenericResponse;
import com.ftn.mdj.services.ServiceUtils;
import com.ftn.mdj.utils.UtilHelper;

import lombok.Getter;
import retrofit2.Call;
import retrofit2.Response;

@Getter
public class ArchiveListThread extends Thread {
        private Handler handler;

        public ArchiveListThread(Long shoppingListId, MainFragment mainFragment, Context context) {
            handler = new Handler() {

                @Override
                public void handleMessage(Message msg) {
                    ServiceUtils.listService.archive(shoppingListId).enqueue(new retrofit2.Callback<GenericResponse>() {

                        @Override
                        public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                            mainFragment.archiveListUI(shoppingListId);
                            UtilHelper.showToastMessage(context, "Archived!", UtilHelper.ToastLength.LONG);
                        }

                        @Override
                        public void onFailure(Call<GenericResponse> call, Throwable t) {
                            UtilHelper.showToastMessage(context, "Error while archiving!", UtilHelper.ToastLength.LONG);
                        }
                    });
                    super.handleMessage(msg);
                }

            };
        }
}
