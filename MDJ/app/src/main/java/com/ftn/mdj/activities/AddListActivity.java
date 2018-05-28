package com.ftn.mdj.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.ftn.mdj.R;
import com.ftn.mdj.services.ServiceUtils;
import com.ftn.mdj.utils.GenericResponse;

import retrofit2.Call;
import retrofit2.Response;

public class AddListActivity extends AppCompatActivity {

    private Button btn_create_list;
    private Button btn_dismiss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_list);

        btn_create_list = (Button) findViewById(R.id.new_list_btn);
//        btn_create_list.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Context context = view.getContext();
//                Intent intent = new Intent(context, MainActivity.class);
//
//                String name = ((TextView) findViewById(R.id.new_list_name)).getText().toString().trim();
//                ShoppingListShowDTO shoppingListDTO = new ShoppingListShowDTO(name);
//
//                intent.putExtra("newList", shoppingListDTO);
//                context.startActivity(intent);
//            }
//        });
        setupSubmit();
        btn_dismiss = findViewById(R.id.btn_dismiss_create);
        btn_dismiss.setOnClickListener(view -> finish());
    }

    private void setupSubmit() {
        btn_create_list.setOnClickListener(view -> {

                WorkerThread workerThread = new WorkerThread((long) 1);
                workerThread.start();
                Message msg = Message.obtain();
            workerThread.handler.sendMessage(msg);
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
