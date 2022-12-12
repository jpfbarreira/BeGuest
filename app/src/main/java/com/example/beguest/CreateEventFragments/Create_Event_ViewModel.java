package com.example.beguest.CreateEventFragments;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class Create_Event_ViewModel extends ViewModel {

    private final MutableLiveData<String> eventName;
    private final MutableLiveData<String> eventDate;
    private final MutableLiveData<String> eventDescription;
    private final MutableLiveData<String> eventTime;
    private final MutableLiveData<String> eventMinAge;
    private final MutableLiveData<String> eventMaxPeople;
    private final MutableLiveData<String> eventMinPoints;
    private final MutableLiveData<String> eventPrivacy;
    private final MutableLiveData<String> eventLocation;


    public Create_Event_ViewModel() {
        eventName = new MutableLiveData<>();
        eventDate = new MutableLiveData<>();
        eventDescription = new MutableLiveData<>();
        eventTime = new MutableLiveData<>();
        eventMinAge = new MutableLiveData<>();
        eventMaxPeople = new MutableLiveData<>();
        eventMinPoints = new MutableLiveData<>();
        eventPrivacy = new MutableLiveData<>();
        eventLocation = new MutableLiveData<>();
    }

    public void setData(String name, String date, String description, String time,
                        String minAge, String maxPeople, String minPoints, String privacy){
        eventName.setValue(name);
        eventDate.setValue(date);
        eventDescription.setValue(description);
        eventTime.setValue(time);
        eventMinAge.setValue(minAge);
        eventMaxPeople.setValue(maxPeople);
        eventMinPoints.setValue(minPoints);
        eventPrivacy.setValue(privacy);
        Log.d("View model", "setted the data of your event " + privacy);
    }

    public MutableLiveData<String> getEventName() {
        return eventName;
    }

    public MutableLiveData<String> getEventDate() {
        return eventDate;
    }

    public MutableLiveData<String> getEventDescription() {
        return eventDescription;
    }

    public MutableLiveData<String> getEventTime() {
        return eventTime;
    }

    public MutableLiveData<String> getEventMinAge() {
        return eventMinAge;
    }

    public MutableLiveData<String> getEventMaxPeople() {
        return eventMaxPeople;
    }

    public MutableLiveData<String> getEventMinPoints() {
        return eventMinPoints;
    }

    public MutableLiveData<String> getEventPrivacy() {
        return eventPrivacy;
    }

    public MutableLiveData<String> getEventLocation() {
        return eventLocation;
    }
}
