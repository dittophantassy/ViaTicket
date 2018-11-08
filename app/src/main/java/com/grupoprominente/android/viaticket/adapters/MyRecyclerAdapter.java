package com.grupoprominente.android.viaticket.adapters;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.grupoprominente.android.viaticket.R;
import com.grupoprominente.android.viaticket.models.Ticket;

/**
 * Created by FCouzo on 13/7/2018.
 */

public class MyRecyclerAdapter extends MyArrayRecycleAdapter<Ticket,MyRecyclerAdapter.TicketViewHolder> {
    private MyRecyclerAdapterClickListener clickListener;

    protected static class TicketViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyRecyclerAdapterClickListener clickListener;

        TextView tvTitle;
        ImageView imageCategory;

        public TicketViewHolder(View itemView, MyRecyclerAdapterClickListener listener) {
            super(itemView);

            clickListener = listener;
            itemView.setOnClickListener(this);
            tvTitle = itemView.findViewById(R.id.tv_ticket_rv_amount);
            imageCategory = itemView.findViewById(R.id.image_view_ticket_type);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(v, getLayoutPosition());
        }
    }

    public MyRecyclerAdapter(MyRecyclerAdapterClickListener listener) {
        clickListener = listener;
    }

    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ticket_rv_item_new, parent, false);
        return new TicketViewHolder(v, clickListener);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder holder, int position) {
        Ticket t = getItems().get(position);
        holder.tvTitle.setText(t.getAmount().toString());

        Drawable ticketBackground = holder.imageCategory.getBackground();

        if (t.getTicketType() != null) {
            switch (t.getTicketType()) {
                case TAXI:
                    holder.imageCategory.setImageResource(R.drawable.ic_local_taxi_black_24dp);
                    holder.imageCategory.setBackgroundResource(R.drawable.ic_ticket_circle_background_taxi);
                    //DrawableCompat.setTint(ticketBackground, R.color.color_bg_taxi);

                    break;

                case FOOD:
                    holder.imageCategory.setImageResource(R.drawable.ic_restaurant_black_24dp);
                    holder.imageCategory.setBackgroundResource(R.drawable.ic_ticket_circle_background_food);
                    break;

                case LODGING:
                    holder.imageCategory.setImageResource(R.drawable.ic_train_black_24dp);
                    holder.imageCategory.setBackgroundResource(R.drawable.ic_ticket_circle_background_lodging);

                    break;

                case TRANSPORT:
                    holder.imageCategory.setImageResource(R.drawable.ic_train_black_24dp);
                    holder.imageCategory.setBackgroundResource(R.drawable.ic_ticket_circle_background_transport);

                    break;

                case OTHER:
                    holder.imageCategory.setImageResource(R.drawable.ic_train_black_24dp);
                    holder.imageCategory.setBackgroundResource(R.drawable.ic_ticket_circle_background_other);

                    break;

                default:
                    holder.imageCategory.setImageResource(R.drawable.ic_reorder_black_24dp);
                    holder.imageCategory.setBackgroundResource(R.drawable.ic_ticket_circle_background_other);

                    break;
            }

        }
    }

}
