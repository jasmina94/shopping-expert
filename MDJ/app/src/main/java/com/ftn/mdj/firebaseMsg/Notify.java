package com.ftn.mdj.firebaseMsg;

import android.os.AsyncTask;
import android.util.Log;

import com.ftn.mdj.activities.ShareListActivity;
import com.google.android.gms.common.GoogleApiAvailability;

import org.json.JSONObject;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Notify extends AsyncTask<Void,Void,Void>
{
    private String reciver;
    private String notificationBody;
    private String targetActivity;

    public Notify(String reciver, String notificationBody, String targetActivity) {
        System.out.println("Notify ===============================================================");

        this.reciver = reciver;
        this.notificationBody = notificationBody;
        this.targetActivity = targetActivity;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
            int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(ShareListActivity.instance);
            System.out.println(resultCode + "===============================================================");

            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            conn.setRequestMethod("POST");
            System.out.println(conn.getRequestMethod() + "===============================================================");

            conn.setRequestProperty("Authorization", "key=AIzaSyAZcVSksPrv_hL5JD9Dazj-2T2UA3a_6GU");
            conn.setRequestProperty("Content-Type", "application/json");

            JSONObject json = new JSONObject();

            json.put("to", reciver);

            System.out.println("Usao da salje notifikaciju ===============================================================");

            JSONObject info = new JSONObject();
            info.put("title", "MDJ : Shopping Expert");
            info.put("body", notificationBody);
            json.put("notification", info);
            json.put("targetActivity", targetActivity);

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(json.toString());
            wr.flush();
            conn.getInputStream();

        }
        catch (Exception e)
        {
            Log.d("Error",""+e);
        }
        return null;
    }
}