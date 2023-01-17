package com.example.beguest.CreateEventFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.beguest.CreateNewEvent;
import com.example.beguest.R;

import java.util.ArrayList;
import java.util.Objects;

public class Create_Event_Fragment3 extends Fragment {
    View view;

    private Create_Event_ViewModel createEventViewModel;
    private TextView eventName, eventDescription, eventDate, eventTime, eventMaxPeople, eventMinPoints, eventMinAge, eventPrivacy, eventLocation;
    private String name, date, description, time, minAge, maxPeople, privacy, location;
    private TextView eventDescriptionLabel, eventMinPointsLabel, eventMinAgeLabel;
    private Button createEventbtn;
    private ArrayList<String> arrayTags;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_create__event_3, container, false);

        createEventViewModel = new ViewModelProvider(getActivity()).get(Create_Event_ViewModel.class);

        createEventbtn = getActivity().findViewById(R.id.create_new_event);

        eventName = view.findViewById(R.id.event_name);
        eventDescription = view.findViewById(R.id.event_description);
        eventDate = view.findViewById(R.id.event_date);
        eventTime = view.findViewById(R.id.event_time);
        eventMaxPeople = view.findViewById(R.id.event_max_people);
        eventMinPoints = view.findViewById(R.id.event_min_points);
        eventMinAge = view.findViewById(R.id.event_min_age);
        eventPrivacy = view.findViewById(R.id.event_privacy);
        eventLocation = view.findViewById(R.id.event_location);

        //labels
        eventDescriptionLabel = view.findViewById(R.id.textView_description);
        eventMinPointsLabel = view.findViewById(R.id.textView13);
        eventMinAgeLabel = view.findViewById(R.id.textView14);


        createEventViewModel.getEventName().observe(getViewLifecycleOwner(), item -> {
            name = item;
            eventName.setText(item);
        });
        createEventViewModel.getEventDate().observe(getViewLifecycleOwner(), item -> {
            date = item;
            eventDate.setText(item);
        });
        createEventViewModel.getEventTime().observe(getViewLifecycleOwner(), item -> {
            time = item;
            eventTime.setText(item);
        });
        createEventViewModel.getEventPrivacy().observe(getViewLifecycleOwner(), item -> {
            privacy = item;
            eventPrivacy.setText(item);
        });
        createEventViewModel.getEventLocation().observe(getViewLifecycleOwner(), item -> {
            location = item;
            eventLocation.setText(item);
            Log.d("yoooh", item);
        });

        //not mandatory
        createEventViewModel.getEventDescription().observe(getViewLifecycleOwner(), item -> {
            description = item;
            if (!TextUtils.isEmpty(item)){
                eventDescription.setText(item);
            }else {
                eventDescription.setText("No Description");
            }
        });
        createEventViewModel.getEventMinAge().observe(getViewLifecycleOwner(), item -> {
            minAge = item;
            if (!TextUtils.isEmpty(item)){
                eventMinAge.setText(item);
            }else {
                eventMinAge.setText("No Minimum Age");
            }
        });
        createEventViewModel.getEventMaxPeople().observe(getViewLifecycleOwner(), item -> {
            maxPeople = item;
            if (!TextUtils.isEmpty(item)){
                eventMaxPeople.setText(item);
            }else {
                eventMaxPeople.setVisibility(View.GONE);
            }
        });
        createEventViewModel.getEventTags().observe(getViewLifecycleOwner(), item -> {
            arrayTags = item;
            if (arrayTags.size() != 0){
                for (int i = 0; i < item.size(); i++){
                    if (arrayTags.size() == 1){
                        eventMinPoints.setText(item.get(i));
                    }else {
                        String text = (String) eventMinPoints.getText();
                        eventMinPoints.setText(text + item.get(i) + ", ");

                    }
                }
            }else {
                eventMinPoints.setText("No Tags");
            }
            Log.d("MYARRAR", String.valueOf(arrayTags));
        });

        createEventbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((CreateNewEvent)getActivity()).registerEvent(name, date, description, time, minAge, maxPeople, privacy, location, arrayTags);
            }
        });

        return view;
    }


}