package com.alizawren.myparker;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.protobuf.Internal;

import java.util.Calendar;
import java.util.List;

public class RenterActivity extends AppCompatActivity {

    private TimePicker timePicker1;
    private TimePicker timePicker2;

    private DatePicker datePicker;

    EditText locationText, descriptionText, phoneText, priceText;
    Button btnDatePicker, btnTimePicker,btnDatePickerEnd, btnTimePickerEnd;
    EditText txtDate, txtTime, txtDateEnd, txtTimeEnd;
    private int mYear, mMonth, mDay, mHour, mMinute;

    Button submitButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renter);

        Button switchScreen = findViewById(R.id.button_back);
        //timePicker1 = (TimePicker) findViewById(R.id.start_time_picker);
        //timePicker2 = (TimePicker) findViewById(R.id.end_time_picker);


        switchScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnDatePicker=(Button)findViewById(R.id.btn_date);
        btnTimePicker=(Button)findViewById(R.id.btn_time);
        txtDate=(EditText)findViewById(R.id.in_date);
        txtTime=(EditText)findViewById(R.id.in_time);

        btnDatePickerEnd=(Button)findViewById(R.id.btn_end_date);
        btnTimePickerEnd=(Button)findViewById(R.id.btn_end_time);
        txtDateEnd=(EditText)findViewById(R.id.in_end_date);
        txtTimeEnd=(EditText)findViewById(R.id.in_end_time);

        final Context self = this;

        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(self,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        btnTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(self,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                txtTime.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        btnDatePickerEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(self,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                txtDateEnd.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        btnTimePickerEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(self,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                txtTimeEnd.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        locationText = (EditText)findViewById(R.id.location);
        descriptionText = (EditText)findViewById(R.id.description);
        phoneText = (EditText)findViewById(R.id.phone_number);
        priceText = (EditText)findViewById(R.id.price);

        submitButton = (Button)findViewById(R.id.submit);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String location = locationText.getText().toString();
                String desc = descriptionText.getText().toString();
                String phone = phoneText.getText().toString();
                float price = Float.parseFloat(priceText.getText().toString());
                String startTimeText = txtTime.getText().toString();
                String endTimeText = txtTimeEnd.getText().toString();
                String startDateText = txtDate.getText().toString();
                String endDateText = txtDateEnd.getText().toString();
                User theUser = Util.getCurrentUser();
                ParkingSpot newSpot = new ParkingSpot(Util.getNewID(), theUser.getEmail(), location, desc, phone, 0.0f, startTimeText,endTimeText,startDateText,endDateText, "");
                Util.addParkingSpot(theUser, newSpot);

                finish();
            }
        });

        ListView list = findViewById(R.id.display);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1);
        list.setAdapter(adapter);

        Util.getParkingSpots().onResult(new Consumer<List<ParkingSpot>>() {
            @Override
            public void accept(List<ParkingSpot> parkingSpots) {
                for(ParkingSpot parkingSpot : parkingSpots)
                {
                    if (parkingSpot.userEmail == Util.getCurrentUser().getEmail())
                    {
                        adapter.add(parkingSpot.location);
                    }
                }
            }
        });
    }


}


