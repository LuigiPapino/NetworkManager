package com.fleetmatics.networkmanager.network;

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
    void setup() {
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
                if (!NetworkRequestManager.Tools.contains(request.hashCode())
                        || NetworkRequestManager.Tools.isWaitingConnection(request.hashCode())
                        )
                    NetworkRequestManager.getInstance().executeRequest(request);
            }
        }
    }

    @ServiceAction
    void executeRequest(final NetworkRequest request) {
        logD("executeRequest() called with: " + "request = [" + request + "]");

        if (isConnectionAvailable) {
            NetworkRequestExecutor executor = NetworkRequestManager.Tools.getExecutor(request);

            rx.Observable<Response> observable = executor.createNetworkRequestObservable(request);

            NetworkRequestManager.Notify.notifyRequestStatusOnGoing(request.hashCode());
            observable
                    .subscribe(
                            response -> {

                                if (response.isSuccessful()) {
                                    NetworkRequestManager.Notify
                                            .notifyRequestStatusSuccess(request.hashCode(), response.body());
                                } else
                                    NetworkRequestManager.Notify.notifyRequestStatusError(request.hashCode(), response.errorBody(), false);
                            },
                            throwable -> {
                                NetworkRequestManager.Notify.notifyRequestStatusError(request.hashCode(), throwable.getMessage(), true);
                            }
                    );

        } else
            NetworkRequestManager.Notify.notifyRequestStatusWaitingConnection(request.hashCode());


    }


}
