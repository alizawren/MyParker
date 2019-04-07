package com.alizawren.myparker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;

public class ParkingListAdapter extends BaseAdapter
{
  private List<ParkingSpot> parkingSpots;
  private LayoutInflater inflater;

  public ParkingListAdapter(Context context, List<ParkingSpot> parkingSpots)
  {
    super();
    this.parkingSpots = parkingSpots;
    this.inflater = LayoutInflater.from(context);
  }

  @Override
  public int getCount() {
    return this.parkingSpots.size();
  }

  @Override
  public Object getItem(int i) {
    return this.parkingSpots.get(i);
  }

  @Override
  public long getItemId(int i) {
    return i;
  }

  @Override
  public View getView(int i, View view, ViewGroup viewGroup) {
    if (view == null)
    {
      view = this.inflater.inflate(R.layout.parking_spot_entry, viewGroup, false);

      TextView location = view.findViewById(R.id.parking_location);
      TextView description = view.findViewById(R.id.parking_description);
      TextView startTime = view.findViewById(R.id.parking_startTime);
      TextView stopTime = view.findViewById(R.id.parking_stopTime);
      TextView price = view.findViewById(R.id.parking_price);

      ParkingSpot parkingSpot = (ParkingSpot) this.getItem(i);
      location.setText(parkingSpot.getLocation());
      description.setText(parkingSpot.getDescription());
      startTime.setText(parkingSpot.getStartDate() + "-" + parkingSpot.getStartTime());
      stopTime.setText(parkingSpot.getEndDate() + "-" + parkingSpot.getEndTime());

      if (parkingSpot.isRented())
      {
        price.setText("RENTED");
      }
      else
      {
        price.setText(parkingSpot.getPrice());
      }
    }

    return view;
  }
}
