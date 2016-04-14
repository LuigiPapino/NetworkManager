package com.fleetmatics.networkingworkapp.network;

import android.support.annotation.NonNull;
import android.util.Log;

import com.fleetmatics.networkingworkapp.model.ResponseSearch;
import com.fleetmatics.networkmanager.model.NetworkRequest;
import com.fleetmatics.networkmanager.model.NetworkRequestExecutor;
import com.fleetmatics.networkmanager.model.NetworkRequestStatus;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by luigi.papino on 08/04/16.
 */
public class SearchExecutor extends NetworkRequestExecutor<ResponseSearch, APIService> {


    private static final String TAG = SearchExecutor.class.getSimpleName();
    private final NetworkApi<APIService> networkApi;

    public SearchExecutor(NetworkApi<APIService> networkApi) {
        this.networkApi = networkApi;
    }

    public static NetworkRequest createRequest(String search, int page) {
        NetworkRequest request = new NetworkRequest.Builder(getInstance(), false)
                .putParam("s", search)
                .putParam("page", String.valueOf(page))
                .build();
        return request;
    }

    @Override
    public Observable<Response<ResponseSearch>> createNetworkRequestObservable(@NonNull NetworkRequest request) {
        return networkApi.getApiService()
                .search(request.getParams().get("s"), request.getParams().get("page"))
                ;
    }

    @Override
    public String getExecutableType() {
        return "GET_SEARCH";
    }

    @Override
    public void newStatusWithoutObserver(NetworkRequest request, NetworkRequestStatus status) {
        Log.d(TAG, "newStatusWithoutObserver() called with: " + "request = [" + request + "], status = [" + status + "]");
    }
}
