package com.ftn.mdj.threads;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;

import com.ftn.mdj.R;
import com.ftn.mdj.activities.MainActivity;
import com.ftn.mdj.dto.ShoppingListShowDTO;
import com.ftn.mdj.fragments.MainFragment;
import com.ftn.mdj.services.ServiceUtils;
import com.ftn.mdj.utils.GenericResponse;

import java.io.IOException;
import java.util.List;

import lombok.Getter;
import retrofit2.Call;
import retrofit2.Response;

@Getter
public class WorkerThreadGetActiveLists extends Thread {
    private Handler handler;

    public WorkerThreadGetActiveLists(Boolean archived, Long userId, MainActivity mainActivity){
        handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                ServiceUtils.listService.listsByStatus(archived, userId).enqueue(new retrofit2.Callback<GenericResponse>(){

                    @Override
                    public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                        List<ShoppingListShowDTO> activeLists = (List<ShoppingListShowDTO>) response.body().getEntity();
                        MainFragment fragment = new MainFragment();
                        try {
                            fragment.setActiveShoppingLists(activeLists);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        FragmentTransaction fragmentTransaction = mainActivity.getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_container, fragment);
                        fragmentTransaction.commit();
                        System.out.println("Meesage recieved successfully!");
                    }

                    @Override
                    public void onFailure(Call<GenericResponse> call, Throwable t) {
                        System.out.println("Error sending registration data!");
                    }
                });
                super.handleMessage(msg);
            }

        };
    }
}
