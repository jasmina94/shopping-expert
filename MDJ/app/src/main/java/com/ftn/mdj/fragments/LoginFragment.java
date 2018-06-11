package com.ftn.mdj.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.ftn.mdj.R;
import com.ftn.mdj.activities.ForgotPasswordActivity;
import com.ftn.mdj.activities.MainActivity;
import com.ftn.mdj.dto.LoginDTO;
import com.ftn.mdj.dto.RegistrationDTO;
import com.ftn.mdj.dto.UserDTO;
import com.ftn.mdj.services.MDJInterceptor;
import com.ftn.mdj.threads.LoginThread;
import com.ftn.mdj.threads.RegisterThread;
import com.ftn.mdj.utils.GenericResponse;
import com.ftn.mdj.utils.SharedPreferencesManager;
import com.ftn.mdj.utils.UtilHelper;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Executor;

/**
 * Created by Jasmina on 11/05/2018.
 */

public class LoginFragment extends Fragment {

    private final String TAG = "LoginFragment";

    private final static String SIGN_IN_FACEBOOK = "fb";
    private final static String SIGN_IN_GOOGLE = "google";
    private final static String SIGN_IN_INNER = "mdj";

    // Google & facebook sign in //
    private static final int RC_SIGN_IN = 9001;
    private final int FACEBOOK_LOG_IN_REQUEST_CODE = 64206;  // for facebook log in
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private CallbackManager mCallbackManager;

    private Handler simpleLoginHandler;
    private Handler registerHandler;

    private AppCompatEditText mEmailWrapper;
    private AppCompatEditText mPasswordWrapper;
    private AppCompatButton mLoginSimpleButton;
    private AppCompatButton mLoginGoogleButton;
    private LoginButton mLoginFacebookButton;
    private TextView mForgotPassTextView;

    private View rootView;

    private FragmentActivity parentActivity;

    public ProgressDialog mProgressDialog;

