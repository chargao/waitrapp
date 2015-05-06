package com.example.Charlie.myapplication.backend;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * Created by nacorti on 5/5/2015.
 */
@Entity
public class RestaurantRecord {
    @Id
    private String name;
    private double lat;
    private double lon;
    private int lineLength;

    public RestaurantRecord(String nam, double latitude, double longitude) {
        name = nam;
        lat = latitude;
        lon = longitude;
        lineLength = 0;
    }

    public int getLineLength() {
        return lineLength;
    }

    public void incrementLength() {
        lineLength++;

    }
    public void decrementLength() {
        lineLength--;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    //For now we just calculate wait time by multiplying the number of customers in line by 4
    public int getWaitTime() {
        return lineLength*4;
    }
}
