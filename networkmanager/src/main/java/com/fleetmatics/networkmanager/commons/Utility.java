package com.fleetmatics.networkmanager.commons;

import android.util.Log;


/**
 * Created by luigi.papino on 06/04/16.
 */
public class Utility {

    public static boolean showLog = true;

    public static void logD(String TAG, String message) {
        if (showLog)
            Log.d(TAG, message);
    }



}
