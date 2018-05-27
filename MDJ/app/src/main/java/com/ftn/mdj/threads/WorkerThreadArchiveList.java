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
public class WorkerThreadArchiveList extends Thread {
        private Handler handler;

        public WorkerThreadArchiveList(Long shoppingListId, MainFragment mainFragment, Context context) {
            handler = new Handler() {

                @Override
                public void handleMessage(Message msg) {
                    ServiceUtils.listService.archive(shoppingListId).enqueue(new retrofit2.Callback<GenericResponse>() {

                        @Override
                        public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                            mainFragment.archiveListUI(shoppingListId);

                            Toast.makeText(context, "Archived", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(Call<GenericResponse> call, Throwable t) {
                            Toast.makeText(context, "Error while archiving", Toast.LENGTH_LONG).show();

                            System.out.println("Error sending registration data!");
                        }
                    });
                    super.handleMessage(msg);
                }

            };
        }
}
