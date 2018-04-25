package com.example.melike.uberapp;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Melike on 19.04.2018.
 */

public interface getPolyline {
    @GET("json")
    Call<JsonObject> getPolylineData(@Query("origin") String origin, @Query("destination") String destination);
}
