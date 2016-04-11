package com.fleetmatics.networkingworkapp.network;

import android.support.annotation.NonNull;

import com.fleetmatics.networkingworkapp.model.ResponseSearch;
import com.fleetmatics.networkmanager.model.NetworkRequest;
import com.fleetmatics.networkmanager.model.NetworkRequestExecutor;
import com.fleetmatics.networkmanager.network.NetworkApi;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by luigi.papino on 08/04/16.
 */
public class SearchExecutor extends NetworkRequestExecutor<ResponseSearch, APIService> {


    public static NetworkRequest createRequest(String search, int page) {
        NetworkRequest request = new NetworkRequest();
        request.setRetry(0);
        request.setType(SearchExecutor.getInstance().getExecutableType());
        request.getParams().put("s", search);
        request.getParams().put("page", String.valueOf(page));
        request.generateUri(false);

        return request;
    }

    @Override
    public Observable<Response<ResponseSearch>> createNetworkRequestObservable(@NonNull NetworkApi<APIService> networkApi, @NonNull NetworkRequest request) {
        return networkApi.getApiService()
                .search(request.getParams().get("s"), request.getParams().get("page"))
                ;
    }

    @Override
    public String getExecutableType() {
        return "GET_SEARCH";
    }
}
