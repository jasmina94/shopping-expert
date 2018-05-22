package com.ftn.mdj.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ftn.mdj.R;
import com.ftn.mdj.activities.ForgotPasswordActivity;
import com.ftn.mdj.activities.MainActivity;
import com.ftn.mdj.dto.LoginDTO;
import com.ftn.mdj.services.MDJInterceptor;
import com.ftn.mdj.utils.Constants;
import com.ftn.mdj.utils.GenericResponse;
import com.ftn.mdj.services.ServiceUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Jasmina on 11/05/2018.
 */

public class LoginFragment extends Fragment {

    public static final int SHARED_PREFS_AUTH_MODE = MODE_PRIVATE;

    private Handler handler;

    private AppCompatEditText mEmailWrapper;
    private AppCompatEditText mPasswordWrapper;
    private TextView mForgotPasswordTextView;

    private AppCompatButton mLoginButton;

    private View rootView;

    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_login, container, false);
        initView();
        setupHandler();
        setupSubmit();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(hasJWTInSharedPreferences()) {
            System.out.println("User is already logged in");
        }
    }

    private void initView(){
        mEmailWrapper = (AppCompatEditText)rootView.findViewById(R.id.log_input_email);
        mPasswordWrapper = (AppCompatEditText)rootView.findViewById(R.id.log_input_password);
        mLoginButton = (AppCompatButton)rootView.findViewById(R.id.btn_login);
        mForgotPasswordTextView = (TextView)rootView.findViewById(R.id.link_forgot_pass);

        mForgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = rootView.getContext();
                Intent intent = new Intent(context, ForgotPasswordActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });
    }

    private void setupSubmit() {
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginDTO loginDTO;
                String email = mEmailWrapper.getEditableText().toString().trim();
                String password = mPasswordWrapper.getEditableText().toString().trim();
                if(email.isEmpty()){
                    mEmailWrapper.setError(getString(R.string.err_required_email));
                    mEmailWrapper.requestFocus();
                }else if(password.isEmpty()){
                    mPasswordWrapper.setError(getString(R.string.err_required_password));
                    mPasswordWrapper.requestFocus();
                }else if(!validateEmail(email)){
                    mEmailWrapper.setError(getString(R.string.err_valid_email));
                    mEmailWrapper.requestFocus();
                }else {
                    loginDTO = new LoginDTO(email, password);
                    LoginFragment.WorkerThread workerThread = new LoginFragment.WorkerThread(handler);
                    workerThread.start();
                    Message msg = Message.obtain();
                    msg.obj = loginDTO;
                    workerThread.handler.sendMessage(msg);
                }
            }
        });
    }

    private void setupHandler(){
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                GenericResponse<String> response = (GenericResponse<String>)msg.obj;
                String message;
                if(response.isSuccessfulOperation()) {
                    message = getString(R.string.success_login);
                    showToastMessage(message);
                    putJWTIntoSharedPreferences(response.getEntity());
                    goToMainActivity();
                } else {
                    message = response.getErrorMessage();
                    showToastMessage(message);
                }
            }
        };
    }

    private void putJWTIntoSharedPreferences(String token) {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(Constants.SHARED_PREFS_AUTH_FILE_NAME, SHARED_PREFS_AUTH_MODE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.SHARED_PREFS_JWT_KEY, token).apply();
        MDJInterceptor.jwt = token;
    }

    private boolean hasJWTInSharedPreferences(){
        boolean hasJWT = false;
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(Constants.SHARED_PREFS_AUTH_FILE_NAME, SHARED_PREFS_AUTH_MODE);
        String jwtVal = sharedPreferences.getString(Constants.SHARED_PREFS_JWT_KEY, "");
        if (jwtVal != null && !jwtVal.isEmpty())
            hasJWT = true;

        return hasJWT;
    }


    private class WorkerThread extends Thread{
        private Handler handler;
        private Handler responseHandler;

        public WorkerThread(Handler handlerUI){
            responseHandler = handlerUI;
            handler = new Handler(){

                @Override
                public void handleMessage(Message msg) {
                    LoginDTO loginDTO = (LoginDTO) msg.obj;
                    ServiceUtils.userService.login(loginDTO).enqueue(new retrofit2.Callback<GenericResponse<String>>(){

                        @Override
                        public void onResponse(Call<GenericResponse<String>> call, Response<GenericResponse<String>> response) {
                            System.out.println("Meesage recieved successfully!");
                            responseHandler.sendMessage(ServiceUtils.getHandlerMessageFromResponse(response));
                        }

                        @Override
                        public void onFailure(Call<GenericResponse<String>> call, Throwable t) {
                            System.out.println("Error sending login data!");
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

    private void showToastMessage(String message){
        Toast t = Toast.makeText(getContext(), message, Toast.LENGTH_LONG);
        t.show();
    }

    private void goToMainActivity(){
        Context context = rootView.getContext();
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    private boolean validateEmail(String email){
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
