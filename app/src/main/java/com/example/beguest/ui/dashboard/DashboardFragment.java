package com.example.beguest.ui.dashboard;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beguest.CreateEventFragments.Event;
import com.example.beguest.Adapters.EventAdapter;
import com.example.beguest.CreateNewEvent;
import com.example.beguest.R;
import com.example.beguest.databinding.FragmentDashboardBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    private RecyclerView recyclerView;
    private EventAdapter eventAdpter;
    private DatabaseReference reference;
    private ArrayList<Event> events;

    private ImageView danceParty, discoParty, karaokeParty, raveParty;

    private ImageView filterBtn;
    private ImageView newEventBtn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        filterBtn = root.findViewById(R.id.filter_btn);
        newEventBtn = root.findViewById(R.id.add_new_event_btn);
        recyclerView = root.findViewById(R.id.events_recycle_view);
        danceParty = root.findViewById(R.id.dance_party_img);
        discoParty = root.findViewById(R.id.disco_party_img);
        karaokeParty = root.findViewById(R.id.karaoke_party_img);
        raveParty = root.findViewById(R.id.rave_party_img);


        reference = FirebaseDatabase.getInstance("https://beguest-4daae-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("Registered Events");

        events = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        eventAdpter = new EventAdapter(this.getContext(), events);
        recyclerView.setAdapter(eventAdpter);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                events.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    String eventID = dataSnapshot.getKey();

                    Event event = dataSnapshot.getValue(Event.class);
                    event.setEventID(eventID);

                    try {
                        Date todayDate = new Date();
                        String todayDateString = todayDate.toString();
                        SimpleDateFormat inputFormat = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy");
                        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yy");
                        todayDate = inputFormat.parse(todayDateString);
                        String eventDateString = event.getDate().toString();
                        Date eventDate = new SimpleDateFormat("dd/MM/yy").parse(eventDateString);

                        int compare = todayDate.compareTo(eventDate);

                        Log.d("eventDate", "eventDate is: " + eventDate);
                        Log.d("todayDate", "todayDate is: " + todayDate);

                        if(compare < 0) {
                            Log.d("compare", "compare < 0");
                            event.setEventID(eventID);

                            if(!events.contains(event)){
                                events.add(event);
                            }
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                eventAdpter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFilter();
            }
        });

        newEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(root.getContext(), CreateNewEvent.class);
                startActivity(intent);
            }
        });

        partyTypeClickListener(danceParty);
        partyTypeClickListener(raveParty);
        partyTypeClickListener(discoParty);
        partyTypeClickListener(karaokeParty);


        return root;
    }

    private void partyTypeClickListener(ImageView img){
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ColorStateList color = img.getBackgroundTintList();

                if( color == ColorStateList.valueOf(Color.parseColor("#1B1B1B"))){
                    img.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#5C0202")));
                }else {
                    img.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#1B1B1B")));
                }
            }
        });

    }


    private void showFilter(){
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.filter_events_layout);

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, 1100);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.FilterAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}