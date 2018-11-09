package com.grupoprominente.android.viaticket.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.grupoprominente.android.viaticket.R;
import com.grupoprominente.android.viaticket.adapters.MyRecyclerAdapter;
import com.grupoprominente.android.viaticket.adapters.MyRecyclerAdapterClickListener;
import com.grupoprominente.android.viaticket.application.Global;
import com.grupoprominente.android.viaticket.data.api.RestApi;
import com.grupoprominente.android.viaticket.data.api.response.TicketResponse;
import com.grupoprominente.android.viaticket.data.api.response.TripResponse;
import com.grupoprominente.android.viaticket.data.serialization.UserSerializer;
import com.grupoprominente.android.viaticket.models.Ticket;
import com.grupoprominente.android.viaticket.models.Trip;
import com.grupoprominente.android.viaticket.models.User;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private MyRecyclerAdapter adapter;
    private DrawerLayout mDrawerLayout;
    private MenuItem mPreviousMenuItem;
    private ImageButton btnAction;
    private ProgressBar pbMain;
    private NavigationView navigationView;

    private ArrayList<Trip> trips;
    private Trip selectedTrip;

    private User loggedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        btnAction = toolbar.findViewById(R.id.imgBtnAction);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectMenuItem(menuItem);
                        loadTickets();

                        btnAction.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (selectedTrip == null) {
                                    showMoveTickets(adapter.getItems());
                                } else {
                                    synchronizeTickets();
                                }
                            }
                        });

                        return true;
                    }
                });

        RecyclerView rvTickets = findViewById(R.id.rv_tickets);
        rvTickets.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerAdapter(new MyRecyclerAdapterClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Ticket t = adapter.getItems().get(position);

                Intent intent = new Intent(MainActivity.this, TicketActivity.class);
                intent.putExtra("ID", t.getId());
                intent.putExtra("TRIP_ID", (selectedTrip != null) ? selectedTrip.getIdTrip() : 0);
                intent.putExtra("CID", (selectedTrip != null) ? selectedTrip.getCid() : "");

                startActivity(intent);

            }

            @Override
            public void onItemLongClick(View v, final int position) {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_Material_Dialog_NoActionBar);
                } else {
                    builder = new AlertDialog.Builder(MainActivity.this);
                }

                builder.setTitle(R.string.main_activity_ask_delete_ticket)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Ticket t = adapter.getItems().get(position);
                                t.delete();

                                loadMenu();
                                loadTickets();
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, null)
                        .show();
            }
        });
        rvTickets.setAdapter(adapter);


        FloatingActionButton btnAddTicket = findViewById(R.id.btnAddTicket);
        btnAddTicket.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, TicketActivity.class);
                        intent.putExtra("TRIP_ID", (selectedTrip != null) ? selectedTrip.getIdTrip() : 0);
                        intent.putExtra("CID", (selectedTrip != null) ? selectedTrip.getCid() : "");

                        startActivity(intent);
                    }
                }
        );

        selectMenuItem(navigationView.getMenu().getItem(0));

        loggedUser = UserSerializer.getInstance().load(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

       loadMenu();
       loadTickets();
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


    private void loadMenu() {
        LoadMenuItemsTask loadMenuItemsTask = new LoadMenuItemsTask(loggedUser.getUsername());
        loadMenuItemsTask.execute();
    }

    private void selectMenuItem(MenuItem menuItem) {
        mDrawerLayout.closeDrawers();
        menuItem.setCheckable(true);
        menuItem.setChecked(true);

        if (mPreviousMenuItem != null) {
            mPreviousMenuItem.setChecked(false);
        }

        mPreviousMenuItem = menuItem;

        switch (menuItem.getItemId()) {
            case R.id.item_unassigned:
                setTitle(R.string.unassigned_tickets);
                menuItem.setChecked(true);

                selectedTrip = null;

                btnAction.setImageResource(R.drawable.ic_create_new_folder_black_24dp);

                break;
            case R.id.item_logout:
                logout();

                break;

            default:
                setTitle(menuItem.getTitle());

                if (trips != null && trips.size() > 0)
                    selectedTrip = trips.get(menuItem.getItemId());

                btnAction.setImageResource(R.drawable.ic_send_black_24dp);

                break;
        }
    }

    private void loadTickets() {
        List<Ticket> ticketsList = Ticket.listAll(Ticket.class);
        adapter.clear();

        int idTrip = (selectedTrip != null) ? selectedTrip.getIdTrip() : 0;

        if (!ticketsList.isEmpty()) {
            for (Ticket ticket : ticketsList) {
                if (ticket.getIdTrip() == idTrip)
                    adapter.add(ticket);
            }
        }
    }

    private void synchronizeTickets() {
        if(adapter.getItemCount() > 0) {
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_NoActionBar);
            } else {
                builder = new AlertDialog.Builder(this);
            }

            builder.setTitle(R.string.main_activity_send_tickets)
                    .setMessage(String.format(getString(R.string.main_activity_confirm_send_tickets), selectedTrip.getDestination()))
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            SendTicketsItemsTask sendTicketsItemsTask = new SendTicketsItemsTask();
                            sendTicketsItemsTask.execute();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }

    private void showMoveTickets(final ArrayList<Ticket> tickets) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_NoActionBar);
        } else {
            builder = new AlertDialog.Builder(this);
        }

        final CharSequence[] items = new CharSequence[(selectedTrip == null) ? trips.size() : trips.size() - 1];
        for (int i = 0; i < items.length; i ++) {
            if (selectedTrip != null) {
                if(selectedTrip.getIdTrip() != trips.get(i).getIdTrip())
                    items[i] = trips.get(i).toString();
            }
            else items[i] = trips.get(i).toString();
        }

        builder.setTitle("Mover tickets")
                .setSingleChoiceItems(items, 0, null)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ListView lw = ((AlertDialog)dialog).getListView();

                        moveTickets(tickets, trips.get(lw.getCheckedItemPosition()));
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    private void moveTickets(final ArrayList<Ticket> tickets, final Trip trip) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }

        builder.setTitle(R.string.main_activity_move_tickets)
                .setMessage(String.format(getString(R.string.main_activity_confirm_move_tickets), trip.getDestination()))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        for (Ticket ticket : tickets) {
                            ticket.setIdTrip(trip.getIdTrip());
                            ticket.update();
                        }

                        loadTickets();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private class LoadMenuItemsTask extends AsyncTask<Void, Integer, TripResponse> {
        private String username;

        public LoadMenuItemsTask(String username) {
            this.username = username;
        }

        @Override
        protected TripResponse doInBackground(Void... voids) {
            TripResponse response = RestApi.getInstance().getTripsByUsername(username);

            return  response;
        }

        @Override
        protected void onPostExecute(TripResponse response) {
            NavigationView navigationView = findViewById(R.id.nav_view);
            Menu tripsMenu = navigationView.getMenu().getItem(1).getSubMenu();

            tripsMenu.clear();

            if (response != null && response.getCode() == 0) {
                if (response.getTrips().size() > 0) {
                    trips = response.getTrips();

                    Trip.deleteAll(Trip.class);

                    for (int i = 0; i < trips.size(); i++) {
                        Trip currentTrip = trips.get(i);

                        Trip.save(currentTrip);

                        MenuItem item = tripsMenu.add(0, i, 0, currentTrip.toString());
                        item.setIcon(R.drawable.ic_flight_black_24dp);
                    }
                }
            }
            else {
                if (response != null) {
                    Snackbar.make(findViewById(android.R.id.content), R.string.main_activity_error_fetching_trips, Snackbar.LENGTH_SHORT).show();
                } else {
                    if (Global.isConnected) {
                        Snackbar.make(findViewById(android.R.id.content), R.string.error_no_server_connection, Snackbar.LENGTH_SHORT).show();
                    } else {
                        Snackbar.make(findViewById(android.R.id.content), R.string.error_no_internet, Snackbar.LENGTH_SHORT).show();
                    }
                }

                trips.clear();
                trips.addAll(Trip.listAll(Trip.class));

                for (int i = 0; i < trips.size(); i++) {
                    Trip currentTrip = trips.get(i);

                    MenuItem item = tripsMenu.add(0, i, 0, currentTrip.toString());
                    item.setIcon(R.drawable.ic_flight_black_24dp);
                }
            }
        }
    }

    private class SendTicketsItemsTask extends AsyncTask<Void, Integer, TicketResponse> {

        @Override
        protected void onPreExecute() {
            btnAction.setClickable(false);
        }

        @Override
        protected TicketResponse doInBackground(Void ...voids) {
            TicketResponse response = null;
            boolean errorAlConvertirImagen = false;

            for (Ticket ticket : adapter.getItems()) {
                if (ticket.getImageFile() != null && !ticket.getImageFile().isEmpty()) {
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(Uri.parse(ticket.getImageFile()));
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArray);
                        ticket.setImage(byteArray.toByteArray());

                    } catch (IOException ex) {
                        errorAlConvertirImagen = true;
                        break;
                    }
                }
            }

            if(!errorAlConvertirImagen)
                response = RestApi.getInstance().sendTickets(adapter.getItems());

            return response;
        }

        @Override
        protected void onPostExecute(TicketResponse response) {
            if (response != null && response.getCode() == 0) {
                for (Ticket ticket : adapter.getItems()) {
                    Ticket.delete(ticket);
                }
                adapter.clear();
            }
            else {
                if (response != null) {
                    Snackbar.make(findViewById(android.R.id.content), R.string.main_activity_error_synchronizing_tickets, Snackbar.LENGTH_SHORT).show();
                } else {
                    if (Global.isConnected) {
                        Snackbar.make(findViewById(android.R.id.content), R.string.error_no_server_connection, Snackbar.LENGTH_SHORT).show();
                    } else {
                        Snackbar.make(findViewById(android.R.id.content), R.string.error_no_internet, Snackbar.LENGTH_SHORT).show();
                    }
                }
            }

            btnAction.setClickable(true);
        }
    }

    private void logout()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.main_activity_confirm_logout);
        builder.setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                UserSerializer.getInstance().clear(MainActivity.this);
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton(R.string.dialog_no, null);
        builder.show();
    }

}
