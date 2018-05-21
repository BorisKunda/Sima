package com.happytrees.fulltankparsing.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.happytrees.fulltankparsing.Objects.Station;
import com.happytrees.fulltankparsing.R;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>  {

    public ArrayList<Station> stations;
    public Context context;

    public MyAdapter(ArrayList<Station> stations, Context context) {
        this.stations = stations;
        this.context = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.station_item,null);
        MyViewHolder myViewHolder = new MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Station station = stations.get(position);
        holder.bindDataFromArrayToView(station);

    }

    @Override
    public int getItemCount() {
        return stations.size();
    }



    //INNER CLASS
public class MyViewHolder extends RecyclerView.ViewHolder {
        View myView;

        public MyViewHolder(View itemView) {
            super(itemView);
            myView = itemView;
        }




        public void bindDataFromArrayToView(final Station currentStation) {
            TextView nameTV = (TextView)myView.findViewById(R.id.nameTV);
            nameTV.setText(currentStation.name);


            TextView priceTV1 = (TextView)myView.findViewById(R.id.price1);
            priceTV1.setText(currentStation.price1);


            TextView priceTV2 = (TextView)myView.findViewById(R.id.price2);
            priceTV2.setText(currentStation.price2);

            TextView priceTV3 = (TextView)myView.findViewById(R.id.price3);
            priceTV3.setText(currentStation.price3);


            ImageView stationIV = (ImageView)myView.findViewById(R.id.stationIV);


            final ProgressBar progressBar = (ProgressBar) myView.findViewById(R.id.progress);
            progressBar.setVisibility(View.VISIBLE);////make progress bar visible




                Glide.with(context).load(currentStation.urlImage).listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);//removes progress bar if there was exception
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);////removes progress bar if picture finished loading
                        return false;

                    }

                }).into(stationIV);//SET IMAGE THROUGH GLIDE

        }





    }



}
