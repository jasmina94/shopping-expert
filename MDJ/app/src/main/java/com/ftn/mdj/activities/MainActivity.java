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
import com.ftn.mdj.utils.DummyCollection;
import com.ftn.mdj.utils.GenericResponse;
import com.ftn.mdj.utils.ServiceUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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

    private  boolean userLogedIn = false;  //for test

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainActivity.WorkerThread workerThread = new MainActivity.WorkerThread(false);
        workerThread.start();
        Message msg = Message.obtain();
        workerThread.handler.sendMessage(msg);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawer);

        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close);
        mToggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        changeDrawerContent();
        setSignInUpListener();
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
        TextView emailText = (TextView) headerView.findViewById(R.id.user_email);
        Button button = (Button)headerView.findViewById(R.id.btn_sign_in);
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
        Button button = (Button) headerView.findViewById(R.id.btn_sign_in);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Toast.makeText(context, "Change activity", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, LogRegActivity.class);
                startActivity(intent);
            }
        });
    }

    private class WorkerThread extends Thread{
        private Handler handler;

        public WorkerThread(final Boolean archived){
            handler = new Handler(){

                @Override
                public void handleMessage(Message msg) {
                    ServiceUtils.listService.listsByStatus(archived).enqueue(new retrofit2.Callback<GenericResponse>(){

                        @Override
                        public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                            activeLists = (List<ShoppingListShowDTO>) response.body().getEntity();
                            MainFragment fragment = new MainFragment();
                            fragment.setActiveShoppingLists(activeLists);
                            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
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


}
