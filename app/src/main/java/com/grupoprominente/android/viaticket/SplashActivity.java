package com.grupoprominente.android.viaticket;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        SharedPreferences sharedPref = SplashActivity.this.getSharedPreferences(getString(R.string.session_shared_preferences), Context.MODE_PRIVATE);
        Intent intent;
        if (sharedPref.getString(getString(R.string.session_logged_user_key), null)==null ){
            intent = new Intent(this, LoginActivity.class);
        }
        else {
            intent = new Intent(this, MainActivity.class);
        }

        startActivity(intent);
        finish();

    }
}
