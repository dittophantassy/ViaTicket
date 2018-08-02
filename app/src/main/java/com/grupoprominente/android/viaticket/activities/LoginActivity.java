package com.grupoprominente.android.viaticket.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.grupoprominente.android.viaticket.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText txtUser = findViewById(R.id.txtUser);
        final EditText txtPassword = findViewById(R.id.txtPassword);


        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String user = txtUser.getText().toString();
                        String password = txtPassword.getText().toString();

                        if (!user.equals("Juan") && !password.equals("123")) {
                            Toast.makeText(LoginActivity.this, "Usario y/o contraseña no válidos.", Toast.LENGTH_SHORT).show();
                        } else {

                            SharedPreferences sharedPref = LoginActivity.this.getSharedPreferences(getString(R.string.session_shared_preferences), Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString(getString(R.string.session_logged_user_key), user);
                            editor.commit();

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                            startActivity(intent);
                            finish();
                        }
                    }
                }
        );
    }
}
