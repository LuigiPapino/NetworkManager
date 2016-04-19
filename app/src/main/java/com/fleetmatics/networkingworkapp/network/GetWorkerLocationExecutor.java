package com.fleetmatics.networkingworkapp.network;

import android.support.annotation.NonNull;
import android.util.Log;

import com.fleetmatics.networkingworkapp.model.WorkerLatLng;
import com.fleetmatics.networkmanager.model.NetworkRequest;
import com.fleetmatics.networkmanager.model.NetworkRequestExecutor;
import com.fleetmatics.networkmanager.model.NetworkRequestStatus;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by luigi.papino on 08/04/16.
 */
public class GetWorkerLocationExecutor extends NetworkRequestExecutor<WorkerLatLng> {


    private static final String TAG = GetWorkerLocationExecutor.class.getSimpleName();
    private static GetWorkerLocationExecutor instance;
    private final NetworkApi<APIService> networkApi;

    public GetWorkerLocationExecutor(NetworkApi<APIService> networkApi) {
        this.networkApi = networkApi;
        instance = this;
    }

    public static GetWorkerLocationExecutor getInstance() {
        return instance;
    }

    public static NetworkRequest createRequest(int workerId) {
        return new NetworkRequest.Builder(getInstance(), false)
                .putParam("workerId", String.valueOf(workerId))
                .build();
    }

    @Override
    public Observable<Response<WorkerLatLng>> createNetworkRequestObservable(@NonNull NetworkRequest request) {
        return networkApi.getApiService().getWorkerLocation(request.getParams().get("workerId"));
    }

    @Override
    public String getExecutableType() {
        return "GET_WORKER_LOCATION";
    }

    @Override
    public void updatedStatus(NetworkRequest request, NetworkRequestStatus status, boolean hasObservers) {
        Log.d(TAG, "updatedStatus() called with: " + "request = [" + request + "], status = [" + status + "], hasObservers = [" + hasObservers + "]");
    }
}
