package com.ftn.mdj.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.ftn.mdj.R;
import com.ftn.mdj.dto.ShoppingListShowDTO;
import com.ftn.mdj.fragments.MainFragment;
import com.ftn.mdj.threads.WorkerThreadGetActiveLists;
import com.ftn.mdj.utils.GenericResponse;
import com.ftn.mdj.utils.ServiceUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView mNavigationView;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    private List<ShoppingListShowDTO> activeLists = new ArrayList<>();

    private  boolean userLogedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mDrawerLayout = findViewById(R.id.main_drawer);

        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close);
        mToggle.syncState();

        mNavigationView = findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        changeDrawerContent();
        setSignInUpListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        WorkerThreadGetActiveLists workerThreadGetActiveLists = new WorkerThreadGetActiveLists(false, (long) 1, this);
        workerThreadGetActiveLists.start();
        Message msg = Message.obtain();
        workerThreadGetActiveLists.getHandler().sendMessage(msg);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnu_trash: {
                Toast.makeText(MainActivity.this, "Show trash fragment!",Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.mnu_help: {
                Toast.makeText(MainActivity.this, "Show help fragment!",Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.mnu_settings:{
                Toast.makeText(MainActivity.this, "Show settings fragment!",Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.mnu_logout:{
                Toast.makeText(MainActivity.this, "Log out user!",Toast.LENGTH_SHORT).show();
                break;
            }
        }

        //Close navigation drawer
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void changeDrawerContent(){
        View headerView = mNavigationView.getHeaderView(0);
        TextView emailText = headerView.findViewById(R.id.user_email);
        Button button = headerView.findViewById(R.id.btn_sign_in);
        if(userLogedIn){
            button.setVisibility(View.INVISIBLE);
            emailText.setVisibility(View.VISIBLE);
            emailText.setText("logedin@email.com"); //Put here email of logged in user if exists
        }else {
            emailText.setVisibility(View.INVISIBLE);
            Menu navMenu = mNavigationView.getMenu();
            navMenu.findItem(R.id.mnu_logout).setVisible(false);
        }
    }

    private void setSignInUpListener() {
        View headerView = mNavigationView.getHeaderView(0);
        Button button = headerView.findViewById(R.id.btn_sign_in);
        button.setOnClickListener(view -> {
            Context context = view.getContext();
            Toast.makeText(context, "Change activity", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, LogRegActivity.class);
            startActivity(intent);
        });
    }

}
