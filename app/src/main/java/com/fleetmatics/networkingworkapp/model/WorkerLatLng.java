package com.fleetmatics.networkingworkapp.model;

import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by luigi.papino on 19/04/16.
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class WorkerLatLng {

    double latitude;
    double longitude;

    public LatLng getLatLng() {
        return new LatLng(latitude, longitude);
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
