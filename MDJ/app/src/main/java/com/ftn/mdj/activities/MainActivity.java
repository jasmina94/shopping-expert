package com.ftn.mdj.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.ftn.mdj.dto.ShoppingListDTO;
import com.ftn.mdj.fragments.MainFragment;
import com.ftn.mdj.utils.DummyCollection;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String SHOPPING_LIST_FILE = "shopping_lists.txt";

    private NavigationView mNavigationView;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    private  boolean userLogedIn = false;  //for test

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DummyCollection dummyCollection = new DummyCollection();
        write_lists(dummyCollection.getDummies());
        List<ShoppingListDTO> lists = read_lists();
        //List<ShoppingListDTO> lists = new ArrayList<>();

        MainFragment fragment = new MainFragment();
        fragment.setActiveShoppingLists(lists);
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();

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
}
