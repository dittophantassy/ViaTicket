package com.grupoprominente.android.viaticket.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.grupoprominente.android.viaticket.R;
import com.grupoprominente.android.viaticket.adapters.MyRecyclerAdapter;
import com.grupoprominente.android.viaticket.adapters.MyRecyclerAdapterClickListener;
import com.grupoprominente.android.viaticket.models.Ticket;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MyRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView rvTickets = findViewById(R.id.rv_tickets);
        rvTickets.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerAdapter(new MyRecyclerAdapterClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Toast.makeText(MainActivity.this, "Position "+position, Toast.LENGTH_SHORT).show();
                Ticket t = adapter.getItems().get(position);
            }

            @Override
            public void onItemLongClick(View v, int position) {

            }
        });
        rvTickets.setAdapter(adapter);

        List<Ticket> ticketsList = new ArrayList<Ticket>();

        for (int i = 0; i<10;i++){
            Ticket ticket = new Ticket();
            ticket.setAmount((float)i);
            ticketsList.add(ticket);
        }
        adapter.addAll(ticketsList);



        Button btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences sharedPref = MainActivity.this.getSharedPreferences(getString(R.string.session_shared_preferences), Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.remove(getString(R.string.session_logged_user_key));
                        editor.commit();

                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);

                        startActivity(intent);
                        finish();

                    }
                }
        );

    }
}
