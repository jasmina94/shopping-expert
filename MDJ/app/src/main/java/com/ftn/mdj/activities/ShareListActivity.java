package com.ftn.mdj.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ftn.mdj.R;
import com.ftn.mdj.adapters.ShareListAdapter;
import com.ftn.mdj.threads.GetFriendListThread;
import com.ftn.mdj.threads.ShareListThread;
import com.ftn.mdj.utils.SharedPreferencesManager;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class ShareListActivity extends AppCompatActivity {

    public static ShareListActivity instance;
    private RecyclerView mRecyclerView;
    private TextView mEmptyView;
    private ImageView mEmptyImgView;
    private Toolbar mToolbar;
    private ShareListAdapter shareListAdapter;
    private Long shoppingListId;
    private FloatingActionButton shareList;

    private SharedPreferencesManager sharedPreferenceManager;

    private Map<String, Boolean> friendList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_list);

        instance = this;

        sharedPreferenceManager = SharedPreferencesManager.getInstance();

        shoppingListId = (Long) getIntent().getSerializableExtra("selectedShoppingListId");

        loadFriendList();

        initViews();
    }

    private void loadFriendList() {
        GetFriendListThread friendListThread = new GetFriendListThread(shoppingListId, (long)sharedPreferenceManager.getInt(SharedPreferencesManager.Key.USER_ID.name()));
        friendListThread.start();
        Message msg = Message.obtain();
        friendListThread.getHandler().sendMessage(msg);
    }

    private void initViews() {
        mRecyclerView = findViewById(R.id.share_list_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(ShareListActivity.this));

        mEmptyView = findViewById(R.id.no_friends_view);
        mEmptyImgView = findViewById(R.id.no_friends_view_img);
        shareList = findViewById(R.id.btn_share_list_popup);

        mToolbar = findViewById(R.id.toolbar_trash);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id==android.R.id.home){
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void setFriendList(Map<String, Boolean> friendList) {
        this.friendList = friendList;
        shareListAdapter = new ShareListAdapter(friendList, ShareListActivity.this);
        mRecyclerView.setAdapter(shareListAdapter);
        if(friendList.size()!=0){
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.INVISIBLE);
            mEmptyImgView.setVisibility(View.INVISIBLE);
        }else {
            mRecyclerView.setVisibility(View.INVISIBLE);
            mEmptyView.setVisibility(View.VISIBLE);
            mEmptyImgView.setVisibility(View.VISIBLE);
        }

        shareList.setOnClickListener(view -> {
            showShareDialog(view.getContext());
        });
    }

    private void showShareDialog(Context context) {
        AlertDialog.Builder shareBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflaterSecret = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View dialogSecretView = inflaterSecret.inflate( R.layout.dialog_one_field, null );
        Button dismissSecret = dialogSecretView.findViewById( R.id.dialog_dismiss );
        TextView dialogSecretTitle = dialogSecretView.findViewById(R.id.dialog_title);
        dialogSecretTitle.setText("Email of user you want to share list");
        Button submitPass = dialogSecretView.findViewById( R.id.dialog_submit );
        submitPass.setText("Share");
        EditText email = dialogSecretView.findViewById(R.id.dialog_text);
        email.setHint("Email...");

        shareBuilder.setView(dialogSecretView);

        AlertDialog shareAlertDialog = shareBuilder.create();
        shareAlertDialog.setCanceledOnTouchOutside(false);

        submitPass.setOnClickListener(view1 -> {
            if(email.getText().toString().isEmpty()) {
                Toast.makeText(context, "Email can not be empty!", Toast.LENGTH_SHORT).show();
            } else {
                ShareListThread shareListThread = new ShareListThread(shoppingListId, email.getText().toString());
                shareListThread.start();
                Message msg = Message.obtain();
                shareListThread.getHandler().sendMessage(msg);
                Toast.makeText(context, "Email sharing!", Toast.LENGTH_SHORT).show();
                shareAlertDialog.cancel();
            }
        });
        dismissSecret.setOnClickListener(view12 -> {
            shareAlertDialog.cancel();
        });

        shareAlertDialog.show();
    }

    public void sendNotifications(List<String> deviceIds) {
        System.out.print("Usao da salje notifikaciju =================================================================================");

        deviceIds.forEach(deviceID -> {
            try {

                URL url = new URL("https://fcm.googleapis.com/fcm/send");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setUseCaches(false);
                conn.setDoInput(true);
                conn.setDoOutput(true);

                conn.setRequestMethod("POST");
                conn.setRequestProperty("Authorization", "AIzaSyAZcVSksPrv_hL5JD9Dazj-2T2UA3a_6GU");
                conn.setRequestProperty("Content-Type", "application/json");

                JSONObject json = new JSONObject();

                json.put("to", deviceID);
                System.out.print(deviceID);

                System.out.print("Usao da salje notifikaciju");


                JSONObject info = new JSONObject();
                info.put("title", "MDJ : Shopping Expert");   // Notification title
                info.put("body", sharedPreferenceManager.getString(SharedPreferencesManager.Key.USER_EMAIL.name()) + " just shared list with you."); // Notification body

                json.put("notification", info);
//
//                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
//                wr.write(json.toString());
//                wr.flush();
//                conn.getInputStream();

            }
            catch (Exception exp)
            {
                Log.d("Error",""+exp);
            }

        });
        finish();
        startActivity(getIntent());
    }
}
