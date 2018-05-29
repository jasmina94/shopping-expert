package com.ftn.mdj.activities;

import android.content.Context;
import android.content.Intent;
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

import com.facebook.login.LoginManager;
import com.ftn.mdj.R;
import com.ftn.mdj.dto.ShoppingListDTO;
import com.ftn.mdj.dto.UserDTO;
import com.ftn.mdj.fragments.MainFragment;
import com.ftn.mdj.services.MDJInterceptor;
import com.ftn.mdj.threads.LoggedUserThread;
import com.ftn.mdj.threads.GetActiveListsThread;
import com.ftn.mdj.utils.DummyCollection;
import com.ftn.mdj.utils.GenericResponse;
import com.ftn.mdj.utils.SharedPreferencesManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private final static String SIGN_IN_FACEBOOK = "fb";
    private final static String SIGN_IN_GOOGLE = "google";
    private final static String SIGN_IN_INNER = "mdj";


    private NavigationView mNavigationView;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    private Handler handler;

    private FirebaseAuth mAuth;
    private boolean userIsLoggedIn = false;
    private SharedPreferencesManager sharedPreferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        sharedPreferenceManager = SharedPreferencesManager.getInstance(this.getApplicationContext()); //Initialize ShPref manager
        initViews();
        setupHandler();
    }

    @Override
    protected void onStart() {
        userIsLoggedIn = checkIfUserLogin();
        super.onStart();
    }

    @Override
    protected void onResume() {
        //Ovde se dobavljaju liste iz baze ako je korisnik ulogovan -> dobavljas iz baze
        if(userIsLoggedIn){
            long id = sharedPreferenceManager.getInt(SharedPreferencesManager.Key.USER_ID.name());
            GetActiveListsThread getActiveListsThread = new GetActiveListsThread(false, id, this);
            getActiveListsThread.start();
            Message msg = Message.obtain();
            getActiveListsThread.getHandler().sendMessage(msg);
        }else {
            //Ovde se citaju liste iz fajla ako ih ima ako ih nema prikazi prazno
            List<ShoppingListDTO> lists = DummyCollection.readLists(this.getApplicationContext());
            MainFragment fragment = new MainFragment();
            try {
                fragment.setActiveShoppingLists(lists);
            } catch (IOException e) {
                e.printStackTrace();
            }
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        }
        super.onResume();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnu_trash: {
                Toast.makeText(MainActivity.this, "Show trash fragment!", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.mnu_help: {
                Toast.makeText(MainActivity.this, "Show help fragment!", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.mnu_settings: {
                Toast.makeText(MainActivity.this, "Show settings fragment!", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.mnu_logout: {
                singOutUser();
            }
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawer);

        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close);
        mToggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        View headerView = mNavigationView.getHeaderView(0);
        Button signInUpBtn = (Button) headerView.findViewById(R.id.btn_sign_in);
        signInUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, LogRegActivity.class);
                startActivity(intent);
            }
        });
    }

    //Ovde dobavljam logovanog korisnika koji je nas MDJ
    private void setupHandler() {
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                GenericResponse<UserDTO> response = (GenericResponse<UserDTO>) msg.obj;
                if (response.isSuccessfulOperation()) {
                    sharedPreferenceManager.put(SharedPreferencesManager.Key.USER_ID, response.getEntity().getId().intValue());
                    updateUI(true, response.getEntity().getEmail());
                } else {
                    updateUI(false, null);
                }
            }
        };
    }

    private boolean checkIfUserLogin() {
        boolean loggedIn = true;
        String type = sharedPreferenceManager.getString(SharedPreferencesManager.Key.SIGN_IN_TYPE.name());
        String jwtVal = sharedPreferenceManager.getString(SharedPreferencesManager.Key.JWT_KEY.name());
        if(type != null && !type.equals(SIGN_IN_INNER)){
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if(currentUser != null){
                updateUI(true, currentUser.getEmail());
            }else {
                updateUI(false, null);
                loggedIn = false;
            }
        }else {
            if (jwtVal != null && !jwtVal.isEmpty()) {
                LoggedUserThread loggedUserThread = new LoggedUserThread(handler);
                loggedUserThread.start();
                Message msg = Message.obtain();
                loggedUserThread.getHandler().sendMessage(msg);
            } else {
                updateUI(false, null);
                loggedIn = false;
            }
        }
        return loggedIn;
    }

    private void updateUI(boolean loggedIn, String userEmail){
        View headerView = mNavigationView.getHeaderView(0);
        TextView emailText = (TextView) headerView.findViewById(R.id.user_email);
        Button button = (Button) headerView.findViewById(R.id.btn_sign_in);
        Menu navMenu = mNavigationView.getMenu();
        if (loggedIn) {
            button.setVisibility(View.INVISIBLE);
            emailText.setVisibility(View.VISIBLE);
            emailText.setText(userEmail);
            navMenu.findItem(R.id.mnu_logout).setVisible(true);
        } else {
            emailText.setVisibility(View.INVISIBLE);
            navMenu.findItem(R.id.mnu_logout).setVisible(false);
        }
    }

    private void singOutUser() {
        String type = sharedPreferenceManager.getString(SharedPreferencesManager.Key.SIGN_IN_TYPE.name());
        if(type.equals(SIGN_IN_INNER)) {
            sharedPreferenceManager.put(SharedPreferencesManager.Key.JWT_KEY, "");
        }else if(type.equals(SIGN_IN_FACEBOOK)){
            mAuth.signOut();
            LoginManager.getInstance().logOut();
        }else if(type.equals(SIGN_IN_GOOGLE)){
            mAuth.signOut();
        }
        sharedPreferenceManager.put(SharedPreferencesManager.Key.SIGN_IN_TYPE, null);
        sharedPreferenceManager.put(SharedPreferencesManager.Key.USER_ID, null);
        MDJInterceptor.jwt = "";
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
