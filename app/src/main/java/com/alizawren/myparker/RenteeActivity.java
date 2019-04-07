package com.alizawren.myparker;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alizawren.myparker.util.Consumer;
import java.util.ArrayList;
import java.util.List;
import android.location.Geocoder;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class RenteeActivity extends AppCompatActivity implements OnMapReadyCallback {

  double lat = 0.0;
  double lng = 0.0;


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
                String location = parkingSpot.getLocation();

                // Get location
                Geocoder gc = new Geocoder(context);
                if(gc.isPresent()){

                  List<Address> list;
                  try {
                    list = gc.getFromLocationName(location, 1);
                  }
                  catch(Exception ex) {
                    System.out.println("Location not a proper string");
                    return;
                  }
                  if (!list.isEmpty()) {
                    Address address = list.get(0);
                    lat = address.getLatitude();
                    lng = address.getLongitude();
                  }
                  else {
                    lat = 37.42279;
                    lng = -122.08506;
                  }
                }

                if (!parkingSpot.isRented()) {

                  //CustomMapDialog cmd = new CustomMapDialog(RenteeActivity.this, lat, lng);
                  //cmd.show();
                  // Open a new dialog to manage this parking spot
                  AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                  alertDialogBuilder.setMessage(spotInfo);

                  /*SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                          .findFragmentById(R.id.map);
                  mapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                      Geocoder gc = new Geocoder(context);
                    }
                  });*/

                  //View theView = new View(context);
                  //alertDialogBuilder.setView(theView);


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
                final ParkingSpot parkingSpot = mySpots.get(i);

                if (parkingSpot.isRented()) {
                  // Open a new dialog to manage this parking spot
                  AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                  alertDialogBuilder.setMessage("Are you done with renting this spot?");
                  alertDialogBuilder.setPositiveButton("I'm done", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                      // yes was pressed
                      ParkingUtil.unrentParkingSpot(parkingSpot);
                      Intent intent = getIntent();
                      finish();
                      startActivity(intent);
                    }
                  });
                  alertDialogBuilder.setNegativeButton("I'm  not done", new DialogInterface.OnClickListener() {
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
      }
    });
  }

  // Include the OnCreate() method here too, as described above.
  @Override
  public void onMapReady(GoogleMap googleMap) {
    // Add a marker in Sydney, Australia,
    // and move the map's camera to the same location.
    LatLng location = new LatLng(lat, lng);
    googleMap.addMarker(new MarkerOptions().position(location)
            .title("Parking Spot Marker"));
    googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
  }
}
