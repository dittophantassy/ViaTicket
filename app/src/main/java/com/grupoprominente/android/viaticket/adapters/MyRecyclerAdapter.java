package com.grupoprominente.android.viaticket.adapters;

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
        ImageView tvSubtitle;

        public TicketViewHolder(View itemView, MyRecyclerAdapterClickListener listener) {
            super(itemView);

            clickListener = listener;
            itemView.setOnClickListener(this);
            tvTitle = itemView.findViewById(R.id.tv_ticket_rv_title);
            tvSubtitle = itemView.findViewById(R.id.iv_ticket_rv_image);
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ticket_rv_item, parent, false);
        return new TicketViewHolder(v, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder holder, int position) {
        Ticket t = getItems().get(position);
        holder.tvTitle.setText(t.getAmount().toString());
        //Glide.with(holder.tvSubtitle.getContext()).load(t.getUrlLogo()).into(holder.tvSubtitle);
        //holder.tvSubtitle.setText(String.valueOf(t.getAmount()));
    }

}
