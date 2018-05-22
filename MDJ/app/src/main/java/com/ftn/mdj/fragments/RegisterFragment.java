package com.ftn.mdj.fragments;

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
import android.widget.Toast;

import com.ftn.mdj.R;
import com.ftn.mdj.activities.LogRegActivity;
import com.ftn.mdj.dto.RegistrationDTO;
import com.ftn.mdj.dto.UserDTO;
import com.ftn.mdj.utils.GenericResponse;
import com.ftn.mdj.services.ServiceUtils;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Jasmina on 11/05/2018.
 */

public class RegisterFragment extends Fragment {

    private Handler handler;

    private AppCompatEditText mFirstnameWrapper;
    private AppCompatEditText mLastnameWrapper;
    private AppCompatEditText mEmailWrapper;
    private AppCompatEditText mPasswordWrapper;

    private AppCompatButton mRegistrationButton;

    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_register, container, false);
        initViews();
        setupHandler();
        setupSubmit();
        return rootView;
    }

    private void initViews(){
        mFirstnameWrapper = (AppCompatEditText)rootView.findViewById(R.id.reg_input_firstname);
        mLastnameWrapper = (AppCompatEditText)rootView.findViewById(R.id.reg_input_lastname);
        mEmailWrapper = (AppCompatEditText)rootView.findViewById(R.id.reg_input_email);
        mPasswordWrapper = (AppCompatEditText)rootView.findViewById(R.id.reg_input_password);
        mRegistrationButton = (AppCompatButton)rootView.findViewById(R.id.btn_register);
    }

    private void setupSubmit() {
        mRegistrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegistrationDTO registrationDTO;
                String firstName = mFirstnameWrapper.getEditableText().toString().trim();
                String lastName = mLastnameWrapper.getEditableText().toString().trim();
                String email = mEmailWrapper.getEditableText().toString().trim();
                String password = mPasswordWrapper.getEditableText().toString().trim();
                if(firstName.isEmpty()){
                    mFirstnameWrapper.setError(getString(R.string.err_required_firstname));
                    mFirstnameWrapper.requestFocus();
                }else if(lastName.isEmpty()){
                    mLastnameWrapper.setError(getString(R.string.err_required_lastname));
                    mLastnameWrapper.requestFocus();
                }else if(email.isEmpty()){
                    mEmailWrapper.setError(getString(R.string.err_required_email));
                    mEmailWrapper.requestFocus();
                }else if(password.isEmpty()){
                    mPasswordWrapper.setError(getString(R.string.err_required_password));
                    mPasswordWrapper.requestFocus();
                }else if(!validateEmail(email)){
                    mEmailWrapper.setError(getString(R.string.err_valid_email));
                    mEmailWrapper.requestFocus();
                }else {
                    registrationDTO = new RegistrationDTO(email, password, firstName, lastName);
                    WorkerThread workerThread = new WorkerThread(handler);
                    workerThread.start();
                    Message msg = Message.obtain();
                    msg.obj = registrationDTO;
                    workerThread.handler.sendMessage(msg);
                }
            }
        });
    }

    private void setupHandler(){
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                GenericResponse<Boolean> response = (GenericResponse<Boolean>)msg.obj;
                String message;
                if(response.isSuccessfulOperation()) {
                    message = getString(R.string.success_registration);
                    showToastMessage(message);
                    clearInputs();
                    ((LogRegActivity)getActivity()).switchFragment(0);
                } else {
                    message = response.getErrorMessage();
                    showToastMessage(message);
                }
            }
        };
    }

    private void clearInputs() {
        mFirstnameWrapper.getEditableText().clear();
        mLastnameWrapper.getEditableText().clear();
        mEmailWrapper.getEditableText().clear();
        mPasswordWrapper.getEditableText().clear();
    }

    private boolean validateEmail(String email){
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private class WorkerThread extends Thread{
        private Handler handler;
        private Handler responseHandler;

        public WorkerThread(Handler handlerUI){
            responseHandler = handlerUI;
            handler = new Handler(){

                @Override
                public void handleMessage(Message msg) {
                    RegistrationDTO registrationDTO = (RegistrationDTO)msg.obj;
                    ServiceUtils.userService.register(registrationDTO).enqueue(new retrofit2.Callback<GenericResponse<UserDTO>>(){

                        @Override
                        public void onResponse(Call<GenericResponse<UserDTO>> call, Response<GenericResponse<UserDTO>> response) {
                            System.out.println("Meesage recieved successfully!");
                            responseHandler.sendMessage(ServiceUtils.getHandlerMessageFromResponse(response));
                        }

                        @Override
                        public void onFailure(Call<GenericResponse<UserDTO>> call, Throwable t) {
                            System.out.println("Error sending registration data!");
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
}
