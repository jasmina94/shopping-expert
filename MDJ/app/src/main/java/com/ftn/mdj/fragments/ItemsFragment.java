package com.ftn.mdj.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.ftn.mdj.R;
import com.ftn.mdj.activities.AddItemActivity;
import com.ftn.mdj.activities.ShoppingListActivity;
import com.ftn.mdj.adapters.CategoryItemsAdapter;
import com.ftn.mdj.dto.CategoryItemDTO;
import com.ftn.mdj.threads.AddCategoryAndShoppingItemThread;
import com.ftn.mdj.threads.GetCategoryItemsThread;
import com.ftn.mdj.utils.GenericResponse;
import com.ftn.mdj.utils.UtilHelper;

import java.util.ArrayList;
import java.util.List;


public class ItemsFragment extends Fragment {

    private Handler allCategoryItemsHandler;
    private Handler newItemAndCategoryItemHandler;
    private int otherCategoryId = 23;
    private long listId;

    private View rootView;
    private FragmentActivity parentActivity;

    private List<CategoryItemDTO> allCategoryItems = new ArrayList<>();

    private FloatingActionButton btnCreateNewItem;
    private AlertDialog dialog; //dialog za kreiranje

    private CategoryItemsAdapter categoryItemsAdapter;
    private RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_items, container, false);
        parentActivity = super.getActivity();
        listId = ((AddItemActivity)parentActivity).getListId();

        categoryItemsAdapter = new CategoryItemsAdapter(parentActivity);
        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.category_item_recview);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));

        mRecyclerView.setAdapter(categoryItemsAdapter);

        setAllItemsHandler();
        setNewItemAndCategoryItemHandler();

        btnCreateNewItem = (FloatingActionButton)rootView.findViewById(R.id.btn_new_shopping_item);
        btnCreateNewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(parentActivity);
                View mView = getLayoutInflater().inflate(R.layout.dialog_create_new_item, null);
                final EditText mItemName = (EditText) mView.findViewById(R.id.txt_dialog_new_item_name);
                Button mCreateNewItemBtn = (Button) mView.findViewById(R.id.dialog_new_btn);
                mBuilder.setView(mView);
                dialog = mBuilder.create();
                dialog.show();
                mCreateNewItemBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = mItemName.getText().toString();
                        if(!name.isEmpty()){
                            createNewItemOtherCategory(name);
                        }else {
                            UtilHelper.showToastMessage(parentActivity, "Item name can't be empty!", UtilHelper.ToastLength.LONG);
                            dialog.hide();
                        }
                    }
                });
            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        getAllCategoryItems();
        super.onStart();
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
                    allCategoryItems = categoryItemDTOS;
                    categoryItemsAdapter.setListData(allCategoryItems);
                    mRecyclerView.setVisibility(View.VISIBLE);
                }else {
                    UtilHelper.showToastMessage(getContext(), response.getErrorMessage(), UtilHelper.ToastLength.LONG);
                }
            }
        };
    }

    private void setNewItemAndCategoryItemHandler(){
        newItemAndCategoryItemHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                GenericResponse<Boolean> response = (GenericResponse<Boolean>) msg.obj;
                if (response.isSuccessfulOperation()) {
                    dialog.hide();
                    UtilHelper.showToastMessage(parentActivity, "Successfully created new item!", UtilHelper.ToastLength.SHORT);
                    Intent i = new Intent(parentActivity, ShoppingListActivity.class);
                    startActivity(i);
                }else {
                    dialog.hide();
                    UtilHelper.showToastMessage(parentActivity, response.getErrorMessage(), UtilHelper.ToastLength.SHORT);
                }
            }
        };
    }
    private void createNewItemOtherCategory(String name){
        CategoryItemDTO categoryItemDTO = new CategoryItemDTO();
        categoryItemDTO.setCategoryId(otherCategoryId);
        categoryItemDTO.setItemName(name);

        AddCategoryAndShoppingItemThread registerThread = new AddCategoryAndShoppingItemThread(newItemAndCategoryItemHandler);
        registerThread.start();
        Message msg = Message.obtain();
        Bundle b = new Bundle();
        b.putLong("listId", listId);
        msg.setData(b);
        msg.obj = categoryItemDTO;
        registerThread.getHandler().sendMessage(msg);
    }
}
