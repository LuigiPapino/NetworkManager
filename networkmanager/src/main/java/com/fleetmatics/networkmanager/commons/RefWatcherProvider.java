package com.fleetmatics.networkmanager.commons;

import com.squareup.leakcanary.RefWatcher;

/**
 * Created by luigi.papino on 06/04/16.
 */
public interface RefWatcherProvider {

    RefWatcher getRefWatcher();
}
