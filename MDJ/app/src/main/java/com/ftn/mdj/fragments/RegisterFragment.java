package com.ftn.mdj.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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
import com.ftn.mdj.threads.RegisterThread;
import com.ftn.mdj.utils.GenericResponse;
import com.ftn.mdj.services.ServiceUtils;
import com.ftn.mdj.utils.UtilHelper;
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
    private View rootView;

    public ProgressDialog mProgressDialog;
    private FragmentActivity parentActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_register, container, false);
        parentActivity = super.getActivity();
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
                }else if(!UtilHelper.validateEmail(email)){
                    mEmailWrapper.setError(getString(R.string.err_valid_email));
                    mEmailWrapper.requestFocus();
                }else {
                    registrationDTO = new RegistrationDTO(email, password, firstName, lastName);
                    registrationDTO.setRegisterType("SIMPLE");
                    RegisterThread registerThread = new RegisterThread(handler);
                    registerThread.start();
                    Message msg = Message.obtain();
                    msg.obj = registrationDTO;
                    registerThread.getHandler().sendMessage(msg);
                    showProgressDialog();
                }
            }
        });
    }

    private void setupHandler(){
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                GenericResponse<UserDTO> response = (GenericResponse<UserDTO>)msg.obj;
                String message;
                hideProgressDialog();
                if(response.isSuccessfulOperation()) {
                    message = getString(R.string.success_registration);
                    UtilHelper.showToastMessage(getContext(), message, UtilHelper.ToastLength.LONG);
                    clearInputs();
                    ((LogRegActivity)getActivity()).switchFragment(0);
                } else {
                    message = response.getErrorMessage();
                    UtilHelper.showToastMessage(getContext(), message,  UtilHelper.ToastLength.LONG);
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

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this.parentActivity);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
