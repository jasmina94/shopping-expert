package com.ftn.mdj.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ftn.mdj.R;
import com.ftn.mdj.dto.CategoryItemDTO;
import com.ftn.mdj.threads.GetCategoryItemsThread;
import com.ftn.mdj.utils.GenericResponse;

import java.util.List;


public class ItemsFragment extends Fragment {

    private Handler allCategoryItemsHandler;

    private View rootView;
    private FragmentActivity parentActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_items, container, false);
        parentActivity = super.getActivity();

        setAllItemsHandler();
        getAllCategoryItems();

        return rootView;
    }


    private void getAllCategoryItems(){
        GetCategoryItemsThread getCategoryItemsThread = new GetCategoryItemsThread(allCategoryItemsHandler);
        getCategoryItemsThread.start();
        Message msg = Message.obtain();
        getCategoryItemsThread.getHandler().sendMessage(msg);
    }

    private void setAllItemsHandler(){
        allCategoryItemsHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                GenericResponse<List<CategoryItemDTO>> response = (GenericResponse<List<CategoryItemDTO>>) msg.obj;
                if (response.isSuccessfulOperation()) {
                    List<CategoryItemDTO> categoryItemDTOS = response.getEntity();
                    for (CategoryItemDTO categoryItemDTO : categoryItemDTOS) {
                        System.out.println(categoryItemDTO.getItemName() + categoryItemDTO.getCategoryId());
                    }
                }
            }
        };
    }
}
