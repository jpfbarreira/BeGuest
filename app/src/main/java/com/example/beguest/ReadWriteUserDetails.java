package com.example.beguest;

import java.io.Serializable;
import java.util.ArrayList;

public class ReadWriteUserDetails implements Serializable {
    public String username;
    public int points;
    public String instagram;
    public String twitter;
    public String email;
    public ArrayList<String> registeredEventsIDs = new ArrayList<String>();

    //constructor
    public ReadWriteUserDetails(){};

    public ReadWriteUserDetails(String username,int points, String instagram, String twitter, String email , ArrayList<String> registeredEventsIDs) {
        this.username = username;
        this.points = points;
        this.instagram = instagram;
        this.twitter = twitter;
        this.email = email;
        this.registeredEventsIDs = registeredEventsIDs;
    }
}
