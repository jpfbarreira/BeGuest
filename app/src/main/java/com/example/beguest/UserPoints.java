package com.example.beguest;

import java.io.Serializable;

public class UserPoints implements Serializable {
    private String id;
    private String points;

    public UserPoints() {

    }

    public UserPoints(String id, String points) {
        this.id = id;
        this.points = points;
    }

    public String getId() {
        return id;
    }

    public String getPoints() {
        return points;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPoints(String points) {
        this.points = points;
    }
}
