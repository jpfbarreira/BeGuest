package com.example.beguest.CreateEventFragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.example.beguest.CreateNewEvent;
import com.example.beguest.R;
import com.example.beguest.SharedViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class Create_Event_Fragment1 extends Fragment {
    private View view;

    private TextInputEditText eventNameTextView, eventDateTextView,eventDescriptionTextView, eventTimeTextView;
    private TextInputEditText eventMinAgeTextView, eventMaxPeopleTextView, eventMinPointsTextView;
    private String eventName, eventDate, eventDescription, eventTime, eventMinAge, eventMaxPeople, eventMinPoints, eventPrivacy;
    private SwitchCompat switchPrivacyBtn;
    private Button saveDataBtn;

    private Create_Event_ViewModel createEventViewModel;

    final Calendar calendar= Calendar.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_create__event_1, container, false);

        createEventViewModel = new ViewModelProvider(getActivity()).get(Create_Event_ViewModel.class);

        eventNameTextView = view.findViewById(R.id.event_name);
        eventDateTextView = view.findViewById(R.id.event_date);
        eventDescriptionTextView = view.findViewById(R.id.event_description);
        eventTimeTextView = view.findViewById(R.id.event_time);
        eventMinAgeTextView = view.findViewById(R.id.event_minimum_age);
        eventMaxPeopleTextView = view.findViewById(R.id.event_max_people);
        eventMinPointsTextView = view.findViewById(R.id.event_minimum_points);

        switchPrivacyBtn = view.findViewById(R.id.event_privacy);

        saveDataBtn = view.findViewById(R.id.save_data_btn);
        Button nextbtn = ((CreateNewEvent)getActivity()).nextbtn;

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                nextbtn.setVisibility(View.INVISIBLE);
                saveDataBtn.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        eventNameTextView.addTextChangedListener(textWatcher);
        eventDateTextView.addTextChangedListener(textWatcher);
        eventDescriptionTextView.addTextChangedListener(textWatcher);
        eventTimeTextView.addTextChangedListener(textWatcher);
        eventMinAgeTextView.addTextChangedListener(textWatcher);
        eventMaxPeopleTextView.addTextChangedListener(textWatcher);
        eventMinPointsTextView.addTextChangedListener(textWatcher);


        switchPrivacyBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    switchPrivacyBtn.setText("Private");
                }else {
                    switchPrivacyBtn.setText("Public");
                }
            }
        });

        eventName = eventNameTextView.getText().toString();


        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,day);
                eventDate();
            }
        };

        eventDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(),R.style.my_dialog_theme, date, calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        eventTimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker();
            }
        });

        saveDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //here make if to make mandatory fields
                eventName = eventNameTextView.getText().toString();
                eventDate =  eventDateTextView.getText().toString();
                eventDescription = eventDescriptionTextView.getText().toString();
                eventTime = eventTimeTextView.getText().toString();
                eventMinAge = eventMinAgeTextView.getText().toString();
                eventMaxPeople = eventMaxPeopleTextView.getText().toString();
                eventMinPoints = eventMinPointsTextView.getText().toString();
                eventPrivacy = (String) switchPrivacyBtn.getText();

                createEventViewModel.setData(eventName, eventDate, eventDescription, eventTime, eventMinAge,
                        eventMaxPeople,
                        eventMinPoints,
                        eventPrivacy);
                nextbtn.setVisibility(View.VISIBLE);
                saveDataBtn.setVisibility(View.INVISIBLE);
            }
        });

        return view;
    }
    private void eventDate(){
        String myFormat="dd/MM/yy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.UK);
        eventDate = dateFormat.format(calendar.getTime());
        eventDateTextView.setText(dateFormat.format(calendar.getTime()));
    }

    private void showTimePicker(){
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), R.style.MyTimePickerWidgetStyle ,new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                eventTimeTextView.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
            }
        }, hour, minute, true);
        timePickerDialog.show();
    }
}