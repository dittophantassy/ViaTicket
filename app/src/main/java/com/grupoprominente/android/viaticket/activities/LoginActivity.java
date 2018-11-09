package com.grupoprominente.android.viaticket.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.grupoprominente.android.viaticket.R;
import com.grupoprominente.android.viaticket.application.Global;
import com.grupoprominente.android.viaticket.data.api.RestApi;
import com.grupoprominente.android.viaticket.data.api.response.UserResponse;
import com.grupoprominente.android.viaticket.data.serialization.UserSerializer;

public class LoginActivity extends AppCompatActivity {
    private EditText txtUser;
    private EditText txtPassword;
    private ProgressBar pbLogin;
    private LoginTask loginTask;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtUser = findViewById(R.id.txtUser);
        txtPassword = findViewById(R.id.txtPassword);
        pbLogin = findViewById(R.id.pb_login);

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        login();
                    }
                }
        );
    }

    private void login() {
        if (!TextUtils.isEmpty(txtUser.getText()) && !TextUtils.isEmpty(txtPassword.getText())) {
            loginTask = new LoginTask(txtUser.getText().toString(), txtPassword.getText().toString());
            loginTask.execute();
        } else {
            Snackbar.make(findViewById(android.R.id.content), R.string.login_activity_enter_user_pass, Snackbar.LENGTH_SHORT).show();
        }
    }

    private void startMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);

        startActivity(intent);
        finish();
    }

    private class LoginTask extends AsyncTask<Void, Integer, UserResponse> {
        private String username;
        private String password;

        public LoginTask(String email, String password) {
            this.username = email;
            this.password = password;
        }

        @Override
        protected void onPreExecute() {
            btnLogin.setVisibility(View.INVISIBLE);
            pbLogin.setVisibility(View.VISIBLE);
        }

        @Override
        protected UserResponse doInBackground(Void... voids) {
            UserResponse response = RestApi.getInstance().login(username, password);

            if (response != null && response.getUser() != null) {
                response.getUser().setPassword(password);
                UserSerializer.getInstance().save(LoginActivity.this, response.getUser());
            }

            return response;
        }

        @Override
        protected void onPostExecute(UserResponse response) {
            pbLogin.setVisibility(View.GONE);

            if (response != null && response.getCode() == 0) {
                startMainActivity();
            } else {
                btnLogin.setVisibility(View.VISIBLE);

                if (response != null) {
                    Snackbar.make(findViewById(android.R.id.content), R.string.login_activity_wrong_user_pass, Snackbar.LENGTH_SHORT).show();
                } else {
                    if (Global.isConnected) {
                        Snackbar.make(findViewById(android.R.id.content), R.string.error_no_server_connection, Snackbar.LENGTH_SHORT).show();
                    } else {
                        Snackbar.make(findViewById(android.R.id.content), R.string.error_no_internet, Snackbar.LENGTH_SHORT).show();
                    }
                }
            }

        }
    }
}





