package com.fleetmatics.networkmanager.network;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;


import com.fleetmatics.networkmanager.commons.BaseIntentService;
import com.fleetmatics.networkmanager.model.NetworkRequest;
import com.fleetmatics.networkmanager.model.NetworkRequestExecutor;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EIntentService;
import org.androidannotations.annotations.ServiceAction;
import org.androidannotations.annotations.SystemService;

import retrofit2.Response;


/**
 * Created by luigi.papino on 06/04/16.
 */
@EIntentService
public class NetworkRequestService extends BaseIntentService {

    public static final String EXTRA_REQUEST = "extra_request";
    private static final String TAG = NetworkRequestService.class.getSimpleName();

    @SystemService
    ConnectivityManager connectivityManager;
    private boolean isConnectionAvailable = true;

    public NetworkRequestService() {
        super(TAG);

    }

    @AfterInject
    void setup(){
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        isConnectionAvailable = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        logD("onHandleIntent() called with: " + "intent = [" + intent + "]");
    }

    @ServiceAction
    void onConnectionAvailable(boolean isConnected) {
        Log.d(TAG, "onConnectionAvailable() called with: " + "isConnected = [" + isConnected + "]");
        isConnectionAvailable = isConnected;

        // execute network requests not already started or waiting for connection
        if (isConnectionAvailable) {
            for (NetworkRequest request : NetworkRequestManager.getInstance().getRequestsStore().getAll()) {
                if (!NetworkRequestManager.getInstance().contains(request.hashCode())
                        || NetworkRequestManager.getInstance().isWaitingConnection(hashCode())
                        )
                    executeRequest(request);
            }
        }
    }

    @ServiceAction
    void executeRequest(final NetworkRequest request) {
        logD("executeRequest() called with: " + "request = [" + request + "]");
        NetworkRequestManager requestManager = NetworkRequestManager.getInstance();

        if (isConnectionAvailable) {
            NetworkRequestExecutor executor = NetworkRequestManager.getInstance().getExecutor(request);

            rx.Observable<Response> observable = executor.createNetworkRequestObservable((NetworkApi<DialogInterface.OnClickListener>) NetworkApi.getInstance(), request);

            requestManager.notifyRequestStatusOnGoing(request.hashCode());
            observable
                    .subscribe(
                            response -> {

                                if (response.isSuccessful()) {
                                    requestManager
                                            .notifyRequestStatusSuccess(request.hashCode(), response.body());
                                } else
                                    requestManager.notifyRequestStatusError(request.hashCode(), response.errorBody(), false);
                            },
                            throwable -> {
                                requestManager.notifyRequestStatusError(request.hashCode(), throwable.getMessage(), true);
                            }
                    );
        } else
            requestManager.notifyRequestStatusWaitingConnection(request.hashCode());


    }


}
