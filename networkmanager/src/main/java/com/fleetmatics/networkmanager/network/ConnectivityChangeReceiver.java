package com.fleetmatics.networkmanager.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by luigi.papino on 06/04/16.
 */
public class ConnectivityChangeReceiver extends BroadcastReceiver {

    private static final String TAG = ConnectivityChangeReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive() called with: " + "context = [" + context + "], intent = [" + intent + "]");
        NetworkInfo info = (NetworkInfo) intent.getExtras().get("networkInfo");
        boolean isConnected = info != null &&
                info.isConnectedOrConnecting();

        NetworkRequestService_.intent(context)
                .onConnectionAvailable(isConnected)
                .start();


    }
}
