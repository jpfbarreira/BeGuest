package com.example.beguest;

import java.util.ArrayList;

public class ReadWriteUserDetails {
    public String username;
    public int points;
    public String instagram;
    public String twitter;

    //constructor
    public ReadWriteUserDetails(){};

    public ReadWriteUserDetails(String username,int points, String instagram, String twitter) {
        this.username = username;
        this.points = points;
        this.instagram = instagram;
        this.twitter = twitter;
    }
}
