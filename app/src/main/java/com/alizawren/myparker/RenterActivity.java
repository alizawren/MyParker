package com.alizawren.myparker;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
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

  private AutoCompleteTextView locationText;
  private EditText descriptionText, phoneText;
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

    btnTimePicker = findViewById(R.id.btn_time);
    txtDate = findViewById(R.id.in_date);
    txtTime = findViewById(R.id.in_time);

    btnTimePickerEnd = findViewById(R.id.btn_end_time);
    txtDateEnd = findViewById(R.id.in_end_date);
    txtTimeEnd = findViewById(R.id.in_end_time);

    final Context self = this;

    btnTimePicker.setOnClickListener(new View.OnClickListener() {
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
            }, mYear, mMonth, mDay);
        datePickerDialog.show();
      }
    });

    btnTimePickerEnd.setOnClickListener(new View.OnClickListener() {
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
            }, mYear, mMonth, mDay);
        datePickerDialog.show();
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
        /*
        float priceValue = 0.0F;
        try {
          priceValue = Float.parseFloat(price);
        } catch (Exception ignored) {
        }
        */
        String startTimeText = txtTime.getText().toString();
        String endTimeText = txtTimeEnd.getText().toString();
        String startDateText = txtDate.getText().toString();
        String endDateText = txtDateEnd.getText().toString();
        User theUser = MainActivity.currentUser;
        ParkingSpot newSpot = new ParkingSpot(ParkingUtil.getNewID(), theUser.getEmail(), location,
            desc,
            phone, price, startTimeText, endTimeText, startDateText, endDateText, "");
        ParkingUtil.addParkingSpot(theUser, newSpot);

        //Restart the activity....
        Intent intent = getIntent();
        finish();
        startActivity(intent);
      }
    });

    final Context context = this;
    ParkingUtil.getParkingSpots().onResult(new Consumer<List<ParkingSpot>>() {
      @Override
      public void accept(List<ParkingSpot> parkingSpots) {

        final ArrayList<ParkingSpot> spots = new ArrayList<>();

        for (ParkingSpot parkingSpot : parkingSpots) {
          if (parkingSpot.isOwnedBy(MainActivity.currentUser)) {
            spots.add(parkingSpot);
          }
        }

        ListView listView = findViewById(R.id.display);
        ParkingListAdapter.initParkingListView(context, listView, spots, new OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
            // Open a new dialog to manage this parking spot

            final ParkingSpot parkingSpot = spots.get(i);

            String spotInfo = parkingSpot.toString();

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setMessage(spotInfo);
            alertDialogBuilder.setPositiveButton("Take Down Rental", new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
                // yes was pressed
                ParkingUtil.removeParkingSpot(spots.get(i));
                Intent intent = getIntent();
                finish();
                startActivity(intent);
              }
            });
            alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
                // no was pressed
                Intent intent = getIntent();
                finish();
                startActivity(intent);
              }

            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();



            //Restart the activity....
            Intent intent = getIntent();
            finish();
            startActivity(intent);
          }
        });
      }
    });
  }
}


