package com.grupoprominente.android.viaticket;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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


                        if( !user.equals("Juan") && !password.equals("123"))
                        {
                            Toast.makeText(LoginActivity.this, "Usario y/o contraseña no válidos.", Toast.LENGTH_SHORT).show();
                        }else{

                        }
                    }
                }
        );
    }
}