    private SharedPreferencesManager sharedPreferencesManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_login, container, false);
        parentActivity = super.getActivity();

        mAuth = FirebaseAuth.getInstance(); // Initialize auth
        mCallbackManager = CallbackManager.Factory.create();
        sharedPreferencesManager = SharedPreferencesManager.getInstance();

        initView();
        setupSimpleLoginHandler();
        setupRegisterHandler();

        initGoogleSignIn();
        initFacebookLogin();
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        System.out.println("usao ovde");
        super.onCreate(savedInstanceState);
        try {
            System.out.println("usao ovde try");
            PackageInfo info = super.getActivity().getPackageManager().getPackageInfo(
                    "com.ftn.mdj",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                System.out.println(Base64.encodeToString(md.digest(),  Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            System.out.println("On start");
            System.out.println(currentUser.getDisplayName());
            goToMainActivity();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Log.w(TAG, "Google sign in failed", e);
            }
        }else {
            System.out.println("aaa");
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }


    private void initGoogleSignIn(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this.parentActivity, gso);
    }

    private void initFacebookLogin(){
        mLoginFacebookButton.setReadPermissions("email", "public_profile");
        mLoginFacebookButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
            }
        });
    }


    //Kad google obavi svoje ja moram tog da registrujem u bazi
    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
        showProgressDialog();
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this.parentActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            putSignInTypeIntoSharedPreferences(SIGN_IN_GOOGLE);
                            RegistrationDTO registrationDTO = new RegistrationDTO(user);
                            registrationDTO.setDeviceInstance(sharedPreferencesManager.getString(SharedPreferencesManager.Key.DEVICE_INSTANCE.name()));
                            registrationDTO.setRegisterType("GOOGLE");
                            callRegisterCustomUser(registrationDTO);
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Snackbar.make(rootView.findViewById(R.id.fragment_login), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                        }
                        hideProgressDialog();
                    }
                });
    }

    //Kad fb odradi svoje uspesno ja moram da registrujem tog korisnika
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        showProgressDialog();
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            putSignInTypeIntoSharedPreferences(SIGN_IN_FACEBOOK);
                            RegistrationDTO registrationDTO = new RegistrationDTO(user);
                            callRegisterCustomUser(registrationDTO);
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            UtilHelper.showToastMessage(getContext(), "Authentication failed.", UtilHelper.ToastLength.SHORT);
                        }
                        hideProgressDialog();
                    }
                });
    }


    private void initView(){
        mEmailWrapper = (AppCompatEditText)rootView.findViewById(R.id.log_input_email);
        mPasswordWrapper = (AppCompatEditText)rootView.findViewById(R.id.log_input_password);
        mLoginSimpleButton = (AppCompatButton)rootView.findViewById(R.id.btn_login);
        mLoginGoogleButton = (AppCompatButton)rootView.findViewById(R.id.btn_login_google);
        mLoginFacebookButton = (LoginButton) rootView.findViewById(R.id.btn_login_facebook);
        mForgotPassTextView = (TextView)rootView.findViewById(R.id.link_forgot_pass);

        mLoginSimpleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                simpleLogin();
            }
        });

        mLoginGoogleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInWithGoogle();
            }
        });

        mForgotPassTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgotPasswordActivity();
            }
        });
    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
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

    //Ovo je obicno logovanje i stavljam jwt token u shared pref
    private void setupSimpleLoginHandler(){
        simpleLoginHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                GenericResponse<String> response = (GenericResponse<String>)msg.obj;
                String message;
                hideProgressDialog();
                if(response.isSuccessfulOperation()) {
                    message = getString(R.string.success_login);
                    showToastMessage(message);
                    putJWTIntoSharedPreferences(response.getEntity());
                    putSignInTypeIntoSharedPreferences(SIGN_IN_INNER);
                    goToMainActivity();
                } else {
                    message = response.getErrorMessage();
                    showToastMessage(message);
                }
            }
        };
    }

    //Ovde treba da stavim ID registrovanog korisnika u shared preferences. Ovo je nasilna registracija
    private void setupRegisterHandler(){
        registerHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                GenericResponse<UserDTO> response = (GenericResponse<UserDTO>)msg.obj;
                String message;
                if(response.isSuccessfulOperation()) {
                    UserDTO userDTO = response.getEntity();
                    sharedPreferencesManager.put(SharedPreferencesManager.Key.USER_ID, userDTO.getId().intValue());
                    sharedPreferencesManager.put(SharedPreferencesManager.Key.USER_EMAIL, userDTO.getEmail());

                    goToMainActivity();
                } else {
                    message = response.getErrorMessage();
                    showToastMessage(message);
                }
            }
        };
    }

    private void putJWTIntoSharedPreferences(String token) {
        sharedPreferencesManager.put(SharedPreferencesManager.Key.JWT_KEY, token);
        MDJInterceptor.jwt = token;
    }

    private void putSignInTypeIntoSharedPreferences(String signInType){
        sharedPreferencesManager.put(SharedPreferencesManager.Key.SIGN_IN_TYPE, signInType);
    }

    private void showToastMessage(String message){
        Toast t = Toast.makeText(getContext(), message, Toast.LENGTH_LONG);
        t.show();
    }

    private void goToMainActivity(){
        Context context = rootView.getContext();
        Intent intent = new Intent(context, MainActivity.class);
        startActivity(intent);
    }

    private void simpleLogin() {
        LoginDTO loginDTO;
        String email = mEmailWrapper.getEditableText().toString().trim();
        String password = mPasswordWrapper.getEditableText().toString().trim();
        if(email.isEmpty()){
            mEmailWrapper.setError(getString(R.string.err_required_email));
            mEmailWrapper.requestFocus();
        }else if(password.isEmpty()){
            mPasswordWrapper.setError(getString(R.string.err_required_password));
            mPasswordWrapper.requestFocus();
        }else if(!UtilHelper.validateEmail(email)){
            mEmailWrapper.setError(getString(R.string.err_valid_email));
            mEmailWrapper.requestFocus();
        }else {
            loginDTO = new LoginDTO(email, password, sharedPreferencesManager.getString(SharedPreferencesManager.Key.DEVICE_INSTANCE.name()));
            LoginThread loginThread = new LoginThread(simpleLoginHandler);
            loginThread.start();
            Message msg = Message.obtain();
            msg.obj = loginDTO;
            loginThread.getHandler().sendMessage(msg);
        }
    }

    private void forgotPasswordActivity(){
        Context context = rootView.getContext();
        Intent intent = new Intent(context, ForgotPasswordActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    private void callRegisterCustomUser(RegistrationDTO registrationDTO){
        RegisterThread registerThread = new RegisterThread(registerHandler);
        registerThread.start();
        Message msg = Message.obtain();
        msg.obj = registrationDTO;
        registerThread.getHandler().sendMessage(msg);
    }

}
