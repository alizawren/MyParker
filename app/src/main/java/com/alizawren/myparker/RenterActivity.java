package com.alizawren.myparker;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import com.alizawren.myparker.util.Consumer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RenterActivity extends AppCompatActivity {

  private EditText locationText, descriptionText, phoneText;
  private Button btnDatePicker, btnTimePicker, btnDatePickerEnd, btnTimePickerEnd;
  private TextView txtDate, txtTime, txtDateEnd, txtTimeEnd;
  private Button submitButton;

  private int mYear, mMonth, mDay, mHour, mMinute;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_renter);

    Button switchScreen = findViewById(R.id.button_back);

    switchScreen.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        finish();
      }
    });

    btnDatePicker = findViewById(R.id.btn_date);
    btnTimePicker = findViewById(R.id.btn_time);
    txtDate = findViewById(R.id.in_date);
    txtTime = findViewById(R.id.in_time);

    btnDatePickerEnd = findViewById(R.id.btn_end_date);
    btnTimePickerEnd = findViewById(R.id.btn_end_time);
    txtDateEnd = findViewById(R.id.in_end_date);
    txtTimeEnd = findViewById(R.id.in_end_time);

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

    locationText = findViewById(R.id.location);
    descriptionText = findViewById(R.id.description);
    phoneText = findViewById(R.id.phone_number);

    final EditText priceText = findViewById(R.id.price);
    priceText.addTextChangedListener(new NumberTextWatcher(priceText, "#,###"));

    submitButton = findViewById(R.id.submit);

    submitButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String location = locationText.getText().toString();
        String desc = descriptionText.getText().toString();
        String phone = phoneText.getText().toString();
        String price = priceText.getText().toString();
        float priceValue = 0.0F;
        try
        {
          priceValue = Float.parseFloat(price);
        }
        catch (Exception e) {}
        String startTimeText = txtTime.getText().toString();
        String endTimeText = txtTimeEnd.getText().toString();
        String startDateText = txtDate.getText().toString();
        String endDateText = txtDateEnd.getText().toString();
        User theUser = Util.getCurrentUser();
        ParkingSpot newSpot = new ParkingSpot(Util.getNewID(), theUser.getEmail(), location, desc,
            phone, priceValue, startTimeText, endTimeText, startDateText, endDateText, "");
        Util.addParkingSpot(theUser, newSpot);

        finish();
      }
    });

    ListView list = findViewById(R.id.display);
    final ArrayList<ParkingSpot> spots = new ArrayList<>();
    final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
        android.R.layout.simple_list_item_1);
    list.setAdapter(adapter);
    list.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Util.removeParkingSpot(spots.get(i));

        //Restart the activity....
        Intent intent = getIntent();
        finish();
        startActivity(intent);
      }
    });

    Util.getParkingSpots().onResult(new Consumer<List<ParkingSpot>>() {
      @Override
      public void accept(List<ParkingSpot> parkingSpots) {
        for (ParkingSpot parkingSpot : parkingSpots) {
          if (Util.getCurrentUser().getEmail().equals(parkingSpot.userEmail)) {
            adapter.add(parkingSpot.toString());
            spots.add(parkingSpot);
          }
        }
      }
    });
  }


}


