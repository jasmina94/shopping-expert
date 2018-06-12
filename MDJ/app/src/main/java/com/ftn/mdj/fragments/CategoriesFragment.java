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
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.ftn.mdj.R;
import com.ftn.mdj.activities.AddItemActivity;
import com.ftn.mdj.dto.CategoryDTO;
import com.ftn.mdj.dto.CategoryItemDTO;
import com.ftn.mdj.threads.AddCategoryItemAsShoppingListItemThread;
import com.ftn.mdj.threads.GetCategoriesThread;
import com.ftn.mdj.threads.GetCategoryItemsMapThread;
import com.ftn.mdj.threads.GetCategoryItemsThread;
import com.ftn.mdj.utils.GenericResponse;
import com.ftn.mdj.adapters.ExpandableCategoryListAdapter;
import com.ftn.mdj.utils.UtilHelper;

import java.util.List;
import java.util.HashMap;


public class CategoriesFragment extends Fragment {

    private Handler allCategoriesHandler;
    private Handler mapHandler;

    private View rootView;
    private FragmentActivity parentActivity;

    private ExpandableCategoryListAdapter expandableCategoryListAdapter;
    private ExpandableListView expListView;

    private List<CategoryDTO> listDataHeader;
    private HashMap<String, List<CategoryItemDTO>> listDataChild;

    private Handler handler;
    long listId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_categories, container, false);
        parentActivity = super.getActivity();

        listId = ((AddItemActivity)parentActivity).getListId();

        expandableCategoryListAdapter = new ExpandableCategoryListAdapter(this.getContext());
        expListView = (ExpandableListView)rootView.findViewById(R.id.exlv_cat);
        expListView.setAdapter(expandableCategoryListAdapter);

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                TextView idTxt = (TextView)v.findViewById(R.id.txt_list_category_item_id);
                long categoryItemId = Long.parseLong(idTxt.getText().toString());
                callAddCategoryItemAsItem(categoryItemId);
                return false;
            }
        });
        setAllCategoriesHandler();
        setMapHandler();
        setHandler();
        return rootView;
    }

    @Override
    public void onStart() {
        getAllCategories();
        getMap();
        super.onStart();
    }

    //********************* Operation to server *********************
    private void getAllCategories(){
        GetCategoriesThread getCategoriesThread = new GetCategoriesThread(allCategoriesHandler);
        getCategoriesThread.start();
        Message msg = Message.obtain();
        getCategoriesThread.getHandler().sendMessage(msg);
    }

    private void getMap(){
        GetCategoryItemsMapThread getCategoryItemsMapThread = new GetCategoryItemsMapThread(mapHandler);
        getCategoryItemsMapThread.start();
        Message msg = Message.obtain();
        getCategoryItemsMapThread.getHandler().sendMessage(msg);
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
                    listDataHeader = categoryDTOS;
                    expandableCategoryListAdapter.setListDataHeader(listDataHeader);
                    expandableCategoryListAdapter.notifyDataSetChanged();
                }else {
                    UtilHelper.showToastMessage(getContext(), response.getErrorMessage(), UtilHelper.ToastLength.LONG);
                }
            }
        };
    }


    private void setMapHandler() {
        mapHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                GenericResponse<HashMap<String, List<CategoryItemDTO>>> response = (GenericResponse<HashMap<String, List<CategoryItemDTO>>>) msg.obj;
                if (response.isSuccessfulOperation()) {
                    HashMap<String, List<CategoryItemDTO>> map = response.getEntity();
                    listDataChild = map;
                    expandableCategoryListAdapter.setListDataChild(map);
                    expandableCategoryListAdapter.notifyDataSetChanged();
                }else {
                    UtilHelper.showToastMessage(getContext(), response.getErrorMessage(), UtilHelper.ToastLength.LONG);
                }
            }
        };
    }
    // ******************************************

    private void callAddCategoryItemAsItem(long categoryItemId){
        AddCategoryItemAsShoppingListItemThread thread = new AddCategoryItemAsShoppingListItemThread(handler, categoryItemId, listId);
        thread.start();
        Message msg = Message.obtain();
        thread.getHandler().sendMessage(msg);
    }

    private void setHandler(){
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                GenericResponse<Boolean> response = (GenericResponse<Boolean>) msg.obj;
                if (response.isSuccessfulOperation()) {
                    getActivity().onBackPressed();
                }else {
                    UtilHelper.showToastMessage(getContext(), response.getErrorMessage(), UtilHelper.ToastLength.LONG);
                }
            }
        };
    }
}
