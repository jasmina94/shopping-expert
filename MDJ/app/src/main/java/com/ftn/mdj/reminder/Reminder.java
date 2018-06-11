package com.ftn.mdj.reminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.ftn.mdj.activities.MainActivity;

public class Reminder extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(MainActivity.instance, "Alarm is fired", Toast.LENGTH_SHORT).show();

    }
}
