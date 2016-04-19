package com.fleetmatics.networkingworkapp.network;

import android.support.annotation.NonNull;
import android.util.Log;

import com.fleetmatics.networkmanager.model.NetworkRequest;
import com.fleetmatics.networkmanager.model.NetworkRequestExecutor;
import com.fleetmatics.networkmanager.model.NetworkRequestStatus;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by luigi.papino on 08/04/16.
 */
public class PutTokenExecutor extends NetworkRequestExecutor<String> {


    private static final String TAG = PutTokenExecutor.class.getSimpleName();
    private static PutTokenExecutor instance;
    private final NetworkApi<APIService> networkApi;

    public PutTokenExecutor(NetworkApi<APIService> networkApi) {
        this.networkApi = networkApi;
        instance = this;
    }

    public static PutTokenExecutor getInstance() {
        return instance;
    }

    public static NetworkRequest createRequest(int customerId, String token) {
        return new NetworkRequest.Builder(getInstance(), false)
                .putParam("customerId", String.valueOf(customerId))
                .putParam("token", token)
                .build();
    }

    @Override
    public Observable<Response<String>> createNetworkRequestObservable(@NonNull NetworkRequest request) {
        return networkApi.getApiService().putToken(request.getParams().get("customerId"), request.getParams().get("token"));
    }

    @Override
    public String getExecutableType() {
        return "PUT_TOKEN";
    }

    @Override
    public void updatedStatus(NetworkRequest request, NetworkRequestStatus status, boolean hasObservers) {
        Log.d(TAG, "updatedStatus() called with: " + "request = [" + request + "], status = [" + status + "], hasObservers = [" + hasObservers + "]");
    }
}
