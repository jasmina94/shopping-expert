package com.ftn.mdj.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.ftn.mdj.R;
import com.ftn.mdj.fragments.SettingsFragment;
import com.ftn.mdj.threads.GetBlockedUsersThread;
import com.ftn.mdj.utils.SharedPreferencesManager;

public class SettingsActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    public static SettingsActivity instance = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        instance=this;

        mToolbar = (Toolbar) findViewById(R.id.toolbar_settings);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        long userId = SharedPreferencesManager.getInstance(SettingsActivity.this).getInt(SharedPreferencesManager.Key.USER_ID.name());

        if(userId!=0){
            GetBlockedUsersThread blockedUsersThread = new GetBlockedUsersThread(userId);
            blockedUsersThread.start();
            Message msg = Message.obtain();
            blockedUsersThread.getHandler().sendMessage(msg);
        }

        SettingsFragment settingsFragment = new SettingsFragment();
        FragmentManager mFragmentManager = getFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager
                .beginTransaction();

        if(savedInstanceState==null){
            mFragmentTransaction.add(R.id.settings_layout,settingsFragment, "settings_fragment");
            mFragmentTransaction.commit();
        }else{
            settingsFragment=(SettingsFragment)getFragmentManager().findFragmentByTag("settings_fragment");
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id==android.R.id.home){
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
