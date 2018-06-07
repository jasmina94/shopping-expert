package com.ftn.mdj.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;
import android.widget.Switch;
import android.widget.Toast;

import com.ftn.mdj.R;
import com.ftn.mdj.activities.MainActivity;
import com.ftn.mdj.activities.SettingsActivity;
import com.ftn.mdj.threads.SettingsThread;
import com.ftn.mdj.threads.UploadListThread;
import com.ftn.mdj.utils.SharedPreferencesManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragment {
    private Context mContext;
    private Activity mActivity;
    private SharedPreferencesManager sharedPreferenceManager;

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
                    Toast.makeText(mActivity,"Unchecked", Toast.LENGTH_SHORT).show();

                    // Checked the switch programmatically
                    onOffNotifications.setChecked(false);
                }else {
                    Toast.makeText(mActivity,"Checked",Toast.LENGTH_SHORT).show();

                    // Unchecked the switch programmatically
                    onOffNotifications.setChecked(true);
                }

                SharedPreferencesManager.getInstance(mContext).put(SharedPreferencesManager.Key.SHOW_NOTIFICATIONS, onOffNotifications.isChecked());

                if(userId!=0){
                    SettingsThread settingsThread = new SettingsThread(userId, onOffNotifications.isChecked());
                    settingsThread.start();
                    Message msg = Message.obtain();
                    settingsThread.getHandler().sendMessage(msg);
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
                    Toast.makeText(mActivity,"Unchecked", Toast.LENGTH_SHORT).show();

                    // Checked the switch programmatically
                    onOffDarkTheme.setChecked(false);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    restartApp();

                }else {
                    Toast.makeText(mActivity,"Checked",Toast.LENGTH_SHORT).show();

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
                System.out.print("blockedEditText " + emailToBlock);
                Toast.makeText(mActivity,"edit "+emailToBlock , Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        final ListPreference blockedList = (ListPreference) findPreference("blocked_list");
        CharSequence[] entries = new CharSequence[]{"One", "Two", "Three"};
        blockedList.setEntries(entries);
        blockedList.setEntryValues(entries);

        blockedList.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                String emailToUnblock = o.toString();
                System.out.print("desaaa " + emailToUnblock);
                Toast.makeText(mActivity,"edit "+emailToUnblock , Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    public void restartApp(){
        Intent i = new Intent(mContext,MainActivity.class);
        startActivity(i);
        mActivity.finish();
    }
}
