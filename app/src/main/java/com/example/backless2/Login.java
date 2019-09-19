package com.example.backless2;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.local.UserIdStorageFactory;

public class Login extends AppCompatActivity {

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    EditText etMail, etPassword;
    Button btnLogin,btnRegister;
    TextView tvReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        etMail= findViewById(R.id.etMail);
        etPassword= findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        tvReset = findViewById(R.id.tvReset);

        showProgress(true);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(etMail.getText().toString().isEmpty() || etPassword.getText().toString().isEmpty()) {
                    Toast.makeText(Login.this, "Enter all fiels", Toast.LENGTH_SHORT).show();
                }
                else {

                    String email = etMail.getText().toString().trim();
                    String password = etPassword.getText().toString().trim();

                    showProgress(true);

                    Backendless.UserService.login(email, password, new AsyncCallback<BackendlessUser>() {
                        @Override
                        public void handleResponse(BackendlessUser response) {

                            ApplicationClass.user = response;
                            Toast.makeText(Login.this, "Logged successfully!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Login.this, MainActivity.class));
                            Login.this.finish();
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Toast.makeText(Login.this, "Error"+fault.getMessage(), Toast.LENGTH_SHORT).show();
                            showProgress(false);
                        }
                    }, true);
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Login.this, Register.class));

            }
        });

        tvReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etMail.getText().toString().isEmpty()){
                    Toast.makeText(Login.this, "enter email address", Toast.LENGTH_SHORT).show();
                }
                else{
                    String email = etMail.getText().toString().trim();
                    showProgress(true);
                    tvLoad.setText("Busy sending reset instructions... please wait...");
                    
                    Backendless.UserService.restorePassword(email, new AsyncCallback<Void>() {
                        @Override
                        public void handleResponse(Void response) {
                            Toast.makeText(Login.this, "Reset instruction have been send", Toast.LENGTH_SHORT).show();
                            showProgress(false);

                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Toast.makeText(Login.this, "Error"+fault.getMessage(), Toast.LENGTH_SHORT).show();
                            showProgress(false);
                        }
                    });
                }

            }
        });
        tvLoad.setText("Checking login credentials...");
        Backendless.UserService.isValidLogin(new AsyncCallback<Boolean>() { //czy jakiś user juz sie logował
            @Override
            public void handleResponse(Boolean response) {

                if(response){ //jeśli tak to czy jest poprawny nadal
                    String userObjectId = UserIdStorageFactory.instance().getStorage().get();//jak sie logowal to zapisujemy jego name
                    tvLoad.setText("Loging...");
                    Backendless.Data.of(BackendlessUser.class).findById(userObjectId, new AsyncCallback<BackendlessUser>() { //czy user nadal jest valid zeby sie zalogowac
                        @Override
                        public void handleResponse(BackendlessUser response) {//jeśli tak to logujemy

                            ApplicationClass.user = response;
                            startActivity(new Intent(Login.this,MainActivity.class));
                            Login.this.finish();
                        }
                        @Override
                        public void handleFault(BackendlessFault fault) {// jak nie to nie
                            Toast.makeText(Login.this, "Error"+fault.getMessage(), Toast.LENGTH_SHORT).show();
                            showProgress(false);
                        }
                    });
                }
                else{// jak nie logowal sie niikt nidgy to nie sie nie dzieje
                    showProgress(false);
                }
            }
            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(Login.this, "Error"+fault.getMessage(), Toast.LENGTH_SHORT).show();
                showProgress(false);
            }
        });
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
// for very easy animations. If available, use these APIs to fade-in
// the progress spinner.
        if ( Build.VERSION.SDK_INT >=Build.VERSION_CODES.HONEYCOMB_MR2){
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
            tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoad.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else{
// The ViewPropertyAnimator APIs are not available, so simply show
// and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}