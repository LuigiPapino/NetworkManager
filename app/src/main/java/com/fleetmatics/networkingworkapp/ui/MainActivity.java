package com.fleetmatics.networkingworkapp.ui;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.fleetmatics.networkingworkapp.MyApplication;
import com.fleetmatics.networkingworkapp.R;
import com.fleetmatics.networkingworkapp.model.ResponseSearch;
import com.fleetmatics.networkingworkapp.network.SearchExecutor;
import com.fleetmatics.networkmanager.network.NetworkRequestManager;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import javax.inject.Inject;

import rx.Subscription;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    @ViewById
    Toolbar toolbar;
    @Inject
    NetworkRequestManager networkRequestManager;
    @Extra
    boolean hello;

    public MainActivity() {
        MyApplication.getInstance().getGraph().inject(this);
    }

    @AfterViews
    void setup() {
        setSupportActionBar(toolbar);

        RxPermissions.getInstance(this)
                .request(Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe();

    }

    @Click
    void fab() {
        Subscription subscription = networkRequestManager.executeRequest(
                SearchExecutor.createRequest("superman", 1), ResponseSearch.class
        )
                .subscribe(
                        response -> {
                            if (response.isCompleted()) {

                            } else if (response.isWaitingConnection()) {

                            }
                        }
                );

    }
}
