package com.alizawren.myparker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1);
        list.setAdapter(adapter);

        Util.getParkingSpots().onResult(new Consumer<List<ParkingSpot>>() {
            @Override
            public void accept(List<ParkingSpot> parkingSpots) {
                for(ParkingSpot parkingSpot : parkingSpots)
                {
                    if (parkingSpot.userEmail != Util.getCurrentUser().getEmail())
                    {
                        adapter.add(parkingSpot.location);
                    }
                }
            }
        });

    }
}
