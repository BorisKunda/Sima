package com.happytrees.fulltankparsing.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.happytrees.fulltankparsing.Adapter.MyAdapter;
import com.happytrees.fulltankparsing.Objects.Station;
import com.happytrees.fulltankparsing.R;


import java.util.ArrayList;
import java.util.List;


public class FavouritesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        final List<Station> allFavourites = Station.listAll(Station.class);
        RecyclerView recyclerView = findViewById(R.id.recyclerFavourites);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(FavouritesActivity.this);//layout manager defines look of RecyclerView -- > grid,list
        recyclerView.setLayoutManager(layoutManager);
        //adapter
        final MyAdapter myAdapter = new MyAdapter((ArrayList<Station>) allFavourites,FavouritesActivity.this);
        recyclerView.setAdapter(myAdapter);
    }
}
