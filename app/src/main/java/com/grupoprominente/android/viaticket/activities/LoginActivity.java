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
import com.grupoprominente.android.viaticket.data.api.RestApi;
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
                        String user = txtUser.getText().toString();
                        String password = txtPassword.getText().toString();

                        login();
//                        if (!user.equals("Juan") && !password.equals("123")) {
//                            Toast.makeText(LoginActivity.this, "Usario y/o contraseña no válidos.", Toast.LENGTH_SHORT).show();
//                        } else {
//
//                            SharedPreferences sharedPref = LoginActivity.this.getSharedPreferences(getString(R.string.session_shared_preferences), Context.MODE_PRIVATE);
//                            SharedPreferences.Editor editor = sharedPref.edit();
//                            editor.putString(getString(R.string.session_logged_user_key), user);
//                            editor.commit();
//
//                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//
//                            startActivity(intent);
//                            finish();
//                        }
                    }
                }
        );
    }

    private void login() {
        if (!TextUtils.isEmpty(txtUser.getText()) && !TextUtils.isEmpty(txtPassword.getText())) {
            loginTask = new LoginTask(txtUser.getText().toString(), txtPassword.getText().toString());
            loginTask.execute();
        } else {
            //Snackbar.make(findViewById(android.R.id.content), R.string.must_type_email_password, Snackbar.LENGTH_SHORT).show();
        }
    }

    private void startHomeActivity() {


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
                SharedPreferences sharedPref = LoginActivity.this.getSharedPreferences(getString(R.string.session_shared_preferences), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.session_logged_user_key), user.getUsername());
                editor.commit();

                // user.setPassword(password);
                // UserSerializer.getInstance().save(LoginActivity.this, user);
            }

            return user;
        }

        @Override
        protected void onPostExecute(User user) {
            pbLogin.setVisibility(View.GONE);

            if (user != null && user.getCode() == 0) {
                startHomeActivity();
            } else {
                btnLogin.setVisibility(View.VISIBLE);

                if (user != null) {
                    Snackbar.make(findViewById(android.R.id.content), "Nombre de usuario o password incorrecto", Snackbar.LENGTH_SHORT).show();
                } else {
//                    if (AppInfo.connected) {
//                        Snackbar.make(findViewById(android.R.id.content), R.string.error_bad_connection_quality, Snackbar.LENGTH_SHORT).show();
//                    } else {
//                        Snackbar.make(findViewById(android.R.id.content), R.string.error_connection, Snackbar.LENGTH_SHORT).show();
//                    }
                }
            }

        }
    }
}





