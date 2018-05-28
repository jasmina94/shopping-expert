package com.ftn.mdj.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.Toast;

import com.ftn.mdj.R;
import com.ftn.mdj.dto.RegistrationDTO;
import com.ftn.mdj.dto.UserDTO;
import com.ftn.mdj.fragments.RegisterFragment;
import com.ftn.mdj.services.ServiceUtils;
import com.ftn.mdj.utils.GenericResponse;

import retrofit2.Call;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {

    private AppCompatButton mRecoverButton;
    private TextInputEditText mEmailTxt;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        initView();
        setupHandler();
        setupSubmit();
    }

    private void initView(){
        mRecoverButton = (AppCompatButton)findViewById(R.id.recoverPass_submit_btn);
        mEmailTxt = (TextInputEditText)findViewById(R.id.recoverPass_email);
    }

    private void setupHandler() {
        handler = new Handler(Looper.getMainLooper()) {
            @SuppressWarnings("unchecked")
            @Override
            public void handleMessage(Message msg) {
                GenericResponse<Boolean> response = (GenericResponse<Boolean>)msg.obj;
                String message;
                if(response.isSuccessfulOperation()) {
                    message = getString(R.string.recovery_message_toast);
                    showToastMessage(message);
                    goToSignIn();
                } else {
                    message = response.getErrorMessage();
                    showToastMessage(message);
                }
            }
        };
    }

    private void goToSignIn() {
        Context context = this;
        Intent intent = new Intent(context, LogRegActivity.class);
        startActivity(intent);
    }

    private void showToastMessage(String message) {
        Toast t = Toast.makeText(this, message, Toast.LENGTH_LONG);
        t.show();
    }

    private void setupSubmit() {
        mRecoverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmailTxt.getEditableText().toString().trim();
                if(email.isEmpty()){
                    mEmailTxt.setError(getString(R.string.err_required_email));
                    mEmailTxt.requestFocus();
                }else {
                    WorkerThread workerThread = new WorkerThread(handler);
                    workerThread.start();
                    Message msg = Message.obtain();
                    Bundle b = new Bundle();
                    b.putString("email", email);
                    msg.setData(b);
                    workerThread.handler.sendMessage(msg);
                }
            }
        });
    }


    private class WorkerThread extends Thread{
        private Handler handler;
        private Handler responseHandler;

        public WorkerThread(Handler handlerUI){
            responseHandler = handlerUI;
            handler = new Handler(){

                @Override
                public void handleMessage(Message msg) {
                    String email =  msg.getData().getString("email");
                    ServiceUtils.userService.forgetPassword(email).enqueue(new retrofit2.Callback<GenericResponse<Boolean>>(){

                        @Override
                        public void onResponse(Call<GenericResponse<Boolean>> call, Response<GenericResponse<Boolean>> response) {
                            System.out.println("Meesage recieved successfully!");
                            responseHandler.sendMessage(ServiceUtils.getHandlerMessageFromResponse(response));
                        }

                        @Override
                        public void onFailure(Call<GenericResponse<Boolean>> call, Throwable t) {
                            System.out.println("Error recovery password data!");
                            responseHandler.sendMessage(GenericResponse.getGenericServerErrorResponseMessage());
                        }
                    });
                    super.handleMessage(msg);
                }

            };
        }

        @Override
        public void run() {
            if(Looper.myLooper() == null) {
                Looper.prepare();
            }

            Looper.loop();
        }
    }
}
