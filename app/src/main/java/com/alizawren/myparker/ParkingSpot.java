package com.alizawren.myparker;

/**
 * Created by Alisa Ren on 4/6/2019.
 */

public class ParkingSpot
{
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
	private String id;

	public ParkingSpot()
	{

	}

	public ParkingSpot(String id, String userEmail, String location, String description, String phoneNumber, float price, String startTime, String endTime, String startDate, String endDate, String renteeEmail)
	{
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

	public boolean isRented()
	{
		return this.renteeEmail != null && this.renteeEmail.length() > 0;
	}

	public String getID()
	{
		return id;
	}

	@Override
	public String toString()
	{
		return this.location + ":" + this.endDate + ":" + this.endTime;
	}

}
