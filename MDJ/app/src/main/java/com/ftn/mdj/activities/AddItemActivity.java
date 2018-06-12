package com.ftn.mdj.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ftn.mdj.R;
import com.ftn.mdj.adapters.ListItemPagerAdapter;
import com.ftn.mdj.adapters.SectionPagerAdapter;
import com.ftn.mdj.dto.CategoryDTO;
import com.ftn.mdj.dto.CategoryItemDTO;
import com.ftn.mdj.dto.ShoppingListItemDTO;
import com.ftn.mdj.threads.AddCategoryAndShoppingItemThread;
import com.ftn.mdj.threads.GetCategoriesThread;
import com.ftn.mdj.threads.GetCategoryItemsThread;
import com.ftn.mdj.threads.RegisterThread;
import com.ftn.mdj.utils.GenericResponse;
import com.ftn.mdj.utils.UtilHelper;

import java.util.List;

import okhttp3.internal.Util;

public class AddItemActivity extends AppCompatActivity {


    private long listId;

    private ListItemPagerAdapter mListItemPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        Context context = this;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            listId = extras.getLong("LIST_ID");
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.add_item_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mListItemPagerAdapter = new ListItemPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.add_item_container);
        mViewPager.setAdapter(mListItemPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void switchFragment(int target){
        mViewPager.setCurrentItem(target);
    }

    public long getListId(){
        return this.listId;
    }


}
