package com.alizawren.myparker;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.alizawren.myparker.util.Consumer;
import java.util.ArrayList;
import java.util.List;

public class RenteeActivity extends AppCompatActivity {

  //AlertDialog.Builder alertDialogBuilder;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_rentee);

    Button switchScreen = findViewById(R.id.button_back);
    switchScreen.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        finish();
      }
    });

    //alertDialogBuilder = new AlertDialog.Builder(this);

    final Context context = this;
    ParkingUtil.getParkingSpots().onResult(new Consumer<List<ParkingSpot>>() {
      @Override
      public void accept(List<ParkingSpot> parkingSpots) {

        final ArrayList<ParkingSpot> mySpots = new ArrayList<>();
        final ArrayList<ParkingSpot> otherSpots = new ArrayList<>();
        for (ParkingSpot parkingSpot : parkingSpots) {
          if (!parkingSpot.isValid()) {
            continue;
          }
          if (parkingSpot.isOwnedBy(MainActivity.currentUser)) {
            continue;
          }
          if (parkingSpot.isUsedBy(MainActivity.currentUser)) {
            mySpots.add(parkingSpot);
          } else {
            otherSpots.add(parkingSpot);
          }
        }

        ListView listView = findViewById(R.id.display);
        ParkingListAdapter
            .initParkingListView(context, listView, otherSpots, new OnItemClickListener() {
              @Override
              public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final ParkingSpot parkingSpot = otherSpots.get(i);

                String spotInfo = parkingSpot.toString();

                if (!parkingSpot.isRented()) {
                  // Open a new dialog to manage this parking spot
                  AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                  alertDialogBuilder.setMessage(spotInfo);
                  alertDialogBuilder.setPositiveButton("Rent", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                      // yes was pressed
                      ParkingUtil.rentParkingSpot(MainActivity.currentUser, parkingSpot);
                      Intent intent = getIntent();
                      finish();
                      startActivity(intent);
                    }
                  });
                  alertDialogBuilder.setNegativeButton("Don't Rent", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                      // no was pressed
                      Intent intent = getIntent();
                      finish();
                      startActivity(intent);
                    }

                  });
                  AlertDialog alertDialog = alertDialogBuilder.create();
                  alertDialog.show();

                }
              }
            });

        ListView myListView = findViewById(R.id.myDisplay);
        ParkingListAdapter
            .initParkingListView(context, myListView, mySpots, new OnItemClickListener() {

              @Override
              public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ParkingSpot parkingSpot = mySpots.get(i);

                if (parkingSpot.isRented()) {
                  // Open a new dialog to manage this parking spot
                  //AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

                  ParkingUtil.unrentParkingSpot(parkingSpot);

                  //Restart the activity....
                  Intent intent = getIntent();
                  finish();
                  startActivity(intent);
                }
              }
            });
      }
    });
  }
}
