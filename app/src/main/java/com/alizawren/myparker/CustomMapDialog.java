package com.alizawren.myparker;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyboardShortcutGroup;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

/**
 * Created by Alisa Ren on 4/7/2019.
 */

public class CustomMapDialog extends Dialog implements DialogInterface.OnClickListener, OnMapReadyCallback
{
    private AppCompatActivity activity;
    private double lat = 0.0;
    private double lng = 0.0;

    LinearLayout linearLayout;

    public CustomMapDialog(@NonNull AppCompatActivity activity, double lat, double lng) {
        super(activity);
        this.activity = activity;
        this.lat = lat;
        this.lng = lng;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Inflate the layout for this fragment
        //View view = inflater.inflate(R.layout.list_fragment, container, false);
        //ListView listView = (ListView) view.findViewById(R.id.testListView);
        //listView.setAdapter(adapter);


        setContentView(R.layout.custom_map_dialog);



        FragmentManager fragmentManager = this.activity.getSupportFragmentManager();//.beginTransaction().replace(R.id.fragment_container, fragment).commit();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //fragmentTransaction.replace(R.id.dynamic_fragment_frame_layout, destFragment);
        fragmentTransaction.commit();

        SupportMapFragment mapFragment = (SupportMapFragment) fragmentManager.findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.custom_map_dialog, container, false);
        linearLayout = (LinearLayout) rootView.findViewById(R.id.linearlayout);
        return rootView;
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng location = new LatLng(lat, lng);
        googleMap.addMarker(new MarkerOptions().position(location)
        .title("Parking Space"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
    }
}