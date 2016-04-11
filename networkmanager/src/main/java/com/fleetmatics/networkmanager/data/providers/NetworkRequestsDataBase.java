package com.fleetmatics.networkmanager.data.providers;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by luigi.papino on 06/04/16.
 */
@Database(version = NetworkRequestsDataBase.VERSION)
public final class NetworkRequestsDataBase {
    public static final int VERSION =1;

    @Table(NetworkRequestsColumns.class) public static final String REQUESTS = "requests";
}
