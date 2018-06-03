package com.ftn.mdj.activities;

import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ftn.mdj.R;
import com.ftn.mdj.adapters.MainAdapter;
import com.ftn.mdj.adapters.TrashAdapter;
import com.ftn.mdj.dto.ShoppingListDTO;
import com.ftn.mdj.threads.GetActiveListsThread;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TrashActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private FloatingActionButton mBtnAddList;
    private TextView mEmptyView;
    private ImageView mEmptyImgView;
    private Toolbar mToolbar;

    private List<ShoppingListDTO> archivedShoppingLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trash);

        ArrayList<ShoppingListDTO> list = (ArrayList<ShoppingListDTO>) getIntent().getSerializableExtra("archivedShoppingLists");

        archivedShoppingLists = list;

        mRecyclerView = (RecyclerView)findViewById(R.id.trash_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(TrashActivity.this));

        mAdapter = new TrashAdapter(archivedShoppingLists, TrashActivity.this);
        mRecyclerView.setAdapter(mAdapter);

        mEmptyView = (TextView) findViewById(R.id.trash_empty_view);
        mEmptyImgView = (ImageView) findViewById(R.id.trash_empty_view_img);

        Toast.makeText(TrashActivity.this, "trash", Toast.LENGTH_SHORT).show();

        if(archivedShoppingLists.size()!=0){
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.INVISIBLE);
            mEmptyImgView.setVisibility(View.INVISIBLE);
        }else {
            mRecyclerView.setVisibility(View.INVISIBLE);
            mEmptyView.setVisibility(View.VISIBLE);
            mEmptyImgView.setVisibility(View.VISIBLE);
        }
        /*mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/
    }
}
