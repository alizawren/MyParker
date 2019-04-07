package com.alizawren.myparker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
        final ArrayList<ParkingSpot> parkingSpots = new ArrayList<>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ParkingSpot parkingSpot = parkingSpots.get(position);

                if (parkingSpot.isRented())
                {
                    Util.unrentParkingSpot(parkingSpot);
                }
                else
                {
                    Util.rentParkingSpot(parkingSpot);
                }

                //Restart the activity....
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

        Util.getParkingSpots().onResult(new Consumer<List<ParkingSpot>>() {
            @Override
            public void accept(List<ParkingSpot> parkingSpots) {
                for(ParkingSpot parkingSpot : parkingSpots)
                {
                    if (parkingSpot.userEmail != Util.getCurrentUser().getEmail())
                    {
                        if (parkingSpot.isRented()) continue;
                        adapter.add(parkingSpot.toString());
                        parkingSpots.add(parkingSpot);
                    }
                    else if (parkingSpot.isRented())
                    {
                        adapter.add("RENTED - " + parkingSpot.toString());
                        parkingSpots.add(parkingSpot);
                    }
                }
            }
        });

    }
}
