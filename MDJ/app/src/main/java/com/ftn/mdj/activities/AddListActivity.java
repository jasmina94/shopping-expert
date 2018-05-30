package com.ftn.mdj.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.ftn.mdj.R;
import com.ftn.mdj.dto.ShoppingListDTO;
import com.ftn.mdj.threads.CreateListThread;
import com.ftn.mdj.utils.DummyCollection;
import com.ftn.mdj.utils.GenericResponse;
import com.ftn.mdj.utils.SharedPreferencesManager;
import com.ftn.mdj.utils.UtilHelper;

import java.util.List;

public class AddListActivity extends AppCompatActivity {

    private Button btn_create_list;
    private Button btn_dismiss;

    private SharedPreferencesManager sharedPreferenceManager;

    private Handler createListHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_list);

        sharedPreferenceManager = SharedPreferencesManager.getInstance();

        btn_create_list = findViewById(R.id.new_list_btn);
        btn_dismiss = findViewById(R.id.btn_dismiss_create);
        btn_dismiss.setOnClickListener(view -> finish());

        setupSubmit();
        setCreateListHandler();
    }

    private void setCreateListHandler(){
        createListHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                GenericResponse<ShoppingListDTO> response = (GenericResponse<ShoppingListDTO>) msg.obj;
                if (response.isSuccessfulOperation()) {
                    UtilHelper.showToastMessage(getApplicationContext(), "Successfully created list!", UtilHelper.ToastLength.SHORT);
                } else {
                    UtilHelper.showToastMessage(getApplicationContext(), "Error while creating list!", UtilHelper.ToastLength.SHORT);
                }
            }
        };
    }

    private void setupSubmit() {
        btn_create_list.setOnClickListener(view -> {
                long loggedUserId = sharedPreferenceManager.getInt(SharedPreferencesManager.Key.USER_ID.name());
                if(loggedUserId != 0) {
                    String listName = ((TextView) findViewById(R.id.new_list_name)).getText().toString().trim();
                    CreateListThread workerThread = new CreateListThread(createListHandler, loggedUserId, listName);
                    workerThread.start();
                    Message msg = Message.obtain();
                    workerThread.getHandler().sendMessage(msg);
                }else {
                    String listName = ((TextView) findViewById(R.id.new_list_name)).getText().toString().trim();
                    ShoppingListDTO newList = new ShoppingListDTO(listName);
                    List<ShoppingListDTO> lists = DummyCollection.readLists(this.getApplicationContext());
                    lists.add(newList);
                    DummyCollection.writeLists(lists, this.getApplicationContext());
                }
            Intent intent = new Intent(AddListActivity.this, MainActivity.class);
            startActivity(intent);
            });
    }
}
