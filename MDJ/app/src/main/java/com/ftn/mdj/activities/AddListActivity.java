package com.ftn.mdj.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.ftn.mdj.R;
import com.ftn.mdj.dto.ShoppingListDTO;
import com.ftn.mdj.services.ServiceUtils;
import com.ftn.mdj.utils.DummyCollection;
import com.ftn.mdj.utils.GenericResponse;
import com.ftn.mdj.utils.SharedPreferencesManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class AddListActivity extends AppCompatActivity {

    private Button btn_create_list;
    private Button btn_dismiss;

    private SharedPreferencesManager sharedPreferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_list);

        sharedPreferenceManager = SharedPreferencesManager.getInstance();

        btn_create_list = (Button) findViewById(R.id.new_list_btn);
        btn_dismiss = findViewById(R.id.btn_dismiss_create);
        btn_dismiss.setOnClickListener(view -> finish());

        setupSubmit();
    }

    private void setupSubmit() {
        btn_create_list.setOnClickListener(view -> {
                long loggedUserId = sharedPreferenceManager.getInt(SharedPreferencesManager.Key.USER_ID.name());
                if(loggedUserId != 0) {
                    WorkerThread workerThread = new WorkerThread((long) 1);
                    workerThread.start();
                    Message msg = Message.obtain();
                    workerThread.handler.sendMessage(msg);
                }else {
                    String listName = ((TextView) findViewById(R.id.new_list_name)).getText().toString().trim();
                    ShoppingListDTO newList = new ShoppingListDTO(listName);
                    List<ShoppingListDTO> lists = DummyCollection.readLists(this.getApplicationContext());
                    lists.add(newList);
                    DummyCollection.writeLists(lists, this.getApplicationContext());
                    Intent intent = new Intent(AddListActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            });
    }

    private class WorkerThread extends Thread{
        private Handler handler;

        public WorkerThread(Long userId){
            handler = new Handler(){

                @Override
                public void handleMessage(Message msg) {
                    String listName = ((TextView) findViewById(R.id.new_list_name)).getText().toString().trim();
                    ServiceUtils.listService.create(listName, userId).enqueue(new retrofit2.Callback<GenericResponse>(){

                        @Override
                        public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                            Intent intent = new Intent(AddListActivity.this, MainActivity.class);
                            startActivity(intent);
                            System.out.println("Meesage recieved successfully!");
                        }

                        @Override
                        public void onFailure(Call<GenericResponse> call, Throwable t) {
                            System.out.println("Error sending registration data!");
                        }
                    });
                    super.handleMessage(msg);
                }

            };
        }
    }
}
