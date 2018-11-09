package com.grupoprominente.android.viaticket.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.grupoprominente.android.viaticket.data.serialization.UserSerializer;
import com.grupoprominente.android.viaticket.models.User;

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
            Snackbar.make(findViewById(android.R.id.content), "Ingrese usuario y contraseña", Snackbar.LENGTH_SHORT).show();
        }
    }

    private void startMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);

        startActivity(intent);
        finish();
    }

    private class LoginTask extends AsyncTask<Void, Integer, User> {
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
        protected User doInBackground(Void... voids) {
            User user = RestApi.getInstance().login(username, password);

            if (user != null) {
                user.setPassword(password);
                UserSerializer.getInstance().save(LoginActivity.this, user);
            }

            return user;
        }

        @Override
        protected void onPostExecute(User user) {
            pbLogin.setVisibility(View.GONE);

            if (user != null && user.getCode() == 0) {
                startMainActivity();
            } else {
                btnLogin.setVisibility(View.VISIBLE);

                if (user != null) {
                    Snackbar.make(findViewById(android.R.id.content), "Nombre de usuario o password incorrecto", Snackbar.LENGTH_SHORT).show();
                } else {
                    if (Global.isConnected) {
                        Snackbar.make(findViewById(android.R.id.content), "Se produjo un error de conexión al servidor", Snackbar.LENGTH_SHORT).show();
                    } else {
                        Snackbar.make(findViewById(android.R.id.content), "No hay conexión a internet", Snackbar.LENGTH_SHORT).show();
                    }
                }
            }

        }
    }
}





