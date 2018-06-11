package com.ftn.mdj.activities;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ftn.mdj.R;
import com.ftn.mdj.dto.CategoryDTO;
import com.ftn.mdj.dto.CategoryItemDTO;
import com.ftn.mdj.threads.GetCategoriesThread;
import com.ftn.mdj.threads.GetCategoryItemsThread;
import com.ftn.mdj.utils.GenericResponse;

import java.util.List;

public class AddItemActivity extends AppCompatActivity {

    private Handler categoriesHandler;
    private Handler categoryItemsHandler;
    private Handler specificCategoryItemHandler;

    private long listId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        setCategoriesHandler();
        setCategoryItemsHandler();
        setSpecificCategoryItemHandler();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            listId = extras.getLong("LIST_ID");
        }
    }

    private void getAllCategories(){
        GetCategoriesThread getCategoriesThread = new GetCategoriesThread(categoriesHandler);
        getCategoriesThread.start();
        Message msg = Message.obtain();
        getCategoriesThread.getHandler().sendMessage(msg);
    }

    private void getAllCategoryItems(){
        GetCategoryItemsThread getCategoryItemsThread = new GetCategoryItemsThread(categoryItemsHandler);
        getCategoryItemsThread.start();
        Message msg = Message.obtain();
        getCategoryItemsThread.getHandler().sendMessage(msg);
    }

    private void getSpecificCategoryItems(long categoryId){
        GetCategoryItemsThread getCategoryItemsThread = new GetCategoryItemsThread(categoryItemsHandler);
        getCategoryItemsThread.start();
        Message msg = Message.obtain();
        Bundle b = new Bundle();
        b.putLong("categoryId", categoryId);
        msg.setData(b);
        getCategoryItemsThread.getHandler().sendMessage(msg);
    }

    private void setCategoriesHandler() {
        categoriesHandler = new Handler(Looper.getMainLooper()) {
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

    private void setCategoryItemsHandler(){
        categoryItemsHandler = new Handler(Looper.getMainLooper()) {
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
}
