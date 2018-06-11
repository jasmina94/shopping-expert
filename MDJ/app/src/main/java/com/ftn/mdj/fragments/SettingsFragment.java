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
import com.ftn.mdj.activities.SettingsActivity;
import com.ftn.mdj.dto.ShoppingListDTO;
import com.ftn.mdj.threads.SaveBlockedUsersThread;
import com.ftn.mdj.threads.SaveDistanceThread;
import com.ftn.mdj.threads.ShowNotificationsThread;
import com.ftn.mdj.utils.SharedPreferencesManager;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;
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
        final SwitchPreference onOffDarkTheme = (SwitchPreference) findPreference("theme_switch");


        /*if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES){
            mActivity.setTheme(R.style.DarkTheme);
            Toast.makeText(getApplicationContext(),"DarkTheme", Toast.LENGTH_SHORT).show();
        }else{
            mActivity.setTheme(R.style.LightTheme);
            Toast.makeText(getApplicationContext(),"LightTheme", Toast.LENGTH_SHORT).show();
        }*/

        if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES){
            onOffDarkTheme.setChecked(true);
        }

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

        int distance =  SharedPreferencesManager.getInstance(mContext).getInt(SharedPreferencesManager.Key.DISTANCE.name());
        final ListPreference distanceList = (ListPreference) findPreference("distance_list");

        distanceList.setValue(""+distance);
        distanceList.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                String textValue = o.toString();

                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(textValue);

                CharSequence[] entries = listPreference.getEntries();

                distanceList.setValue(textValue);

                if(index >= 0)
                    Toast.makeText(mActivity, entries[index], Toast.LENGTH_LONG).show();
                if(userId!=0){
                    SaveDistanceThread saveDistanceThread = new SaveDistanceThread(userId, Integer.parseInt(textValue));
                    saveDistanceThread.start();
                    Message msg = Message.obtain();
                    saveDistanceThread.getHandler().sendMessage(msg);
                }else{
                    Toast.makeText(mActivity,"Login to use this setting.", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
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
