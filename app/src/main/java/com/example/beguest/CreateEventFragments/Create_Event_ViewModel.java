package com.example.beguest.CreateEventFragments;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class Create_Event_ViewModel extends ViewModel {

    private final MutableLiveData<String> eventName;
    private final MutableLiveData<String> eventDate;
    private final MutableLiveData<String> eventDescription;
    private final MutableLiveData<String> eventTime;
    private final MutableLiveData<String> eventMinAge;
    private final MutableLiveData<String> eventMaxPeople;
    private final MutableLiveData<ArrayList<String>> arrayTags;
    private final MutableLiveData<String> eventPrivacy;
    private final MutableLiveData<String> eventLocation;


    public Create_Event_ViewModel() {
        eventName = new MutableLiveData<>();
        eventDate = new MutableLiveData<>();
        eventDescription = new MutableLiveData<>();
        eventTime = new MutableLiveData<>();
        eventMinAge = new MutableLiveData<>();
        eventMaxPeople = new MutableLiveData<>();
        arrayTags = new MutableLiveData<>();
        eventPrivacy = new MutableLiveData<>();
        eventLocation = new MutableLiveData<>();
    }

    public void setData(String name, String date, String description, String time,
                        String minAge, String maxPeople, String privacy, ArrayList<String> eventTags){
        eventName.setValue(name);
        eventDate.setValue(date);
        eventDescription.setValue(description);
        eventTime.setValue(time);
        eventMinAge.setValue(minAge);
        eventMaxPeople.setValue(maxPeople);
        arrayTags.setValue(eventTags);
        eventPrivacy.setValue(privacy);
    }

    public LiveData<String> getEventName() {
        return eventName;
    }

    public LiveData<String> getEventDate() {
        return eventDate;
    }

    public LiveData<String> getEventDescription() {
        return eventDescription;
    }

    public LiveData<String> getEventTime() {
        return eventTime;
    }

    public LiveData<String> getEventMinAge() {
        return eventMinAge;
    }

    public LiveData<String> getEventMaxPeople() {
        return eventMaxPeople;
    }

    public LiveData<ArrayList<String>> getEventTags() {
        return arrayTags;
    }

    public LiveData<String> getEventPrivacy() {
        return eventPrivacy;
    }

    public LiveData<String> getEventLocation() {
        return eventLocation;
    }

    public void setLocation(String location){
        eventLocation.setValue(location);
    }
}
