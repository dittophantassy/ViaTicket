package com.grupoprominente.android.viaticket.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.grupoprominente.android.viaticket.data.serialization.UserSerializer;
import com.grupoprominente.android.viaticket.models.User;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        User user = UserSerializer.getInstance().load(this);
        Intent intent;

        if(user == null)
            intent = new Intent(this, LoginActivity.class);
        else
            intent = new Intent(this, MainActivity.class);

        startActivity(intent);
        finish();
    }
}
