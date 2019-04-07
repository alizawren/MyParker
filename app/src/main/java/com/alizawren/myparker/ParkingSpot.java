package com.alizawren.myparker;

/**
 * Created by Alisa Ren on 4/6/2019.
 */

public class ParkingSpot {

  private String ownerEmail;
  private String location;
  private String description;
  private String phoneNumber;
  private float price;
  private String startTime;
  private String endTime;
  private String startDate;
  private String endDate;
  private String clientEmail;
  private String id;

  public ParkingSpot() {
    this.id = "";
  }

  public ParkingSpot(String id, String ownerEmail, String location, String description,
      String phoneNumber, float price, String startTime, String endTime, String startDate,
      String endDate, String clientEmail) {
    this.id = id;
    this.ownerEmail = ownerEmail;
    this.location = location;
    this.description = description;
    this.phoneNumber = phoneNumber;
    this.price = price;
    this.startTime = startTime;
    this.endTime = endTime;
    this.startDate = startDate;
    this.endDate = endDate;
    this.clientEmail = clientEmail;
  }

  public boolean isOwnedBy(User user) {
    return user.getEmail().equals(this.ownerEmail);
  }

  public boolean isUsedBy(User user) {
    return user.getEmail().equals(this.clientEmail);
  }

  public boolean isValid() {
    return this.id != null && this.ownerEmail != null;
  }

  public boolean isRented() {
    return this.clientEmail != null && this.clientEmail.length() > 0;
  }

  public String getOwnerEmail() {
    return ownerEmail;
  }

  public String getLocation() {
    return location;
  }

  public String getDescription() {
    return description;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public float getPrice() {
    return price;
  }

  public String getStartTime() {
    return startTime;
  }

  public String getEndTime() {
    return endTime;
  }

  public String getStartDate() {
    return startDate;
  }

  public String getEndDate() {
    return endDate;
  }

  public String getClientEmail() {
    return clientEmail;
  }

  public ParkingSpot setClientEmail(String email) {
    this.clientEmail = email;
    return this;
  }

  public String getID() {
    return id;
  }

  @Override
  public String toString() {
    return this.location + ":" + this.endDate + ":" + this.endTime;
  }

}
