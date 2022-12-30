package com.example.beguest.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beguest.CreateEventFragments.Event;
import com.example.beguest.EventActivity;
import com.example.beguest.R;

import java.util.ArrayList;

public class HomeEventsAdapter extends RecyclerView.Adapter<HomeEventsAdapter.HomeEventsAdapterViewHolder>{
        Context context;
        ArrayList<Event> events;


    public HomeEventsAdapter(Context context, ArrayList<Event> events) {
            this.context = context;
            this.events = events;
            }

    @NonNull
    @Override
    public HomeEventsAdapter.HomeEventsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_event_card, parent, false);

        return new HomeEventsAdapter.HomeEventsAdapterViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull HomeEventsAdapter.HomeEventsAdapterViewHolder holder, int position) {
        Event event = events.get(position);

        holder.eventTitle.setText(event.getTitle());



        holder.eventCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EventActivity.class);
                intent.putExtra("Event", event);
                intent.putExtra("EventId", event.eventID);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        Log.d("Events size", String.valueOf(events.size()));
        return events.size();

    }

    public static class HomeEventsAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView eventTitle, eventTime, eventDate;
        CardView eventCard;

        public HomeEventsAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            eventTitle = itemView.findViewById(R.id.card_event_tile);
            eventCard = itemView.findViewById(R.id.card_event);
        }


    }
}
