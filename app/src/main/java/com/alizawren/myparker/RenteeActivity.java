package com.alizawren.myparker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
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

    final ArrayList<ParkingSpot> adapterSpots = new ArrayList<>();

    ParkingUtil.getParkingSpots().onResult(new Consumer<List<ParkingSpot>>() {
      @Override
      public void accept(List<ParkingSpot> parkingSpots) {
        for (ParkingSpot parkingSpot : parkingSpots) {
          if (parkingSpot.isOwnedBy(MainActivity.currentUser)) {
            continue;
          }
          if (!parkingSpot.isValid()) {
            continue;
          }
          adapterSpots.add(parkingSpot);
          initParkingList(adapterSpots);
        }
      }
    });
  }

  private void initParkingList(final List<ParkingSpot> parkingSpots)
  {
    ListView list = findViewById(R.id.display);
    ParkingListAdapter parkingListAdapter = new ParkingListAdapter(this.getApplicationContext(), parkingSpots);
    list.setAdapter(parkingListAdapter);
    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        boolean flag = false;
        ParkingSpot parkingSpot = parkingSpots.get(position);

        if (parkingSpot.isRented()) {
          if (parkingSpot.isUsedBy(MainActivity.currentUser)) {
            ParkingUtil.unrentParkingSpot(parkingSpot);
            flag = true;
          } else {
            //Not yours!
          }
        } else {
          ParkingUtil.rentParkingSpot(MainActivity.currentUser, parkingSpot);
          flag = true;
        }

        if (flag) {
          //Restart the activity....
          Intent intent = getIntent();
          finish();
          startActivity(intent);
        }
      }
    });
  }
}
