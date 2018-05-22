package com.ftn.mdj.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
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
import com.ftn.mdj.dto.ShoppingListDTO;
import com.ftn.mdj.dto.UserDTO;
import com.ftn.mdj.fragments.MainFragment;
import com.ftn.mdj.services.MDJInterceptor;
import com.ftn.mdj.services.ServiceUtils;
import com.ftn.mdj.utils.Constants;
import com.ftn.mdj.utils.GenericResponse;
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

    private static final String SHOPPING_LIST_FILE = "shopping_lists.txt";
    public static final int SHARED_PREFS_AUTH_MODE = MODE_PRIVATE;

    private NavigationView mNavigationView;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    private  boolean userLogedIn;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO : Change this to show lists from database if loged in or from file if not
//        DummyCollection dummyCollection = new DummyCollection();
//        write_lists(dummyCollection.getDummies());
//        List<ShoppingListDTO> lists = read_lists();
        List<ShoppingListDTO> lists = new ArrayList<>();

        MainFragment fragment = new MainFragment();
        fragment.setActiveShoppingLists(lists);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();

        initViews();
        setupHandler();
        checkIfUserLogin();
        setSignInUpListener();
    }

    @Override
    protected void onResume() {
        checkIfUserLogin();
        super.onResume();
    }

    private void initViews(){
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawer);

        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close);
        mToggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    private void setupHandler() {
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                GenericResponse<UserDTO> response = (GenericResponse<UserDTO>)msg.obj;
                if(response.isSuccessfulOperation()) {
                    changeDrawerContent(true, response.getEntity());
                } else {
                    changeDrawerContent(false, null);
                }
            }
        };
    }

    private void checkIfUserLogin() {
        boolean hasJWT = false;
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFS_AUTH_FILE_NAME, SHARED_PREFS_AUTH_MODE);
        String jwtVal = sharedPreferences.getString(Constants.SHARED_PREFS_JWT_KEY, "");
        if (jwtVal != null && !jwtVal.isEmpty()){
            MainActivity.WorkerThread workerThread = new MainActivity.WorkerThread(handler);
            workerThread.start();
            Message msg = Message.obtain();
            workerThread.handler.sendMessage(msg);
        }
    }

    private void changeDrawerContent(boolean loggedIn, UserDTO userDTO){
        View headerView = mNavigationView.getHeaderView(0);
        TextView emailText = (TextView) headerView.findViewById(R.id.user_email);
        Button button = (Button)headerView.findViewById(R.id.btn_sign_in);
        Menu navMenu = mNavigationView.getMenu();
        if(loggedIn){
            button.setVisibility(View.INVISIBLE);
            emailText.setVisibility(View.VISIBLE);
            emailText.setText(userDTO.getEmail());
            navMenu.findItem(R.id.mnu_logout).setVisible(true);
        }else {
            emailText.setVisibility(View.INVISIBLE);
            navMenu.findItem(R.id.mnu_logout).setVisible(false);
        }
    }

    private void setSignInUpListener() {
        View headerView = mNavigationView.getHeaderView(0);
        Button button = (Button)headerView.findViewById(R.id.btn_sign_in);
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

    public void write_lists(List<ShoppingListDTO> list) {
        String json = new Gson().toJson(list);
        try {
            FileOutputStream fos = openFileOutput(SHOPPING_LIST_FILE, Context.MODE_PRIVATE);
            fos.write(json.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<ShoppingListDTO> read_lists() {
        String text = "";
        List<ShoppingListDTO> shoppingLists = new ArrayList<>();
        try {
            FileInputStream fis = openFileInput(SHOPPING_LIST_FILE);
            int size = fis.available();
            byte[] buffer = new byte[size];
            fis.read(buffer);
            fis.close();
            text = new String(buffer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!text.isEmpty()) {
            shoppingLists = new Gson().fromJson(text, new TypeToken<List<ShoppingListDTO>>() {
            }.getType());
        }
        return shoppingLists;
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
                singOutUser();
            }
        }

        //Close navigation drawer
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void singOutUser(){
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        deleteJWTToken();
        changeDrawerContent(false, null);
        startActivity(intent);
    }

    private void deleteJWTToken() {
        SharedPreferences sp = getSharedPreferences(Constants.SHARED_PREFS_AUTH_FILE_NAME, SHARED_PREFS_AUTH_MODE);
        SharedPreferences.Editor e = sp.edit();
        e.remove(Constants.SHARED_PREFS_JWT_KEY).apply();
        MDJInterceptor.jwt = "";
    }

    private class WorkerThread extends Thread {
        private Handler handler;
        private Handler responseHandler;

        public WorkerThread(Handler handlerUI) {
            responseHandler = handlerUI;
            handler = new Handler() {

                @Override
                public void handleMessage(Message msg) {
                    ServiceUtils.userService.getLoggedInUser().enqueue(new retrofit2.Callback<GenericResponse<UserDTO>>() {

                        @Override
                        public void onResponse(Call<GenericResponse<UserDTO>> call, Response<GenericResponse<UserDTO>> response) {
                            System.out.println("Meesage recieved successfully!");
                            responseHandler.sendMessage(ServiceUtils.getHandlerMessageFromResponse(response));
                        }

                        @Override
                        public void onFailure(Call<GenericResponse<UserDTO>> call, Throwable t) {
                            System.out.println("Error sending registration data!");
                            responseHandler.sendMessage(GenericResponse.getGenericServerErrorResponseMessage());
                        }
                    });
                    super.handleMessage(msg);
                }

            };
        }

        @Override
        public void run() {
            if(Looper.myLooper() == null) {
                Looper.prepare();
            }

            Looper.loop();
        }
    }
}
