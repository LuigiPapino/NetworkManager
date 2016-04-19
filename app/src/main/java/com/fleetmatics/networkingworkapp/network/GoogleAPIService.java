package com.fleetmatics.networkingworkapp.network;


import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by luigi.papino on 19/04/16.
 */
public interface GoogleAPIService {


    @GET("json?key=AIzaSyBQWh_OR7NgiKOWfnPRwktqpMfxk93CzBM")
    Observable<com.google.gson.JsonObject> getPath(@Query("origin") String origin, @Query("destination") String destination, @Query("arrival_time") long arrivalTime);
}
