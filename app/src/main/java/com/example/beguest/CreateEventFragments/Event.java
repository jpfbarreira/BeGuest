package com.example.beguest.CreateEventFragments;

import java.util.ArrayList;

public class Event {
    public ArrayList<String> registeredUserIDs = new ArrayList<String>();
    public String name, date, description, time, minAge,  maxPeople,  minPoints,  privacy, location;

    //constructor
    public Event(){};

    public Event(ArrayList<String> registeredUserIDs, String name, String date, String description, String time,
                 String minAge, String maxPeople, String minPoints, String privacy, String location) {
        this.registeredUserIDs = registeredUserIDs;
        this.name = name;
        this.date = date;
        this.description = description;
        this.time = time;
        this.minAge = minAge;
        this.maxPeople = maxPeople;
        this.minPoints = minPoints;
        this.privacy = privacy;
        this.location = location;
    }
}
