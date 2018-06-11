package com.ftn.mdj.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.widget.Button;
import android.widget.TextView;

import com.ftn.mdj.R;
import com.ftn.mdj.dto.ShoppingListDTO;
import com.ftn.mdj.threads.CreateListThread;
import com.ftn.mdj.utils.DummyCollection;
import com.ftn.mdj.utils.SharedPreferencesManager;

import java.util.List;

public class AddListActivity extends AppCompatActivity {

    private Button btn_create_list;
    private Button btn_dismiss;

    private SharedPreferencesManager sharedPreferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.DarkTheme);
        }else{
            setTheme(R.style.LightTheme);
        }
        setContentView(R.layout.activity_add_list);

        sharedPreferenceManager = SharedPreferencesManager.getInstance();

        btn_create_list = findViewById(R.id.new_list_btn);
        btn_dismiss = findViewById(R.id.btn_dismiss_create);
        btn_dismiss.setOnClickListener(view -> finish());

        setupSubmit();
    }

    private void setupSubmit() {
        btn_create_list.setOnClickListener(view -> {
                long loggedUserId = sharedPreferenceManager.getInt(SharedPreferencesManager.Key.USER_ID.name());
                if(loggedUserId != 0) {
                    String listName = ((TextView) findViewById(R.id.new_list_name)).getText().toString().trim();
                    CreateListThread workerThread = new CreateListThread(loggedUserId, listName);
                    workerThread.start();
                    Message msg = Message.obtain();
                    workerThread.getHandler().sendMessage(msg);
                }else {
                    Long id;
                    List<ShoppingListDTO> lists = DummyCollection.readLists(this.getApplicationContext());
                    if(lists.isEmpty()) {
                        id = Long.valueOf(1);
                    } else {
                        id = lists.get(lists.size() - 1).getId() + 1;
                    }
                    String listName = ((TextView) findViewById(R.id.new_list_name)).getText().toString().trim();
                    ShoppingListDTO newList = new ShoppingListDTO(listName);
                    newList.setId(id);
                    lists.add(newList);
                    DummyCollection.writeLists(lists, this.getApplicationContext());
                }
            Intent intent = new Intent(AddListActivity.this, MainActivity.class);
            startActivity(intent);
            });
    }
}
