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
import com.ftn.mdj.dto.CategoryDTO;
import com.ftn.mdj.dto.CategoryItemDTO;
import com.ftn.mdj.threads.GetCategoriesThread;
import com.ftn.mdj.threads.GetCategoryItemsThread;
import com.ftn.mdj.utils.GenericResponse;

import java.util.List;


public class CategoriesFragment extends Fragment {

    private Handler allCategoriesHandler;
    private Handler specificCategoryItemHandler;

    private View rootView;
    private FragmentActivity parentActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_categories, container, false);
        parentActivity = super.getActivity();

        setAllCategoriesHandler();
        setSpecificCategoryItemHandler();

        getAllCategories();

        return rootView;
    }

    //********************* Operation to server *********************
    private void getAllCategories(){
        GetCategoriesThread getCategoriesThread = new GetCategoriesThread(allCategoriesHandler);
        getCategoriesThread.start();
        Message msg = Message.obtain();
        getCategoriesThread.getHandler().sendMessage(msg);
    }

    private void getSpecificCategoryItems(long categoryId){
        GetCategoryItemsThread getCategoryItemsThread = new GetCategoryItemsThread(specificCategoryItemHandler);
        getCategoryItemsThread.start();
        Message msg = Message.obtain();
        Bundle b = new Bundle();
        b.putLong("categoryId", categoryId);
        msg.setData(b);
        getCategoryItemsThread.getHandler().sendMessage(msg);
    }
    //***************************************************************


    // ********************* Handlers *********************
    private void setAllCategoriesHandler() {
        allCategoriesHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                GenericResponse<List<CategoryDTO>> response = (GenericResponse<List<CategoryDTO>>) msg.obj;
                if (response.isSuccessfulOperation()) {
                    List<CategoryDTO> categoryDTOS = response.getEntity();
                    for (CategoryDTO categoryDTO : categoryDTOS) {
                        System.out.println(categoryDTO.getCategoryName());
                    }
                }
            }
        };
    }
    private void setSpecificCategoryItemHandler() {
        specificCategoryItemHandler = new Handler(Looper.getMainLooper()) {
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
    // ******************************************
}
