package com.ftn.mdj.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.support.v4.app.Fragment;
import android.widget.Switch;
import android.widget.Toast;

import com.ftn.mdj.R;
import com.ftn.mdj.activities.MainActivity;
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

        System.out.println("op1   "+SharedPreferencesManager.Key.SHOW_NOTIFICATIONS.name());

        Boolean showNotifications =  SharedPreferencesManager.getInstance(mContext).getBoolean(SharedPreferencesManager.Key.SHOW_NOTIFICATIONS.name());

        long userId = SharedPreferencesManager.getInstance(mContext).getInt(SharedPreferencesManager.Key.USER_ID.name());

        System.out.println("op2   "+ showNotifications);

        final SwitchPreference onOffNotifications = (SwitchPreference) findPreference("switch");

        onOffNotifications.setChecked(showNotifications);

        // SwitchPreference preference change listener
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
    }
}