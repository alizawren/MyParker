package com.alizawren.myparker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import com.alizawren.myparker.util.Consumer;
import java.util.ArrayList;
import java.util.List;

public class RenteeActivity extends AppCompatActivity {

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
        ParkingListAdapter.initParkingListView(context, listView, otherSpots, new OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            ParkingSpot parkingSpot = otherSpots.get(i);

            if (!parkingSpot.isRented()) {
              ParkingUtil.rentParkingSpot(MainActivity.currentUser, parkingSpot);

              //Restart the activity....
              Intent intent = getIntent();
              finish();
              startActivity(intent);
            }
          }
        });



        ListView myListView = findViewById(R.id.myDisplay);        ParkingListAdapter.initParkingListView(context, myListView, mySpots, new OnItemClickListener() {

          @Override
          public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            ParkingSpot parkingSpot = mySpots.get(i);

            if (parkingSpot.isRented()) {
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
