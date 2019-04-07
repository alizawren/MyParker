package com.alizawren.myparker;

import java.util.UUID;

/**
 * Created by Alisa Ren on 4/6/2019.
 */

public class ParkingSpot {
    private String id;
    public String userEmail;
    public String location;
    public String description;
    public String phoneNumber;
    public float price;
    public String startTime;
    public String endTime;
    public String startDate;
    public String endDate;
    public String renteeEmail;

    public ParkingSpot() {

    }

    public ParkingSpot(String id, String userEmail, String location, String description, String phoneNumber, float price, String startTime, String endTime, String startDate, String endDate, String renteeEmail) {
        this.id = id;
        this.userEmail = userEmail;
        this.location = location;
        this.description = description;
        this.phoneNumber = phoneNumber;
        this.price = price;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startDate = startDate;
        this.endDate = endDate;
        this.renteeEmail = renteeEmail;
    }

    public String getID() {
        return id;
    }

}
