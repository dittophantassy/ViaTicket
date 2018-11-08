package com.grupoprominente.android.viaticket.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
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
import com.grupoprominente.android.viaticket.data.api.RestApi;
import com.grupoprominente.android.viaticket.models.CurrencyType;
import com.grupoprominente.android.viaticket.models.Ticket;
import com.grupoprominente.android.viaticket.models.TicketType;
import com.grupoprominente.android.viaticket.models.Trip;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MyRecyclerAdapter adapter;
    private DrawerLayout mDrawerLayout;
    private MenuItem mPreviousMenuItem;
    private ImageButton btnAction;
    private ProgressBar pbMain;
    private NavigationView navigationView;

    private ArrayList<Trip> trips;
    private Trip selectedTrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.rv_tickets);
        //pbMain = findViewById(R.id.pbMain);

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
                                SharedPreferences sharedPref = MainActivity.this.getSharedPreferences(getString(R.string.session_shared_preferences), Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.remove(getString(R.string.session_logged_user_key));
                                editor.commit();

                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);

                                startActivity(intent);
                                finish();

                                break;

                            default:
                                Toolbar toolbar = findViewById(R.id.toolbar);
                                toolbar.setTitle(menuItem.getTitle());

                                if (trips != null && trips.size() > 0)
                                    selectedTrip = trips.get(menuItem.getItemId());

                                btnAction.setImageResource(R.drawable.ic_send_black_24dp);

                                break;
                        }

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

                /*final CharSequence[] items = new CharSequence[(selectedTrip == null) ? trips.size() : trips.size() - 1];
                for (int i = 0; i < items.length; i ++) {
                    if (selectedTrip != null) {
                        if(selectedTrip.getIdTrip() != trips.get(i).getIdTrip())
                            items[i] = trips.get(i).toString();
                    }
                    else items[i] = trips.get(i).toString();
                }*/

                builder.setTitle("Eliminar Ticket?")
                        //.setSingleChoiceItems(items, 0, null)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Ticket t = adapter.getItems().get(position);
                                t.delete();
                                
                                LoadMenuItemsTask loadMenuItemsTask = new LoadMenuItemsTask("dperalta");
                                loadMenuItemsTask.execute();

                                loadTickets();
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
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

                        startActivity(intent);
                    }
                }
        );

        navigationView.getMenu().getItem(0).setChecked(true);
    }


    @Override
    protected void onResume() {
        super.onResume();

        LoadMenuItemsTask loadMenuItemsTask = new LoadMenuItemsTask("dperalta");
        loadMenuItemsTask.execute();

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


    private class LoadMenuItemsTask extends AsyncTask<Void, Integer, ArrayList<Trip>> {
        private String username;

        public LoadMenuItemsTask(String username) {
            this.username = username;
        }


        @Override
        protected ArrayList<Trip> doInBackground(Void... voids) {
            trips = RestApi.getInstance().getTripsByUsername(username);

            return trips;
        }

        @Override
        protected void onPostExecute(ArrayList<Trip> tripsByCurrentUser) {
            NavigationView navigationView = findViewById(R.id.nav_view);
            Menu tripsMenu = navigationView.getMenu().getItem(1).getSubMenu();

            if (tripsByCurrentUser.size() > 0) {
                tripsMenu.clear();
                for (int i = 0; i < tripsByCurrentUser.size(); i++) {
                    Trip currentTrip = tripsByCurrentUser.get(i);
                    MenuItem item = tripsMenu.add(0, i, 0, currentTrip.toString());
                    item.setIcon(R.drawable.ic_flight_black_24dp);
                }
            }
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

            builder.setTitle("Rendir tickets")
                    .setMessage(String.format("¿Confirma rendición de tickets al viaje %s?", selectedTrip.getDestination()))
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            SendTicketsItemsTask sendTicketsItemsTask = new SendTicketsItemsTask();
                            sendTicketsItemsTask.execute();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
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
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                .show();
    }

    private void moveTickets(final ArrayList<Ticket> tickets, final Trip trip) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_NoActionBar);
        } else {
            builder = new AlertDialog.Builder(this);
        }

        builder.setTitle("Mover tickets")
                .setMessage(String.format("¿Confirma mover tickets al viaje %s?", trip.getDestination()))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        for (Ticket ticket : tickets) {
                            ticket.setIdTrip(trip.getIdTrip());
                            ticket.update();
                        }

                        loadTickets();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private class SendTicketsItemsTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            btnAction.setClickable(false);
            //recyclerView.setVisibility(View.GONE);
          //  pbMain.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            RestApi.getInstance().sendTickets(adapter.getItems());

            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            btnAction.setClickable(true);

            for (Ticket ticket : adapter.getItems()) {
                Ticket.delete(ticket);
            }

            adapter.clear();

            //recyclerView.setVisibility(View.VISIBLE);
          //  pbMain.setVisibility(View.GONE);
        }
    }

}
