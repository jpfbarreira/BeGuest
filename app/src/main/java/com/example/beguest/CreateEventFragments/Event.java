package com.example.beguest.CreateEventFragments;

import java.io.Serializable;
import java.util.ArrayList;

public class Event implements Serializable {
    public ArrayList<String> registeredUserIDs = new ArrayList<String>();
    public String eventID, title, date, description, time, minAge,  maxPeople,  privacy, location, creatorId;
    public Integer eventPhoto;
    public ArrayList<String> arrayTags;

    //constructor
    public Event(){};

    public Event(String creatorId, ArrayList<String> registeredUserIDs, String name, String date, String description, String time,
                 String minAge, String maxPeople, String privacy, String location, int eventPhoto, ArrayList<String> arrayTags) {
        this.creatorId = creatorId;
        this.registeredUserIDs = registeredUserIDs;
        this.title = name;
        this.date = date;
        this.description = description;
        this.time = time;
        this.minAge = minAge;
        this.maxPeople = maxPeople;
        this.privacy = privacy;
        this.location = location;
        this.eventPhoto = eventPhoto;
        this.arrayTags = arrayTags;
    }

    public ArrayList<String> getRegisteredUserIDs() {
        return registeredUserIDs;
    }

    public String getTitle() {
        return title;
    }

    public String getEventID(){
        return eventID;
    }

    public void setEventID(String eventID){
        this.eventID = eventID;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getTime() {
        return time;
    }

    public String getMinAge() {
        return minAge;
    }

    public String getMaxPeople() {
        return maxPeople;
    }

    public ArrayList<String> getTags() {
        return arrayTags;
    }

    public String getPrivacy() {
        return privacy;
    }

    public String getLocation() {
        return location;
    }

    public String getCreatorId() {
        return creatorId;
    }
}
