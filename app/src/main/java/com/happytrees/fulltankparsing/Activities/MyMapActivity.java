package com.happytrees.fulltankparsing.Activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.happytrees.fulltankparsing.R;

public class MyMapActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_map);

        Intent incomingIntent= getIntent();//gets object passed by intent from previous activity
        String passedLat = incomingIntent.getStringExtra("ExtraLat");
        String passedLng = incomingIntent.getStringExtra("ExtraLng");
        double passedLatConverted =  Double.parseDouble(passedLat);
        double passedLngConverted =  Double.parseDouble(passedLng);

        final LatLng mapLatLng = new LatLng(passedLatConverted,passedLngConverted);

        MapFragment mapFragment = new MapFragment();
        getFragmentManager().beginTransaction().replace(R.id.my_container,mapFragment).commit();
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                googleMap.addMarker(new MarkerOptions().position(mapLatLng).title("Your Gas Station Is Here"));//add marker
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mapLatLng, 17));//v --> is zoom.0 is no zoom
            }
        });



/*
   @Override
    public void changeFragments(double lat, double lng, final String name) {
        customLatLng = new LatLng(lat,lng);
        //CREATES MAP FRAGMENT ->  no need to create java,xml file for it .No need in creating GPS object and make Location Permission in manifest.Only key cause you using google API
        MapFragment mapFragment = new MapFragment();
        if(isTablet()) {//isTablet = true .Means device used xlarge xml layout of MainActivity,due to device large screen(tablet)
            getFragmentManager().beginTransaction().addToBackStack("adding map").replace(R.id.ExtraContainer, mapFragment).commit(); //we add MapFragment  only  to sub layout of xlarge MainActivity called ExtraContainer ,and only if its not null(its null if device detected normal size screen)
        }else{
            getFragmentManager().beginTransaction().addToBackStack("replacing").replace(R.id.MainContainer, mapFragment).commit();//replaces current Fragment with mapFragment.
        }
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                googleMap.addMarker(new MarkerOptions().position(customLatLng).title(name));//add marker
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(customLatLng, 17));//v --> is zoom.0 is no zoom
            }
        });
    }
 */

    }
}