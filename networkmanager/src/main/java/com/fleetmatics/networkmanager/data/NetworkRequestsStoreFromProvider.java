package com.fleetmatics.networkmanager.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.bluelinelabs.logansquare.LoganSquare;
import com.fleetmatics.networkmanager.data.providers.NetworkRequestsColumns;
import com.fleetmatics.networkmanager.data.providers.NetworkRequestsProvider;
import com.fleetmatics.networkmanager.model.NetworkRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by luigi.papino on 06/04/16.
 * <p>
 * Implementation of @NetworkRequestsStore that retrieve the data from a ContentProvider
 */
public class NetworkRequestsStoreFromProvider implements NetworkRequestsStore {
    private static final String TAG = NetworkRequestsStoreFromProvider.class.getSimpleName();
    private Context context;

    public NetworkRequestsStoreFromProvider(@NonNull Context context) {
        Log.d(TAG, "NetworkRequestsStoreFromProvider() called with: " + "context = [" + context + "]");
        this.context = context;
    }


    @Override
    @NonNull
    public List<NetworkRequest> getAll() {
        Log.d(TAG, "getAll() called with: " + "");

        Cursor cursor = getContentResolver().query(NetworkRequestsProvider.NetworkRequests.NETWORK_REQUESTS,
                new String[]{NetworkRequestsColumns._ID, NetworkRequestsColumns.JSON}
                , null, null, null);

        return cursorToList(cursor);
    }

    @Override
    @Nullable
    public NetworkRequest get(int hashcode) {
        Log.d(TAG, "get() called with: " + "hashcode = [" + hashcode + "]");
        Cursor cursor = getContentResolver().query(NetworkRequestsProvider.NetworkRequests.withId(hashcode),
                new String[]{NetworkRequestsColumns._ID, NetworkRequestsColumns.JSON}
                , null, null, null);
        List<NetworkRequest> list = cursorToList(cursor);
        return list.size() > 0 ? list.get(0) : null;
    }

    @Override
    public void put(@Nullable NetworkRequest request) {
        if (request == null)
            return;
        ContentValues values = new ContentValues();
        try {
            values.put(NetworkRequestsColumns._ID, request.hashCode());
            values.put(NetworkRequestsColumns.JSON, LoganSquare.serialize(request));
        } catch (IOException e) {
            e.printStackTrace();
        }
        getContentResolver().insert(NetworkRequestsProvider.NetworkRequests.NETWORK_REQUESTS, values);
    }

    @Override
    public boolean delete(int hashcode) {
        Log.d(TAG, "delete() called with: " + "hashcode = [" + hashcode + "]");
        int result = getContentResolver()
                .delete(NetworkRequestsProvider.NetworkRequests.withId(hashcode), null, null);
        return result > 0;
    }

    private ContentResolver getContentResolver() {
        return context.getContentResolver();
    }


    @NonNull
    protected ContentObserver getContentObserver() {
        HandlerThread handlerThread = new HandlerThread(this.getClass().getSimpleName());
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        return new ContentObserver(handler) {
            @Override
            public void onChange(boolean selfChange, Uri uri) {
                super.onChange(selfChange, uri);
                Log.v(TAG, "onChange(" + uri + ")");
                //queryAndNext();
            }
        };
    }


    @NonNull
    private List<NetworkRequest> cursorToList(@Nullable Cursor cursor) {
        ArrayList<NetworkRequest> contacts = new ArrayList<>(0);
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    contacts = new ArrayList<>(cursor.getCount());
                    int jsonColIndex = cursor.getColumnIndex(NetworkRequestsColumns.JSON);
                    do {
                        try {
                            contacts.add(LoganSquare.parse(cursor.getString(jsonColIndex), NetworkRequest.class));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } while (cursor.moveToNext());
                }

            } finally {
                cursor.close();
            }
        }

        return contacts;
    }

}
