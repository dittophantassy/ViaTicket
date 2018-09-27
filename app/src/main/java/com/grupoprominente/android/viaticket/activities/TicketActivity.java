package com.grupoprominente.android.viaticket.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Button;


import com.grupoprominente.android.viaticket.R;



public class TicketActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 1888;
    private ImageButton imageButton;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);
        Bundle extras = getIntent().getExtras();
        long ticketId;
        if(extras != null) {
            ticketId = extras.getLong("ID");
            setTitle("Editar Ticket");
        }


        Spinner spnCurrency = (Spinner) findViewById(R.id.spnCurrency);
        ArrayAdapter<CharSequence> currencyAdapter = ArrayAdapter.createFromResource(this, R.array.CurrencyTypes, android.R.layout.simple_spinner_item);
        currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCurrency.setAdapter(currencyAdapter);

        Spinner spnTypes = (Spinner) findViewById(R.id.spnTicketType);
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this, R.array.TicketTypes, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTypes.setAdapter(typeAdapter);


        imageButton = (ImageButton) this.findViewById(R.id.btnPhoto);
        imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(intent, CAMERA_REQUEST);
            }
        });

        Button okButton = (Button) this.findViewById(R.id.btnOK);
        okButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TicketActivity.this, MainActivity.class);

                startActivity(intent);
                finish();
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST) {
            if (data.getExtras() != null)
            {
                Bitmap image = (Bitmap) data.getExtras().get("data");
                imageButton.setImageBitmap(image);
            }
        }
    }
}
