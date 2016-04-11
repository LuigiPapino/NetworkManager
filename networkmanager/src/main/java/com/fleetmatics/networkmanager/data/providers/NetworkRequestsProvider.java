package com.fleetmatics.networkmanager.data.providers;

import android.net.Uri;
import android.support.annotation.NonNull;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by luigi.papino on 06/04/16.
 */
@ContentProvider(authority = NetworkRequestsProvider.AUTHORITY, database = NetworkRequestsDataBase.class)
public final class NetworkRequestsProvider {

    public static final String AUTHORITY = "com.fleetmatics.android.NetworkRequestsProvider";

    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    private static Uri buildUri(@NonNull String... paths) {
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path : paths) {
            builder.appendPath(path);
        }
        return builder.build();
    }

    @TableEndpoint(table = NetworkRequestsDataBase.REQUESTS)
    public static class NetworkRequests {

        @ContentUri(
                path = NetworkRequestsDataBase.REQUESTS,
                type = "vnd.android.cursor.dir/list",
                defaultSort = NetworkRequestsColumns.JSON + " ASC")
        public static final Uri NETWORK_REQUESTS = Uri.parse("content://" + AUTHORITY + "/" + NetworkRequestsDataBase.REQUESTS);

        @InexactContentUri(
                path = NetworkRequestsDataBase.REQUESTS + "/#",
                name = "NETWORK_REQUEST_ID",
                type = "vnd.android.cursor.item/vnd.com.fleetmatics.android.network_request",
                whereColumn = NetworkRequestsColumns._ID,
                pathSegment = 1)
        public static Uri withId(long id) {
            return buildUri(NetworkRequestsDataBase.REQUESTS, String.valueOf(id));
        }
    }
}
