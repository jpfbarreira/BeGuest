package com.example.beguest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.beguest.CreateEventFragments.Create_Event_Fragment1;
import com.example.beguest.CreateEventFragments.Create_Event_Fragment2;
import com.example.beguest.CreateEventFragments.Create_Event_Fragment3;
import com.example.beguest.CreateEventFragments.Create_Event_ViewModel;
import com.example.beguest.CreateEventFragments.Event;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.kofigyan.stateprogressbar.StateProgressBar;
import com.kofigyan.stateprogressbar.components.StateItem;
import com.kofigyan.stateprogressbar.listeners.OnStateItemClickListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class CreateNewEvent extends AppCompatActivity {

    static List<String> fragments = new ArrayList<String>();
    public Button nextbtn, back_btn, createEventbtn;
    private ImageView backToActivityBtn;

    private StorageReference storageReference;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;

    private Create_Event_ViewModel createEventViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_event);

        createEventViewModel = new ViewModelProvider(this).get(Create_Event_ViewModel.class);

        StateProgressBar stateProgressBar = (StateProgressBar) findViewById(R.id.step_view);

        nextbtn = findViewById(R.id.next_btn);
        back_btn = findViewById(R.id.back_btn);
        backToActivityBtn = findViewById(R.id.back_toAtivity_btn);
        createEventbtn = findViewById(R.id.create_new_event);

        Fragment fragment1 = new Create_Event_Fragment1();
        Fragment fragment2 = new Create_Event_Fragment2();
        Fragment fragment3 = new Create_Event_Fragment3();
        FragmentReplacer(fragment1);


        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fragment1.isVisible()){
                    FragmentReplacer(fragment2);
                    back_btn.setVisibility(View.VISIBLE);
                    createEventbtn.setVisibility(View.INVISIBLE);
                    stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
                }else if(fragment2.isVisible()){
                    FragmentReplacer(fragment3);
                    nextbtn.setVisibility(View.INVISIBLE);
                    createEventbtn.setVisibility(View.VISIBLE);

                    stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
                }
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fragment2.isVisible()){
                    FragmentReplacer(fragment1);
                    back_btn.setVisibility(View.INVISIBLE);
                    stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);
                }else if(fragment3.isVisible()){
                    FragmentReplacer(fragment2);
                    nextbtn.setVisibility(View.VISIBLE);
                    createEventbtn.setVisibility(View.INVISIBLE);
                    stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
                }
            }
        });

        backToActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        stateProgressBar.setOnStateItemClickListener(new OnStateItemClickListener() {
            @Override
            public void onStateItemClick(StateProgressBar stateProgressBar, StateItem stateItem, int stateNumber, boolean isCurrentState) {
                if(stateNumber == 1){
                    stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);
                    FragmentReplacer(fragment1);
                } else if(stateNumber == 2){
                    stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
                    FragmentReplacer(fragment2);
                }else if(stateNumber == 3){
                    stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
                    FragmentReplacer(fragment3);
                }
            }
        });
    }

    private void FragmentReplacer(Fragment fragment) {
        String backStateName = fragment.getClass().getName();
        FragmentManager manager = getSupportFragmentManager();
        //fragment not in back stack, create it.
        FragmentTransaction ft = manager.beginTransaction();
        if (!fragments.contains(backStateName)) {
            // ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            // ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            ft.replace(R.id.frame_layout, fragment);
            ft.addToBackStack(backStateName);
            ft.commit();
            fragments.add(backStateName);
            Log.d("backStateName", String.valueOf(fragments));
        } else {
            ft.replace(R.id.frame_layout, fragment, String.valueOf(fragment.getId()));
            ft.commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            getFragmentManager().popBackStack();
        }
    }

    public void registerEvent(String name, String date, String description, String time,
                              String minAge, String maxPeople, String minPoints, String privacy, String location) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance("https://beguest-4daae-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("Registered Events");

        DatabaseReference userReference = FirebaseDatabase.getInstance("https://beguest-4daae-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("Registered Users");

        ArrayList<String> registeredUserIDs = new ArrayList<String>();
        //event photo;
        ArrayList<Integer> photos = new ArrayList<>();
        int photo1 = R.drawable.photo_events1;
        int photo2 = R.drawable.photo_events2;
        int photo3 = R.drawable.photo_events3;
        int photo4 = R.drawable.photo_events4;
        int photo5 = R.drawable.photo_events5;
        int photo6 = R.drawable.photo_events6;
        int photo7 = R.drawable.photo_events7;
        int photo8 = R.drawable.photo_events8;
        int photo9 = R.drawable.photo_events9;
        int photo10 =R.drawable.photo_events10;
        photos.add(photo1);photos.add(photo2);photos.add(photo3);photos.add(photo4);photos.add(photo5);photos.add(photo6);
        photos.add(photo7);photos.add(photo8);photos.add(photo9);photos.add(photo10);

        int random = new Random().nextInt(photos.size());
        int eventPhoto = photos.get(random);

        String eventID = reference.push().getKey();
        Log.d("EVENT",eventID);

        Event event = new Event(user.getUid(), registeredUserIDs, name, date, description, time, minAge, maxPeople
                , minPoints, privacy, location, eventPhoto);


        reference.child(eventID).setValue(event).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(CreateNewEvent.this, "Event Created Successfully", Toast.LENGTH_SHORT).show();

                    String eventId = reference.child(user.getUid()).getKey();
                    //start new activity;
                    onBackPressed();
                }else {
                    Toast.makeText(CreateNewEvent.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    Log.d("Error", task.getException().getMessage());
                }
            }
        });


    }

}