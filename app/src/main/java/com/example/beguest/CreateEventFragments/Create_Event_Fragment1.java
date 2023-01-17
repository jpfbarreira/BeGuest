package com.example.beguest.CreateEventFragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.beguest.CreateNewEvent;
import com.example.beguest.R;
import com.example.beguest.SharedViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerView;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Create_Event_Fragment1 extends Fragment {
    private View view;

    private TextInputEditText eventNameTextView, eventDateTextView, eventDescriptionTextView, eventTimeTextView;
    private TextInputEditText eventMinAgeTextView, eventMaxPeopleTextView, eventTagsTextView;
    private String eventName, eventDate, eventDescription, eventTime, eventMinAge, eventMaxPeople, eventPrivacy, eventTags;
    private SwitchCompat switchPrivacyBtn;
    private Button saveDataBtn;

    private ConstraintLayout tagsBottomSheet;

    private Create_Event_ViewModel createEventViewModel;

    final Calendar calendar = Calendar.getInstance();

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
        eventTagsTextView = view.findViewById(R.id.event_tags_field);

        tagsBottomSheet = view.findViewById(R.id.tags_bottom_sheet);
        switchPrivacyBtn = view.findViewById(R.id.event_privacy);

        saveDataBtn = view.findViewById(R.id.save_data_btn);
        Button nextbtn = ((CreateNewEvent) getActivity()).nextbtn;

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
        eventTagsTextView.addTextChangedListener(textWatcher);


        switchPrivacyBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    switchPrivacyBtn.setText("Private");
                } else {
                    switchPrivacyBtn.setText("Public");
                }
            }
        });

        eventName = eventNameTextView.getText().toString();


        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                eventDate();
            }
        };

        eventDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(), R.style.my_dialog_theme, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        eventTimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker();
            }
        });

        eventTagsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.CustomBottomSheetDialog);
                View bottomSheetView = getLayoutInflater().inflate(R.layout.event_tags, null);

                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();

                TextView danceTagTextView = bottomSheetDialog.findViewById(R.id.dance_textView);
                TextView karaokeTagTextView = bottomSheetDialog.findViewById(R.id.karaoke_textView);
                TextView raveTagTextView = bottomSheetDialog.findViewById(R.id.rave_textView);
            }
        });

        saveDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventName = eventNameTextView.getText().toString();
                eventDate = eventDateTextView.getText().toString();
                eventDescription = eventDescriptionTextView.getText().toString();
                eventTime = eventTimeTextView.getText().toString();
                eventMinAge = eventMinAgeTextView.getText().toString();
                eventMaxPeople = eventMaxPeopleTextView.getText().toString();
                eventPrivacy = (String) switchPrivacyBtn.getText();
                eventTags = String.valueOf(eventTagsTextView.getText());

                if (eventName.length() == 0 || eventDate.length() == 0 || eventTime.length() == 0) {
                    if (eventName.length() == 0) {
                        eventNameTextView.setError("Required");
                    } else if (eventDate.length() == 0) {
                        eventDateTextView.setError("Required");
                    } else if (eventTime.length() == 0) {
                        eventTimeTextView.setError("Required");
                    }
                } else {
                    eventDateTextView.setError(null);
                    eventTimeTextView.setError(null);
                    createEventViewModel.setData(eventName, eventDate, eventDescription, eventTime, eventMinAge,
                            eventMaxPeople,
                            eventTags,
                            eventPrivacy);
                    nextbtn.setVisibility(View.VISIBLE);
                    saveDataBtn.setVisibility(View.INVISIBLE);
                    Toast.makeText(getActivity(), "Data Saved",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void eventDate() {
        String myFormat = "dd/MM/yy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.UK);
        eventDate = dateFormat.format(calendar.getTime());
        Date date = new Date();
        String currentDate = dateFormat.format(date);

        if(eventDate.compareTo(currentDate) > 0 || eventDate.compareTo(currentDate) == 0){
            eventDateTextView.setText(dateFormat.format(calendar.getTime()));
        }else{
            Toast.makeText(getActivity(), "Invalid Date",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void showTimePicker() {

        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), R.style.MyTimePickerWidgetStyle, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                eventTimeTextView.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
            }
        }, hour, minute, true);
        timePickerDialog.show();
    }
}