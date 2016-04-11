package com.fleetmatics.networkmanager.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.fleetmatics.networkmanager.model.NetworkRequest;

import java.util.List;


/**
 * Created by luigi.papino on 06/04/16.
 */
public interface NetworkRequestsStore {

    @NonNull
    List<NetworkRequest> getAll();

    @Nullable
    NetworkRequest get(int hashcode);

    void put(@Nullable NetworkRequest request);

    boolean delete(int hashcode);
}
