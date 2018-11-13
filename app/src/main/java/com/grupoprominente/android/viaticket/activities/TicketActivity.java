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
import com.grupoprominente.android.viaticket.adapters.CurrencyAdapter;
import com.grupoprominente.android.viaticket.adapters.TicketTypeAdapter;
import com.grupoprominente.android.viaticket.models.CurrencyType;
import com.grupoprominente.android.viaticket.models.Ticket;
import com.grupoprominente.android.viaticket.models.TicketType;
import com.grupoprominente.android.viaticket.models.Trip;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class TicketActivity extends AppCompatActivity {

    private Ticket ticket;
    private long ticketId;

    private static final int CAMERA_REQUEST = 1888;
    private ImageButton imageButton;

    private EditText etAmount;
    private Spinner spnCurrency;
    private Spinner spnTypes;
    private Spinner spnTrips;
    private EditText txtIssued;
    private Calendar myCalendar;
    DatePickerDialog.OnDateSetListener dateListener;
    private EditText txtCid;
    private Uri mCurrentPhotoPath;
    private Uri mTempPhotoPath;

    private int tripId;
    private String cid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        etAmount = findViewById(R.id.txtAmount);

        Bundle extras = getIntent().getExtras();

        spnCurrency = findViewById(R.id.spnCurrency);
        CurrencyAdapter currencyAdapter = new CurrencyAdapter(this);
        spnCurrency.setAdapter(currencyAdapter);

        spnTypes = findViewById(R.id.spnTicketType);
        ArrayAdapter<TicketType> typeAdapter = new TicketTypeAdapter(this);
        spnTypes.setAdapter(typeAdapter);

        spnTrips = findViewById(R.id.spnTrip);

        Trip empty = new Trip();
        empty.setDestination(this.getString(R.string.unassigned_tickets));
        List<Trip> trips = Trip.listAll(Trip.class);
        trips.add(0, empty);
        ArrayAdapter<Trip> tripAdapter = new ArrayAdapter<Trip>(this, android.R.layout.simple_spinner_dropdown_item, trips);
        spnTrips.setAdapter(tripAdapter);

        txtIssued = findViewById(R.id.txtIssued);
        myCalendar = Calendar.getInstance();
        myCalendar.set(Calendar.HOUR_OF_DAY, 0);

        txtCid = findViewById(R.id.txtCid);

        updateLabel();

        imageButton = this.findViewById(R.id.btnPhoto);

        if(extras != null) {
            ticketId = extras.getLong("ID");
            tripId = extras.getInt("TRIP_ID");
            cid = extras.getString("CID");

            if (ticketId>0)
            {
                setTitle(getString(R.string.ticket_activity_edit_title));
                ticket = Ticket.findById(Ticket.class,ticketId);

                if (ticket.getIssueDate() != null) {
                    myCalendar.setTime(ticket.getIssueDate());
                    updateLabel();
                }

                etAmount.setText(getString(R.string.currency_none_format, ticket.getAmount()));
                spnCurrency.setSelection(currencyAdapter.getPosition(ticket.getCurrency()));
                spnTypes.setSelection(typeAdapter.getPosition(ticket.getTicketType()));

                if (ticket.getImageFile() != null)
                    mCurrentPhotoPath = Uri.parse(ticket.getImageFile());

                txtCid.setText(ticket.getCid());

                imageButton.setImageURI(mCurrentPhotoPath);
            }
            else {
                ticket = new Ticket();
                ticket.setCid(cid);

                txtCid.setText(cid);
                etAmount.setText("0.00");
            }
            if (tripId>0)
            {
                for (Trip trip :trips)
                {
                    if (trip.getIdTrip() == tripId){
                        spnTrips.setSelection(tripAdapter.getPosition(trip));
                        break;
                    }
                }
            }
            ticket.setIdTrip(tripId);
        }

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

        Button okButton = this.findViewById(R.id.btnOK);
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
        String myFormat = "dd/MM/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        txtIssued.setText(sdf.format(myCalendar.getTime()));
    }

    private void save()
    {
        if(ticket == null)
            ticket = new Ticket();


        ticket.setIdTrip(((Trip)spnTrips.getSelectedItem()).getIdTrip());

        //TODO should I allow a zero amount? (in case of a split bill)
        String amount = etAmount.getText().toString();
        if (!amount.isEmpty()){
            ticket.setAmount(Double.parseDouble(etAmount.getText().toString()));
        }
        else {
            //TODO popup error
            return;
        }

        ticket.setCurrency((CurrencyType) spnCurrency.getSelectedItem());
        ticket.setTicketType((TicketType) spnTypes.getSelectedItem());

        ticket.setIssueDate(myCalendar.getTime());
        ticket.setCid((!txtCid.getText().toString().isEmpty()) ? txtCid.getText().toString() : cid);

        //TODO is the photo a requirement?
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
                ".bmp",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        return image;
    }
}
