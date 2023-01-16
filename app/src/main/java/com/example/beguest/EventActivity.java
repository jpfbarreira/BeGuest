package com.example.beguest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.beguest.Adapters.EventAdapter;
import com.example.beguest.Adapters.HomeEventsAdapter;
import com.example.beguest.Adapters.RegisteredUsersAdapter;
import com.example.beguest.CreateEventFragments.Event;
import com.example.beguest.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class EventActivity extends AppCompatActivity {
    private ImageView backbtn, editEventBtn, first_person_photo, second_person_photo, third_person_photo;
    private MaterialButton interestedBtn;
    private Boolean isInterested;
    private TextView eventTitleTextView, eventDescriptionTextView, eventLocationTextView, eventDateTextView, users_registered, eventMaxPeopleTextView;
    private CardView users_registered_cardView;
    private DatabaseReference reference;
    private TextView checkInBtn;

    private RecyclerView recyclerView;
    public static RegisteredUsersAdapter userAdpter;


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
        recyclerView = findViewById(R.id.users_recycle_view);
        eventMaxPeopleTextView = findViewById(R.id.event_max_people);
        //check in btn
        checkInBtn = findViewById(R.id.check_in_btn);

        users_registered = findViewById(R.id.users_registered);
        users_registered_cardView = findViewById(R.id.users_registered_cardView);
        //setTexts
        eventTitleTextView.setText(event.title);
        eventDescriptionTextView.setText(event.description);
        eventLocationTextView.setText(event.location);
        if (event.maxPeople != ""){
            eventMaxPeopleTextView.setText(String.valueOf(event.maxPeople));
            eventMaxPeopleTextView.setVisibility(View.VISIBLE);
        }

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
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        reference = FirebaseDatabase.getInstance("https://beguest-4daae-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("Registered Events");
        DatabaseReference eventReference = reference.child(eventId);
        DatabaseReference registeredUsersRef = eventReference.child("Registered Users");

        registeredUsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                registeredUsers.clear();
                registeredUsers.add(event.creatorId);

                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    String userId = String.valueOf(dataSnapshot.getValue(String.class));
                    registeredUsers.add(userId);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                    if(registeredUsers.size()>3){
                        ArrayList<String> list = new ArrayList<>();
                        for(int i = 0; i < 3; i++){
                            list.add(registeredUsers.get(i));
                        }

                        userAdpter = new RegisteredUsersAdapter(getApplicationContext(), list);
                        recyclerView.setAdapter(userAdpter);

                        users_registered_cardView.setVisibility(View.VISIBLE);
                        users_registered.setVisibility(View.VISIBLE);
                        String numberOfUsers = String.valueOf(registeredUsers.size() - 3);
                        users_registered.setText("+" + numberOfUsers);

                    } else {
                        users_registered_cardView.setVisibility(View.INVISIBLE);
                        users_registered.setVisibility(View.INVISIBLE);
                        userAdpter = new RegisteredUsersAdapter(getApplicationContext(), registeredUsers);
                        recyclerView.setAdapter(userAdpter);
                    }

                    if (userId.equals(currentUser.getUid()) || event.creatorId.equals(currentUser.getUid())){
                        isInterested = true;
                        interestedBtn.setIcon(getResources().getDrawable(R.drawable.ic_baseline_star_24));
                    }
                }
                if (registeredUsers.size() == 1){
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                    userAdpter = new RegisteredUsersAdapter(getApplicationContext(), registeredUsers);
                    recyclerView.setAdapter(userAdpter);
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

                    for(int i = 0; i < registeredUsers.size(); i++){
                        if(Objects.equals(registeredUsers.get(i), currentUser.getUid())){
                            registeredUsers.remove(i);
                            userAdpter.notifyDataSetChanged();

                        };
                    }

                    interestedBtn.setIcon(getResources().getDrawable(R.drawable.ic_baseline_star_border_24));
                }
            }
        });

        if (Objects.equals(event.creatorId, currentUser.getUid())){
            interestedBtn.setIcon(getResources().getDrawable(R.drawable.ic_baseline_star_24));
            interestedBtn.setClickable(false);

            if (checkInBtn.getVisibility() != View.VISIBLE){
                editEventBtn.setVisibility(View.VISIBLE);
            }else{
                editEventBtn.setVisibility(View.INVISIBLE);
            }
        }else {
            interestedBtn.setIcon(getResources().getDrawable(R.drawable.ic_baseline_star_border_24));
        }

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        users_registered_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(registeredUsers.size()>3) {
                    Intent intent1 = new Intent(EventActivity.this, RegisteredUsersListActivity.class);
                    intent1.putExtra("usersRegistered", registeredUsers);
                    startActivity(intent1);
                }
            }
        });

        checkInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventActivity.this, DancingActivity.class);
                startActivity(intent);
            }
        });
    }
}