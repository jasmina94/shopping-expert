package com.ftn.mdj.reminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.widget.Toast;

import com.ftn.mdj.activities.MainActivity;
import com.ftn.mdj.threads.RemoveReminderThread;
import com.ftn.mdj.utils.MyNotification;

public class Reminder extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        MyNotification notification = new MyNotification();
        String listName = intent.getStringExtra("nameList");
        Long id = intent.getLongExtra("idList", 1);
        String body = "You have set reminder on list(id:" + id + ") " + listName + ".";
        notification.showNotification(context, body);

        RemoveReminderThread removeReminderThread= new RemoveReminderThread(id);
        removeReminderThread.start();
        Message msg = Message.obtain();
        removeReminderThread.getHandler().sendMessage(msg);

        AlarmManager am = (AlarmManager) MainActivity.instance.getSystemService(Context.ALARM_SERVICE);

        Intent i = new Intent(MainActivity.instance, Reminder.class);
        i.putExtra("idList", id);
        i.putExtra("nameList", listName);
        PendingIntent pi = PendingIntent.getBroadcast(MainActivity.instance, 0, i, 0);

        Toast.makeText(MainActivity.instance, "Reminder is fired", Toast.LENGTH_SHORT).show();
        am.cancel(pi);
    }
}
