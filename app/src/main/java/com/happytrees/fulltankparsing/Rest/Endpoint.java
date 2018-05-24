package com.happytrees.fulltankparsing.Rest;

import com.happytrees.fulltankparsing.JsonModel.MyResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Endpoint  {

    @GET("/maps/api/place/textsearch/json")
    Call<MyResponse> getMyResults(@Query("query") String query, @Query("key") String key);



}
//31.96167523  34.88159882