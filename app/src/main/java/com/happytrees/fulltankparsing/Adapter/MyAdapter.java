package com.happytrees.fulltankparsing.Adapter;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.google.android.gms.maps.model.LatLng;
import com.happytrees.fulltankparsing.Activities.MainActivity;
import com.happytrees.fulltankparsing.JsonModel.MyResponse;
import com.happytrees.fulltankparsing.JsonModel.MyResult;
import com.happytrees.fulltankparsing.Objects.Station;
import com.happytrees.fulltankparsing.R;
import com.happytrees.fulltankparsing.Rest.ApiClient;
import com.happytrees.fulltankparsing.Rest.Endpoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

            String nameFromActivity = currentStation.name;
            String nameFromActivityRemovedSpace = nameFromActivity.replace(" ", "+");
            String gasStation = "gas+station+";
            String fullQuery = gasStation + nameFromActivityRemovedSpace;

            //FETCH LOCATION VIA RETROFIT FROM JSON
            String key = "AIzaSyDo6e7ZL0HqkwaKN-GwKgqZnW03FhJNivQ";
            final Endpoint apiService = ApiClient.getClient().create(Endpoint.class);
            Call<MyResponse>call = apiService.getMyResults(fullQuery,key);
            call.enqueue(new Callback<MyResponse>() {
                @Override
                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                    final ArrayList<MyResult> myDataSource = new ArrayList<>();
                    MyResponse myResponse = response.body();
                    myDataSource.addAll(myResponse.results);
                    for(MyResult myResult : myDataSource) {
                        Log.e("s","s" + myResult);
                    }
                    Log.e("SHOW ME WHAT YOU GOT","GOOD JOB " );
                }

                @Override
                public void onFailure(Call<MyResponse> call, Throwable t) {
                    Log.e("SHOW ME WHAT YOU GOT "," BOOOOOO NOT COOL ");
                }
            });

            //final double myLatitude = txtResultCurrent.geometry.location.lat;

            /*
                    Call<TxtResponse> call = apiService.getMyResults(fromEdtTxt, key);
                            progressDoalog.show();//SHOW PROGRESS BAR BEFORE CALL
                            call.enqueue(new Callback<TxtResponse>() {
                                @Override
                                public void onResponse(Call<TxtResponse> call, Response<TxtResponse> response) {
                                    final ArrayList<TxtResult> myDataSource = new ArrayList<>();
                                    myDataSource.clear();//clean old list if there was call from before
                                    TxtResponse res = response.body();


                                    myDataSource.addAll(res.results);

                                    if (myDataSource.isEmpty()) {
                                        Toast.makeText(getActivity(), "No Results", Toast.LENGTH_SHORT).show();//TOAST MESSAGE IF WE HAVE JSON WITH ZERO RESULTS
                                    }

                                    //SEARCH HISTORY

                                    if (!myDataSource.isEmpty()) {//prevent an attempt of storing empty  array into LastSearch DB


                                        //delete old searches
                                        List<LastSearch> lastSearches = LastSearch.listAll(LastSearch.class);//select all favourites
                                        LastSearch.deleteAll(LastSearch.class);


                                        //loop through array  of results received from retrofit and insert them all into database as most recent result
                                        for (int position = 0; position < myDataSource.size(); position++) {
                                            LastSearch lastSearch = new LastSearch(myDataSource.get(position).name, myDataSource.get(position).formatted_address, myDataSource.get(position).geometry.location.lat, myDataSource.get(position).geometry.location.lng);
                                            lastSearch.save();
                                        }

                                    }


                                    //setting txt adapter
                                    RecyclerView.Adapter myTxtAdapter = new TxtAdapter(myDataSource, getActivity());
                                    fragArecycler.setAdapter(myTxtAdapter);
                                    myTxtAdapter.notifyDataSetChanged();//refresh
                                    progressDoalog.dismiss();//dismiss progress bar after call was completed
                                    Log.i("TxtResults", " very good: " + response.body());

                                }


                                @Override
                                public void onFailure(Call<TxtResponse> call, Throwable t) {
                                    progressDoalog.dismiss();//dismiss progress bar after call was completed
                                    Log.i("TxtResults", " bad: " + t);
                                }
                            });

             */


            //fetching place's location according to it's name
            if (Geocoder.isPresent()) {
                try {
                    String location = currentStation.name;   // "יעד מור רמלה"  ;//currentStation.name;
                    Geocoder gc = new Geocoder(context);
                    List<Address> addresses = gc.getFromLocationName(location, 1); // get the found Address Objects

                    List<LatLng> ll = new ArrayList<LatLng>(addresses.size()); // A list to save the coordinates if they are available
                    for (Address a : addresses) {
                        if (a.hasLatitude() && a.hasLongitude()) {
                            ll.add(new LatLng(a.getLatitude(), a.getLongitude()));
                        }
                    }

                    if (ll.isEmpty()) {
                        distanceTV.setText("unknown");
                    } else {
                        //if one of double is empty write unknown
                        if (MainActivity.lat == null || MainActivity.lng == null) {
                            distanceTV.setText("unknown");
                        } else {
                            float[] distanceResults = new float[10];//10 random number.you need any number higher than 3
                            Location.distanceBetween(MainActivity.lat, MainActivity.lng, ll.get(0).latitude, ll.get(0).longitude, distanceResults);//DEFAULT IN KILOMETERS
                          //  Location.distanceBetween(31.96120024, 34.88155316,  31.936582, 34.8832343, distanceResults);//DEFAULT IN KILOMETERS
                            Double roundedDis =  (double)Math.round( (distanceResults[0]/1000 ) * 100d) / 100d;//number of zeros must be same in and outside parenthesis.number of zeroes equals to number of numbers after dot that will remain after rounding up
                            distanceTV.setText(roundedDis + "km");
                        }

                    }

 //ganey aviv 31.96120024 34.88155316
                    // iad mor ramle -> 31.936582,34.8832343

                } catch (IOException e) {
                    // handle the exception
                }
            }


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
        }
    }
}

