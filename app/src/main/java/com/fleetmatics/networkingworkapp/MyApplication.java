package com.fleetmatics.networkingworkapp;

import android.app.NotificationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.multidex.MultiDexApplication;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.fleetmatics.networkingworkapp.injection.Graph;
import com.fleetmatics.networkingworkapp.model.JobStatus;
import com.fleetmatics.networkingworkapp.network.PutTokenExecutor;
import com.fleetmatics.networkmanager.commons.RefWatcherProvider;
import com.fleetmatics.networkmanager.network.NetworkRequestManager;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import javax.inject.Inject;

import eu.inloop.easygcm.EasyGcm;
import eu.inloop.easygcm.GcmListener;
import io.branch.referral.Branch;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;


/**
 * Created by nietzsche on 28/01/16.
 */
public class MyApplication extends MultiDexApplication implements RefWatcherProvider, GcmListener {
    private static final String TAG = MyApplication.class.getSimpleName();
    private static MyApplication instance;
    @Inject
    NetworkRequestManager networkRequestManager;
    NotificationManager notificationManager;
    private Graph graph;
    private RefWatcher refWatcher;
    private BehaviorSubject<Integer> subjectCustomerID = BehaviorSubject.create();
    private PublishSubject<Bundle> gcmSubject = PublishSubject.create();

    @NonNull
    public static MyApplication getInstance() {
        return instance;
    }

    @NonNull
    public rx.Observable<Integer> getCustomerIdObservable() {
        return subjectCustomerID.asObservable();
    }

    @Nullable
    public Integer getCustomerIdValue() {
        return subjectCustomerID.getValue();
    }

    public void setCustomerId(int id) {
        subjectCustomerID.onNext(id);
    }

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
        refWatcher = LeakCanary.install(this);
        graph = Graph.Initializer.init(this);
        graph.inject(this);
        EasyGcm.init(this);
        Branch.getAutoInstance(this);
    }

    public Graph getGraph() {
        return graph;
    }

    @Override
    public RefWatcher getRefWatcher() {
        return refWatcher;
    }

    public rx.Observable<Bundle> getGCMObserver() {
        return gcmSubject.asObservable();
    }

    @Override
    public void onMessage(String s, Bundle bundle) {
        Log.d(TAG, "onMessage() called with: " + "s = [" + s + "], bundle = [" + bundle + "]");
        gcmSubject.onNext(bundle);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher);
        if (bundle.getInt("type") == 0) {
            mBuilder.setContentTitle(bundle.getString("jobTitle"))
                    .setContentText("New Status: " + JobStatus.values()[bundle.getInt("value")].toString());
        } else if (bundle.getInt("type") == 1) {
            mBuilder.setContentTitle(bundle.getString("workerName"))
                    .setContentText("is in late " + bundle.getString("value"));
        }


        if (notificationManager == null)
            notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(1, mBuilder.build());

    }

    @Override
    public void sendRegistrationIdToBackend(String s) {
        Log.d(TAG, "sendRegistrationIdToBackend() called with: " + "s = [" + s + "]");

        if (getCustomerIdValue() == null || s == null)
            return;
        networkRequestManager
                .executeRequest(PutTokenExecutor.createRequest(getCustomerIdValue(), s), String.class)
                .subscribe(status -> Log.d(TAG, status.toString()));

    }
}
