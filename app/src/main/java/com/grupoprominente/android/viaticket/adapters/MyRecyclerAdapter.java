package com.grupoprominente.android.viaticket.adapters;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
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

    protected static class TicketViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private MyRecyclerAdapterClickListener clickListener;

        View view;

        TextView txtCategory;
        TextView txtAmount;
        TextView txtIssueDate;
        ImageView imageCategory;

        android.text.format.DateFormat dateFormat;

        public TicketViewHolder(View itemView, MyRecyclerAdapterClickListener listener) {
            super(itemView);

            view = itemView;


            clickListener = listener;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            itemView.setLongClickable(true);
            imageCategory = itemView.findViewById(R.id.tv_ticket_imgCategory);
            txtCategory = itemView.findViewById(R.id.tv_ticket_txtCategory);
            txtAmount = itemView.findViewById(R.id.tv_ticket_txtAmount);
            txtIssueDate = itemView.findViewById(R.id.tv_ticket_txtIssueDate);

            dateFormat = new android.text.format.DateFormat();
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(v, getLayoutPosition());
        }

        @Override
        public boolean onLongClick(View v){
            clickListener.onItemLongClick(v, getLayoutPosition());
            return true;
        }
    }

    public MyRecyclerAdapter(MyRecyclerAdapterClickListener listener) {
        clickListener = listener;
    }

    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ticket_rv_item, parent, false);
        return new TicketViewHolder(v, clickListener);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder holder, int position) {
        Ticket t = getItems().get(position);
        if (t.getTicketType()!=null)
            holder.txtCategory.setText(t.getTicketType().getResource());
        if (t.getIssueDate()!=null)
            holder.txtIssueDate.setText(holder.dateFormat.format("dd/MM", t.getIssueDate()));
        switch (t.getCurrency())
        {
            case PESO: holder.txtAmount.setText(holder.view.getContext().getString(R.string.currency_peso_format, t.getAmount()));
                break;
            case DOLLAR: holder.txtAmount.setText(holder.view.getContext().getString(R.string.currency_dollar_format, t.getAmount()));
                break;
            default:  holder.txtAmount.setText(t.getAmount().toString());
        }

        if (t.getTicketType() != null) {
            switch (t.getTicketType()) {
                case TAXI:
                    holder.imageCategory.setImageResource(R.drawable.ic_local_taxi_black_24dp);

                    break;

                case FOOD:
                    holder.imageCategory.setImageResource(R.drawable.ic_restaurant_black_24dp);

                    break;

                case LODGING:
                    holder.imageCategory.setImageResource(R.drawable.ic_hotel_black_24dp);

                    break;

                case TRANSPORT:
                    holder.imageCategory.setImageResource(R.drawable.ic_train_black_24dp);

                    break;

                case OTHER:
                    holder.imageCategory.setImageResource(R.drawable.ic_redeem_black_24dp);

                    break;

                default:
                    holder.imageCategory.setImageResource(R.drawable.ic_redeem_black_24dp);

                    break;
            }

        }
    }

}
