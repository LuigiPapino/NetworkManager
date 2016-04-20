package com.fleetmatics.networkingworkapp.network;


import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by luigi.papino on 19/04/16.
 */
public interface GoogleAPIService {


    @GET("json?key=AIzaSyAVMG5X4o4Z9teye9dLK3BRGF0DLReN2MY")
    Observable<com.google.gson.JsonObject> getPath(@Query("origin") String origin, @Query("destination") String destination, @Query("arrival_time") long arrivalTime);
}
