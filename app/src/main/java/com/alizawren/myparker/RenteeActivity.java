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

    ListView list = findViewById(R.id.display);
    final ArrayList<ParkingSpot> adapterSpots = new ArrayList<>();
    final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
        android.R.layout.simple_list_item_1);
    list.setAdapter(adapter);
    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        boolean flag = false;
        ParkingSpot parkingSpot = adapterSpots.get(position);

        if (parkingSpot.isRented()) {
          if (Util.getCurrentUser().getEmail().equals(parkingSpot.renteeEmail)) {
            Util.unrentParkingSpot(parkingSpot);
            flag = true;
          } else {
            //Not yours!
          }
        } else {
          Util.rentParkingSpot(parkingSpot);
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

    Util.getParkingSpots().onResult(new Consumer<List<ParkingSpot>>() {
      @Override
      public void accept(List<ParkingSpot> parkingSpots) {
        for (ParkingSpot parkingSpot : parkingSpots) {
          if (Util.getCurrentUser().getEmail().equals(parkingSpot.userEmail)) {
            continue;
          }
          if (!parkingSpot.isValid()) {
            continue;
          }
          if (parkingSpot.isRented()) {
            adapter.add("RENTED - " + parkingSpot.toString());
            adapterSpots.add(parkingSpot);
          } else {
            adapter.add(parkingSpot.toString());
            adapterSpots.add(parkingSpot);
          }
        }
      }
    });

  }
}
