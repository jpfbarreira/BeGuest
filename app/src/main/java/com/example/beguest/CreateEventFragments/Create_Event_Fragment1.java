package com.example.beguest.CreateEventFragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.beguest.Adapters.HomeEventsAdapter;
import com.example.beguest.Adapters.TagsAdapter;
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
    private TextInputEditText eventMinAgeTextView, eventMaxPeopleTextView;
    private String eventName, eventDate, eventDescription, eventTime, eventMinAge, eventMaxPeople, eventPrivacy, eventTags;
    private SwitchCompat switchPrivacyBtn;
    private Button saveDataBtn;
    private ConstraintLayout eventTagsTextView;
    //tags
    private boolean isDanceParty, isKaraoke, isRave, isDisco, isRock,isFunk, isPop, isRap = false;
    private ArrayList<String> arrayTags;
    private TagsAdapter tagsAdapter;

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
        eventTagsTextView = view.findViewById(R.id.textInputLayoutType);
        TextView event_tags_string = view.findViewById(R.id.event_tags_string);

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

                arrayTags = new ArrayList<>();

                RecyclerView recyclerView = view.findViewById(R.id.event_tags);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

                TextView danceTagTextView = bottomSheetDialog.findViewById(R.id.dance_textView);
                TextView karaokeTagTextView = bottomSheetDialog.findViewById(R.id.karaoke_textView);
                TextView raveTagTextView = bottomSheetDialog.findViewById(R.id.rave_textView);
                TextView discoTextView = bottomSheetDialog.findViewById(R.id.disco_textView);
                TextView rockTagTextView = bottomSheetDialog.findViewById(R.id.rock_textView);
                TextView funkTagTextView = bottomSheetDialog.findViewById(R.id.funk_textView);
                TextView popTagTextView = bottomSheetDialog.findViewById(R.id.pop_textView);
                TextView rapTagTextView = bottomSheetDialog.findViewById(R.id.rap_textView);

                danceTagTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(isDanceParty == false){
                            isDanceParty = true;
                            arrayTags.add("Dance");
                            danceTagTextView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#2e0101")));
                        }else {
                            isDanceParty = false;
                            arrayTags.remove("Dance");
                            danceTagTextView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#5c0202")));
                        }
                    }
                });
                karaokeTagTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(isKaraoke == false){
                            isKaraoke = true;
                            arrayTags.add("Karaoke");
                            karaokeTagTextView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#2e0101")));
                        }else {
                            isKaraoke = false;
                            arrayTags.remove("Karaoke");
                            karaokeTagTextView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#5c0202")));
                        }
                    }
                });
                raveTagTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(isRave == false){
                            isRave = true;
                            arrayTags.add("Rave");
                            raveTagTextView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#2e0101")));
                        }else {
                            isRave = false;
                            arrayTags.remove("Rave");
                            raveTagTextView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#5c0202")));
                        }
                    }
                });
                discoTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(isDisco == false){
                            isDisco = true;
                            arrayTags.add("Disco");
                            discoTextView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#2e0101")));
                        }else {
                            isDisco = false;
                            arrayTags.remove("Disco");
                            discoTextView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#5c0202")));
                        }
                    }
                });
                rockTagTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(isRock == false){
                            isRock = true;
                            arrayTags.add("Rock");
                            rockTagTextView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#2e0101")));
                        }else {
                            isRock = false;
                            arrayTags.remove("Rock");
                            rockTagTextView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#5c0202")));
                        }
                    }
                });
                funkTagTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(isFunk == false){
                            isFunk = true;
                            arrayTags.add("Funk");
                            funkTagTextView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#2e0101")));
                        }else {
                            isFunk = false;
                            arrayTags.remove("Funk");
                            funkTagTextView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#5c0202")));
                        }
                    }
                });

                popTagTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(isPop == false){
                            isPop = true;
                            arrayTags.add("Pop");
                            popTagTextView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#2e0101")));
                        }else {
                            isPop = false;
                            arrayTags.remove("Pop");
                            popTagTextView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#5c0202")));
                        }
                    }
                });

                rapTagTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(isRap == false){
                            isRap = true;
                            arrayTags.add("Rap");
                            rapTagTextView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#2e0101")));
                        }else {
                            isRap = false;
                            arrayTags.remove("Rap");
                            rapTagTextView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#5c0202")));
                        }
                    }
                });
                bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        Log.d("arraytags", String.valueOf(arrayTags));
                        if (arrayTags.size() > 0){
                            event_tags_string.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            tagsAdapter = new TagsAdapter(getContext(), arrayTags);
                            recyclerView.setAdapter(tagsAdapter);
                        }else {
                            event_tags_string.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }
                    }
                });
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
                            eventPrivacy,
                            arrayTags);
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