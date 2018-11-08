package com.grupoprominente.android.viaticket.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Button;


import com.grupoprominente.android.viaticket.R;
import com.grupoprominente.android.viaticket.models.CurrencyType;
import com.grupoprominente.android.viaticket.models.Expense;
import com.grupoprominente.android.viaticket.models.Ticket;
import com.grupoprominente.android.viaticket.models.TicketType;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class TicketActivity extends AppCompatActivity {

    private Ticket ticket;
    private long ticketId;
    private long expenseId;

    private static final int CAMERA_REQUEST = 1888;
    private ImageButton imageButton;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private EditText etAmount;
    private Spinner spnCurrency;
    private Spinner spnTypes;
    private EditText txtIssued;
    private Calendar myCalendar;
    DatePickerDialog.OnDateSetListener dateListener;
    private Uri mCurrentPhotoPath;
    private Uri mTempPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        etAmount = findViewById(R.id.txtAmount);

        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            ticketId = extras.getLong("TICKET_ID");
            expenseId = extras.getLong("EXPENSE_ID");
            setTitle("Editar Ticket");
        }


        spnCurrency = (Spinner) findViewById(R.id.spnCurrency);
        ArrayAdapter<CharSequence> currencyAdapter = ArrayAdapter.createFromResource(this, R.array.CurrencyTypes, android.R.layout.simple_spinner_item);
        currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCurrency.setAdapter(currencyAdapter);

        spnTypes = (Spinner) findViewById(R.id.spnTicketType);
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this, R.array.TicketTypes, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTypes.setAdapter(typeAdapter);

        txtIssued = (EditText) findViewById(R.id.txtIssued);
        myCalendar = Calendar.getInstance();
        myCalendar.set(Calendar.HOUR_OF_DAY, 0);
        updateLabel();


        dateListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        txtIssued.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(TicketActivity.this, dateListener, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        imageButton = (ImageButton) this.findViewById(R.id.btnPhoto);
        imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {

                    }

                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(TicketActivity.this,
                                "com.grupoprominente.android.viaticket.fileprovider",
                                photoFile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        mTempPhotoPath = photoURI;
                        startActivityForResult(intent, CAMERA_REQUEST);
                    }
                }
            }
        });

        Button okButton = (Button) this.findViewById(R.id.btnOK);
        okButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                save();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST) {
            if (resultCode == RESULT_CANCELED){
                File f = new File(mTempPhotoPath.getPath());
                f.delete();
            }
            else if (resultCode == RESULT_OK)
            {
                mCurrentPhotoPath = mTempPhotoPath;
                imageButton.setImageURI(mCurrentPhotoPath);
            }
        }
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        txtIssued.setText(sdf.format(myCalendar.getTime()));
    }

    private void save()
    {
        if(ticket == null)
            ticket = new Ticket();

        if (expenseId>0){
            ticket.setExpense(Expense.findById(Expense.class, expenseId));
        }
        //TODO validar, no dejar al usuario aceptar con un monto vacío (0 creo que debería dejar)
        String amount = etAmount.getText().toString();
        if (!amount.isEmpty()){
            ticket.setAmount(Double.parseDouble(etAmount.getText().toString()));
        }
        else {

            return;
        }

        ticket.setCurrency(CurrencyType.values()[(int)spnCurrency.getSelectedItemId()]);
        ticket.setTicketType(TicketType.values()[(int)spnTypes.getSelectedItemId()]);
        //TODO es obligatoria la foto?
        if (mCurrentPhotoPath!= null)
            ticket.setImageFile(mCurrentPhotoPath.toString());

        ticket.save();

        finish();
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        return image;
    }
}
