package com.fleetmatics.networkmanager.model;

import android.support.annotation.NonNull;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by luigi.papino on 06/04/16.
 */
public abstract class NetworkRequestExecutor<T> {




    public abstract Observable<Response<T>> createNetworkRequestObservable(@NonNull NetworkRequest request);

    public abstract String getExecutableType();

    public abstract void updatedStatus(NetworkRequest request, NetworkRequestStatus status, boolean hasObservers);

}
