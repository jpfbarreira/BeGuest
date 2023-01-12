package com.example.beguest.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beguest.CreateEventFragments.Event;
import com.example.beguest.EventActivity;
import com.example.beguest.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventAdapterViewHolder> {
    Context context;
    ArrayList<Event> events;

    public EventAdapter(Context context, ArrayList<Event> events) {
        this.context = context;
        this.events = events;
    }

    @NonNull
    @Override
    public EventAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.event_card, parent, false);

        return new EventAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventAdapterViewHolder holder, int position) {
        Event event = events.get(position);
        holder.eventTitle.setText(event.getTitle());
        String location = event.getLocation();
        String country = location.split(",")[2];
        String cityWithAdd = location.split(",")[1];
        String city = cityWithAdd.split(" ")[2];
        holder.eventLocation.setText(country + ", " + city);

        //date
        String[] months = new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy");
        try {
            Date eventDate = simpleDateFormat.parse(event.date);
            String eventDay = String.valueOf(eventDate.getDate());
            int eventMonth = eventDate.getMonth();

            holder.eventDate.setText(eventDay + " " +  months[eventMonth]);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        //image
        Drawable photo = context.getResources().getDrawable(event.eventPhoto);

        holder.photoEventCard.setBackground(photo);

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
        return events.size();
    }

    public static class EventAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView eventTitle, eventLocation, eventDate;
        CardView eventCard;
        ImageView photoEventCard;

        public EventAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            photoEventCard = itemView.findViewById(R.id.event_card_photo);
            eventTitle = itemView.findViewById(R.id.card_event_tile);
            eventLocation = itemView.findViewById(R.id.card_event_location);
            eventDate = itemView.findViewById(R.id.card_event_date);
            eventCard = itemView.findViewById(R.id.event_card);

        }
    }
}
