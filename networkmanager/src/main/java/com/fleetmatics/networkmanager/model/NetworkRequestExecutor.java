package com.fleetmatics.networkmanager.model;

import android.support.annotation.NonNull;

import com.fleetmatics.networkmanager.network.NetworkApi;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by luigi.papino on 06/04/16.
 */
public abstract class NetworkRequestExecutor<T, API> {

    private static NetworkRequestExecutor instance;

    public NetworkRequestExecutor() {
        instance = this;
    }

    public static NetworkRequestExecutor getInstance() {
        return instance;
    }

    public abstract Observable<Response<T>> createNetworkRequestObservable(@NonNull NetworkApi<API> networkApi, @NonNull NetworkRequest request);

    public abstract String getExecutableType();

    public abstract void newStatusWithoutObserver(NetworkRequest request, NetworkRequestStatus status);
}
