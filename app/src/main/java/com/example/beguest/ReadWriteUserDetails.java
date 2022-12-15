package com.example.beguest;

import java.util.ArrayList;

public class ReadWriteUserDetails {
    public String username;
    public int points;
    public String instagram;
    public String twitter;
    public ArrayList<String> registeredEventsIDs = new ArrayList<String>();

    //constructor
    public ReadWriteUserDetails(){};

    public ReadWriteUserDetails(String username,int points, String instagram, String twitter, ArrayList<String> registeredEventsIDs) {
        this.username = username;
        this.points = points;
        this.instagram = instagram;
        this.twitter = twitter;
        this.registeredEventsIDs = registeredEventsIDs;
    }
}
