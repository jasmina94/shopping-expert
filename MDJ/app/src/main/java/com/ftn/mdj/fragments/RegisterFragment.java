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
import com.ftn.mdj.dto.RegistrationDTO;
import com.ftn.mdj.dto.UserDTO;
import com.ftn.mdj.utils.GenericResponse;
import com.ftn.mdj.utils.ServiceUtils;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_register, container, false);

        mFirstnameWrapper = (AppCompatEditText)rootView.findViewById(R.id.reg_input_firstname);
        mLastnameWrapper = (AppCompatEditText)rootView.findViewById(R.id.reg_input_lastname);
        mEmailWrapper = (AppCompatEditText)rootView.findViewById(R.id.reg_input_email);
        mPasswordWrapper = (AppCompatEditText)rootView.findViewById(R.id.reg_input_password);

        mRegistrationButton = (AppCompatButton)rootView.findViewById(R.id.btn_register);

        setupHandler();
        setupSubmit();

        return rootView;
    }

    private void setupSubmit() {
        mRegistrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegistrationDTO registrationDTO;
                String firstname = mFirstnameWrapper.getEditableText().toString().trim();
                String lastname = mLastnameWrapper.getEditableText().toString().trim();
                String email = mEmailWrapper.getEditableText().toString().trim();
                String password = mPasswordWrapper.getEditableText().toString().trim();
                if(!validateEmail(email)){
                    mEmailWrapper.setError("Email address is not valid!");
                }else {
                    registrationDTO = new RegistrationDTO(firstname, lastname, email, password);
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
                } else {
                    message = response.getErrorMessage();
                }

                Toast t = Toast.makeText(getContext(), message, Toast.LENGTH_LONG);
                t.show();
            }
        };
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
    }
}
