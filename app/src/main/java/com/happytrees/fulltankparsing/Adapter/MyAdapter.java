package com.happytrees.fulltankparsing.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.happytrees.fulltankparsing.Objects.Station;
import com.happytrees.fulltankparsing.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    public ArrayList<Station> stations;
    public Context context;
    // public double latFromMainActivity; -> pass latitude from main activity to adapter using adapter's constructor
    //public double lngFromMainActivity; -> pass latitude from main activity to adapter using adapter's constructor


    public MyAdapter(ArrayList<Station> stations, Context context) {
        this.stations = stations;
        this.context = context;
        //  latFromMainActivity;
        // lngFromMainActivity;

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.station_item, null);
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
            TextView nameTV = myView.findViewById(R.id.nameTV);
            nameTV.setText(currentStation.name);


            TextView priceTV1 = myView.findViewById(R.id.price1);
            priceTV1.setText(currentStation.price1);


            TextView priceTV2 = myView.findViewById(R.id.price2);
            priceTV2.setText(currentStation.price2);

            TextView priceTV3 = myView.findViewById(R.id.price3);
            priceTV3.setText(currentStation.price3);


            TextView distanceTV = myView.findViewById(R.id.distanceValTV);





            distanceTV.setText("unknown");

            ImageView stationIV = myView.findViewById(R.id.stationIV);


            final ProgressBar progressBar = myView.findViewById(R.id.progress);
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


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //open map
                    Toast.makeText(context, "click", Toast.LENGTH_SHORT).show();

                }
            });


            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //DIALOG - SAVE TO FAVOURITES OR OPEN NAVIGATION
                    //alert dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("which one suits you best?");//dialog message
                    builder.setPositiveButton("Save to Favourites", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Station station = new Station(currentStation.name, currentStation.price1, currentStation.price2, currentStation.price3, currentStation.urlImage);
                            station.save();
                            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
                            //save to DB
                        }
                    });

                    builder.setNeutralButton("Driving Directions", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(context, "driver", Toast.LENGTH_SHORT).show();
                            //begin direction
                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(context, "closed", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                    return true;
                }
            });
        }
    }

}
