package com.grupoprominente.android.viaticket.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.app.ActionBar;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.grupoprominente.android.viaticket.R;
import com.grupoprominente.android.viaticket.adapters.MyRecyclerAdapter;
import com.grupoprominente.android.viaticket.adapters.MyRecyclerAdapterClickListener;
import com.grupoprominente.android.viaticket.data.TicketDao;
import com.grupoprominente.android.viaticket.data.api.RestApi;
import com.grupoprominente.android.viaticket.models.CurrencyType;
import com.grupoprominente.android.viaticket.models.Ticket;
import com.grupoprominente.android.viaticket.models.TicketType;
import com.grupoprominente.android.viaticket.models.Trip;
import com.grupoprominente.android.viaticket.models.User;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MyRecyclerAdapter adapter;
    private DrawerLayout mDrawerLayout;
    private MenuItem mPreviousMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        mDrawerLayout.closeDrawers();
                        menuItem.setCheckable(true);
                        menuItem.setChecked(true);
                        if (mPreviousMenuItem != null) {
                            mPreviousMenuItem.setChecked(false);
                        }
                        mPreviousMenuItem = menuItem;


                        switch (menuItem.getItemId()) {
                            case R.id.item_unasigned:
                                setTitle("Tickets sin asignar");
                                menuItem.setChecked(true);
                                break;
                            case R.id.item_logout:
                                SharedPreferences sharedPref = MainActivity.this.getSharedPreferences(getString(R.string.session_shared_preferences), Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.remove(getString(R.string.session_logged_user_key));
                                editor.commit();

                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);

                                startActivity(intent);
                                finish();
                                break;
                                default:
                                    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                                    toolbar.setTitle("Prueba");
                                    break;

                        }

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });

        LoadMenuItemsTask loadMenuItemsTask = new LoadMenuItemsTask("dperalta");
        loadMenuItemsTask.execute();

        RecyclerView rvTickets = findViewById(R.id.rv_tickets);
        rvTickets.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerAdapter(new MyRecyclerAdapterClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Ticket t = adapter.getItems().get(position);

                Intent intent = new Intent(MainActivity.this, TicketActivity.class);
                intent.putExtra("ID", t.getId());

                startActivity(intent);

            }

            @Override
            public void onItemLongClick(View v, int position) {

            }
        });
        rvTickets.setAdapter(adapter);

        List<Ticket> ticketsList = TicketDao.listAll();
        ticketsList.get(0).setTicketType(TicketType.TAXI);
        ticketsList.get(1).setTicketType(TicketType.TAXI);
        ticketsList.get(2).setTicketType(TicketType.TAXI);

        adapter.addAll(ticketsList);

        FloatingActionButton btnAddTicket = findViewById(R.id.btnAddTicket);
        btnAddTicket.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, TicketActivity.class);
                        startActivity(intent);
                    }
                }
        );
    }


    @Override
    protected void onResume() {
        super.onResume();

        LoadMenuItemsTask loadMenuItemsTask = new LoadMenuItemsTask("dperalta");
        loadMenuItemsTask.execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private class LoadMenuItemsTask extends AsyncTask<Void, Integer, ArrayList<Trip>> {
        private String username;

        public LoadMenuItemsTask(String username) {
            this.username = username;
        }


        @Override
        protected ArrayList<Trip> doInBackground(Void... voids) {
            ArrayList<Trip> tripsByCurrentUser = RestApi.getInstance().getTripsByUsername(username);

            return tripsByCurrentUser;
        }

        @Override
        protected void onPostExecute(ArrayList<Trip> tripsByCurrentUser) {
            NavigationView navigationView = findViewById(R.id.nav_view);
            Menu tripsMenu = navigationView.getMenu().getItem(1).getSubMenu();

            if (tripsByCurrentUser.size() > 0) {
                android.text.format.DateFormat dateFormat = new android.text.format.DateFormat();

                tripsMenu.clear();
                for (int i = 0; i < tripsByCurrentUser.size(); i++) {
                    Trip currentTrip = tripsByCurrentUser.get(i);
                    MenuItem item = tripsMenu.add(0, i, 0, currentTrip.getDestination() + " - " + dateFormat.format("dd/MM", currentTrip.getTripDate()));
                    item.setIcon(R.drawable.ic_flight_black_24dp);
                }
            }
        }
    }

}
