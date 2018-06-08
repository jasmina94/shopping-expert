package com.ftn.mdj.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;
import android.widget.Toast;

import com.ftn.mdj.R;
import com.ftn.mdj.activities.MainActivity;
import com.ftn.mdj.dto.ShoppingListDTO;
import com.ftn.mdj.threads.SaveBlockedUsersThread;
import com.ftn.mdj.threads.ShowNotificationsThread;
import com.ftn.mdj.utils.SharedPreferencesManager;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.in;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragment {
    private Context mContext;
    private Activity mActivity;
    private SharedPreferencesManager sharedPreferenceManager;
    private List<String> blockedUsers = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        // Get the application context
        mContext = this.getActivity();
        mActivity = this.getActivity();

        Boolean showNotifications =  SharedPreferencesManager.getInstance(mContext).getBoolean(SharedPreferencesManager.Key.SHOW_NOTIFICATIONS.name());
        long userId = SharedPreferencesManager.getInstance(mContext).getInt(SharedPreferencesManager.Key.USER_ID.name());

        final SwitchPreference onOffNotifications = (SwitchPreference) findPreference("notifications_switch");
        onOffNotifications.setChecked(showNotifications);
        onOffNotifications.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                if(onOffNotifications.isChecked()){
                    // Checked the switch programmatically
                    onOffNotifications.setChecked(false);
                }else {
                    // Unchecked the switch programmatically
                    onOffNotifications.setChecked(true);
                }

                SharedPreferencesManager.getInstance(mContext).put(SharedPreferencesManager.Key.SHOW_NOTIFICATIONS, onOffNotifications.isChecked());

                if(userId!=0){
                    ShowNotificationsThread showNotificationsThread = new ShowNotificationsThread(userId, onOffNotifications.isChecked());
                    showNotificationsThread.start();
                    Message msg = Message.obtain();
                    showNotificationsThread.getHandler().sendMessage(msg);
                }else{
                    Toast.makeText(mActivity,"Login to use this setting.", Toast.LENGTH_SHORT).show();
                }

                return false;
            }
        });

        /*if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.DesaTheme);
            Toast.makeText(getApplicationContext(),"DesaTheme", Toast.LENGTH_SHORT).show();
        }else{
            setTheme(R.style.AppTheme);
            Toast.makeText(getApplicationContext(),"AppTheme", Toast.LENGTH_SHORT).show();
        }*/

        final SwitchPreference onOffDarkTheme = (SwitchPreference) findPreference("theme_switch");
        onOffDarkTheme.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                if(onOffDarkTheme.isChecked()){
                    // Checked the switch programmatically
                    onOffDarkTheme.setChecked(false);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    restartApp();

                }else {
                    // Unchecked the switch programmatically
                    onOffDarkTheme.setChecked(true);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    restartApp();
                }

                return false;
            }
        });

        final EditTextPreference blockedEditText = (EditTextPreference) findPreference("blocked_text");

        blockedEditText.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                String emailToBlock = blockedEditText.getEditText().getEditableText().toString();
                if(userId!=0){
                    SaveBlockedUsersThread blockedUsersThread = new SaveBlockedUsersThread(userId, emailToBlock, true);
                    blockedUsersThread.start();
                    Message msg = Message.obtain();
                    blockedUsersThread.getHandler().sendMessage(msg);
                }else{
                    Toast.makeText(mActivity,"Login to use this setting.", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        final ListPreference blockedList = (ListPreference) findPreference("blocked_list");
        if(!blockedUsers.isEmpty()){
            CharSequence[] entries = new CharSequence[blockedUsers.size()];
            for(int i=0;i<blockedUsers.size();i++){
                entries[i] = blockedUsers.get(i);
            }
            blockedList.setEntries(entries);
            blockedList.setEntryValues(entries);
        }

        blockedList.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                String emailToUnblock = o.toString();
                if(userId!=0){
                    SaveBlockedUsersThread blockedUsersThread = new SaveBlockedUsersThread(userId, emailToUnblock, false);
                    blockedUsersThread.start();
                    Message msg = Message.obtain();
                    blockedUsersThread.getHandler().sendMessage(msg);
                }else{
                    Toast.makeText(mActivity,"Login to use this setting.", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        /*final ListPreference distanceList = (ListPreference) findPreference("distance_list");

        distanceList.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                String textValue = o.toString();

                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(textValue);

                CharSequence[] entries = listPreference.getEntries();

                if(index >= 0)
                    Toast.makeText(preference.getContext(), entries[index], Toast.LENGTH_LONG);
                if(userId!=0){
                    *//*SaveBlockedUsersThread blockedUsersThread = new SaveBlockedUsersThread(userId, emailToUnblock, false);
                    blockedUsersThread.start();
                    Message msg = Message.obtain();
                    blockedUsersThread.getHandler().sendMessage(msg);*//*
                }else{
                    Toast.makeText(mActivity,"Login to use this setting.", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });*/
    }

    public void restartApp(){
        Intent i = new Intent(mContext,MainActivity.class);
        startActivity(i);
        mActivity.finish();
    }

    public void setBlockedUsers(List<String> blockedUsers){ //throws IOException {
        this.blockedUsers = blockedUsers;
    }
}
