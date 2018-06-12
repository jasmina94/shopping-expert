package com.ftn.mdj.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ftn.mdj.R;
import com.ftn.mdj.adapters.MainAdapter;
import com.ftn.mdj.adapters.ShoppingListAdapter;
import com.ftn.mdj.dto.CategoryDTO;
import com.ftn.mdj.dto.CategoryItemDTO;
import com.ftn.mdj.dto.ShoppingListItemDTO;
import com.ftn.mdj.threads.GetCategoriesThread;
import com.ftn.mdj.threads.GetCategoryItemsThread;
import com.ftn.mdj.threads.GetShoppingListItemsThread;
import com.ftn.mdj.utils.GenericResponse;

import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;

public class ShoppingListActivity extends AppCompatActivity  {

    private Toolbar mToolbar;

    private Handler itemsHandler;

    private long listId;
    private String listName;
    private List<ShoppingListItemDTO> listItemDTOS = new ArrayList<>();

    private FloatingActionButton mBtnAddItem;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    private TextView mEmptyListTxt;
    private ImageView mEmptyListImg;

    private TextView listNameTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        setItemsHandler();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            listId = extras.getLong("LIST_ID");
            listName = extras.getString("LIST_NAME");
        }

        mBtnAddItem = (FloatingActionButton)findViewById(R.id.btn_add_item);
        mBtnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ShoppingListActivity.this, AddItemActivity.class);
                i.putExtra("LIST_ID",listId);
                startActivity(i);
            }
        });

        mRecyclerView = (RecyclerView)findViewById(R.id.sh_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));

        mAdapter = new ShoppingListAdapter(listItemDTOS, this);
        mRecyclerView.setAdapter(mAdapter);

        mEmptyListImg = (ImageView)findViewById(R.id.sh_empty_view_img);
        mEmptyListTxt = (TextView) findViewById(R.id.sh_empty_view);

        listNameTxt = (TextView)findViewById(R.id.sh_txt_list_name);
        listNameTxt.setText(listName);
    }

    @Override
    protected void onStart() {
        getAllItemsForList();
        super.onStart();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    private void getAllItemsForList(){
        GetShoppingListItemsThread getShoppingListItemsThread = new GetShoppingListItemsThread(itemsHandler);
        getShoppingListItemsThread.start();
        Message msg = Message.obtain();
        Bundle b = new Bundle();
        b.putLong("listId", listId);
        msg.setData(b);
        getShoppingListItemsThread.getHandler().sendMessage(msg);
    }


    private void setItemsHandler(){
        Context context = this;
        itemsHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                GenericResponse<List<ShoppingListItemDTO>> response = (GenericResponse<List<ShoppingListItemDTO>>) msg.obj;
                if (response.isSuccessfulOperation()) {
                    List<ShoppingListItemDTO> itemDTOS = response.getEntity();
                    if(itemDTOS.size() != 0){
                        listItemDTOS = itemDTOS;
                        mAdapter = new ShoppingListAdapter(listItemDTOS, context);
                        mRecyclerView.setAdapter(mAdapter);
                        mRecyclerView.setVisibility(View.VISIBLE);
                        mEmptyListImg.setVisibility(View.INVISIBLE);
                        mEmptyListTxt.setVisibility(View.INVISIBLE);
                    }else {
                        mRecyclerView.setVisibility(View.INVISIBLE);
                        mEmptyListImg.setVisibility(View.VISIBLE);
                        mEmptyListTxt.setVisibility(View.VISIBLE);
                    }
                }
            }
        };
    }
}
