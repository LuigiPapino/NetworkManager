package com.fleetmatics.networkingworkapp;

import android.app.Application;
import android.support.annotation.NonNull;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.fleetmatics.networkingworkapp.injection.Graph;
import com.fleetmatics.networkmanager.commons.RefWatcherProvider;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;


/**
 * Created by nietzsche on 28/01/16.
 */
public class MyApplication extends Application implements RefWatcherProvider{
    private static final String TAG = MyApplication.class.getSimpleName();
    private static MyApplication instance;

    private Graph graph;
    private RefWatcher refWatcher;


    @NonNull
    public static MyApplication getInstance() {
        return instance;
    }


    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
        refWatcher = LeakCanary.install(this);
        graph = Graph.Initializer.init(this);
        graph.inject(this);
        Fresco.initialize(this);
    }



    public Graph getGraph() {
        return graph;
    }

    @Override
    public RefWatcher getRefWatcher() {
        return refWatcher;
    }







}
