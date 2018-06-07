package com.ftn.mdj.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.ftn.mdj.R;
import com.ftn.mdj.fragments.SettingsFragment;

public class SettingsActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_settings);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

       /* mFragmentTransaction.replace(android.R.id.content, settingsFragment);
        mFragmentTransaction.commit();*/
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
