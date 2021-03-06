package com.fleetmatics.networkingworkapp.ui;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.fleetmatics.networkingworkapp.MyApplication;
import com.fleetmatics.networkingworkapp.R;
import com.fleetmatics.networkingworkapp.model.ResponseSearch;
import com.fleetmatics.networkingworkapp.network.SearchExecutor;
import com.fleetmatics.networkmanager.network.NetworkRequestManager;

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
    Subscription subscription;

    public MainActivity() {
        MyApplication.getInstance().getGraph().inject(this);
    }

    @AfterViews
    void setup() {
        setSupportActionBar(toolbar);

    }

    @Click
    void fab() {
        if (subscription != null)
            subscription.unsubscribe();
        subscription = networkRequestManager.executeRequest(
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

    @Override
    protected void onPause() {
        super.onPause();
        if (subscription != null)
            subscription.unsubscribe();
    }
}
