package com.example.beguest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.beguest.Adapters.HomeEventsAdapter;
import com.example.beguest.CreateEventFragments.Event;
import com.example.beguest.ui.home.HomeFragment;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class EventActivity extends AppCompatActivity {
    private ImageView backbtn, editEventBtn;
    private MaterialButton interestedBtn;
    private Boolean isInterested;
    private TextView eventTitleTextView, eventDescriptionTextView, eventLocationTextView, eventDateTextView;

    private DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        isInterested = false;
        Intent intent = getIntent();
        Event event = (Event) intent.getSerializableExtra("Event");
        String eventId = intent.getExtras().getString("EventId");
        Log.d("event id", eventId);

        backbtn = findViewById(R.id.back_toAtivity_btn);
        interestedBtn = findViewById(R.id.interested_btn);
        eventTitleTextView = findViewById(R.id.event_title);
        eventDescriptionTextView = findViewById(R.id.event_description);
        eventLocationTextView = findViewById(R.id.event_location);
        eventDateTextView = findViewById(R.id.event_time);
        editEventBtn = findViewById(R.id.edit_event_btn);

        //setTexts
        eventTitleTextView.setText(event.title);
        eventDescriptionTextView.setText(event.description);
        eventLocationTextView.setText(event.location);

        //Date
        String[] months = new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy");
        try {
            Date eventDate = simpleDateFormat.parse(event.date);
            String eventDay = String.valueOf(eventDate.getDate());
            int eventMonth = eventDate.getMonth();
            String eventTime = event.time;

            String hour = eventTime.split(":")[0];

            if(Integer.parseInt(hour) > 12){
                eventDateTextView.setText(eventDay + " " +  months[eventMonth]
                        + ", " + eventTime + " " + "PM");
            }else {
                eventDateTextView.setText(eventDay + " " +  months[eventMonth]
                        + ", " + eventTime + " " + "AM");
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        //DataBase
        ArrayList<String> registeredUsers = new ArrayList<>();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        reference = FirebaseDatabase.getInstance("https://beguest-4daae-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("Registered Events");
        DatabaseReference eventReference = reference.child(eventId);
        DatabaseReference registeredUsersRef = eventReference.child("Registered Users");

        Log.d("EventCreator", event.creatorId);

        registeredUsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                registeredUsers.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    String userId = String.valueOf(dataSnapshot.getValue(String.class));
                    Log.d("USERID", userId);
//                    Log.d("USERID", currentUser.getUid());

                    if (userId.equals(currentUser.getUid()) || event.creatorId.equals(currentUser.getUid())){
                        Log.d("USERID", currentUser.getUid());
                        isInterested = true;
                        interestedBtn.setIcon(getResources().getDrawable(R.drawable.ic_baseline_star_24));
                    }else {
                        isInterested = false;
                        interestedBtn.setIcon(getResources().getDrawable(R.drawable.ic_baseline_star_border_24));
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //onclicks
        interestedBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {
                if (!isInterested){
                    isInterested = true;
                    interestedBtn.setIcon(getResources().getDrawable(R.drawable.ic_baseline_star_24));
//                    HashMap<String, Object> values = new HashMap<>();
//                    values.put("Registered Users",  String.valueOf(currentUser.getUid()));
                    registeredUsersRef.child(currentUser.getUid()).setValue(currentUser.getUid());

                }else {
                    isInterested = false;
                    registeredUsersRef.child(currentUser.getUid()).removeValue();
                    interestedBtn.setIcon(getResources().getDrawable(R.drawable.ic_baseline_star_border_24));
                }
            }
        });

        if (Objects.equals(event.creatorId, currentUser.getUid())){
            interestedBtn.setIcon(getResources().getDrawable(R.drawable.ic_baseline_star_24));
            interestedBtn.setClickable(false);
            editEventBtn.setVisibility(View.VISIBLE);
        }else {
            interestedBtn.setIcon(getResources().getDrawable(R.drawable.ic_baseline_star_border_24));
        }

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}