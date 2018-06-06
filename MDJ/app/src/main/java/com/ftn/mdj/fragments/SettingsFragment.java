package com.ftn.mdj.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.support.v4.app.Fragment;
import android.widget.Switch;
import android.widget.Toast;

import com.ftn.mdj.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragment {
    private Context mContext;
    private Activity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        // Get the application context
        mContext = this.getActivity();
        mActivity = this.getActivity();

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);

        Boolean notificationValue = sp.getBoolean("switch", false);

        Toast.makeText(mActivity,"notificationValue "+notificationValue, Toast.LENGTH_SHORT).show();

        final SwitchPreference onOffNotifications = (SwitchPreference) findPreference("switch");

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
                return false;
            }
        });
    }
}
