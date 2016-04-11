package com.fleetmatics.networkingworkapp.commons;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.fleetmatics.networkingworkapp.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * Created by nietzsche on 16/02/16.
 */
public class Utility {

    public static boolean showLog = BuildConfig.DEBUG;

    public static void logD(String TAG, String message) {
        if (showLog)
            Log.d(TAG, message);
    }


    public static Uri ResourceToUri(Context context, int resID) {
        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                context.getResources().getResourcePackageName(resID) + '/' +
                context.getResources().getResourceTypeName(resID) + '/' +
                context.getResources().getResourceEntryName(resID));
    }

    public static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }
}
