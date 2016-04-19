package com.fleetmatics.networkingworkapp.ui.jobs_list;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;

import com.fleetmatics.networkingworkapp.MyApplication;
import com.fleetmatics.networkingworkapp.R;
import com.fleetmatics.networkingworkapp.model.JobList;
import com.fleetmatics.networkingworkapp.network.JobsListExecutor;
import com.fleetmatics.networkingworkapp.ui.login.LoginActivity_;
import com.fleetmatics.networkingworkapp.ui.workers_map.WorkersMapActivity_;
import com.fleetmatics.networkmanager.model.NetworkRequestStatus;
import com.fleetmatics.networkmanager.network.NetworkRequestManager;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    @ViewById
    Toolbar toolbar;
    @ViewById
    UltimateRecyclerView ultimateRecyclerView;

    @Bean
    JobsListAdapter adapter;

    @Inject
    NetworkRequestManager networkRequestManager;

    Subscription subscription, subscription2;
    private JobList jobList;

    public MainActivity() {
        MyApplication.getInstance().getGraph().inject(this);
    }

    @AfterViews
    void setup() {
        setSupportActionBar(toolbar);
        ultimateRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ultimateRecyclerView.setAdapter(adapter);
        subscription2 = MyApplication.getInstance().getGCMObserver().subscribe(bundle -> update());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (MyApplication.getInstance().getCustomerIdValue() == null) {
            LoginActivity_.intent(this).start();
        } else {


        }
    }

    private void update() {
        if (subscription != null)
            subscription.unsubscribe();
        subscription = networkRequestManager.executeRequest(
                JobsListExecutor.createRequest(MyApplication.getInstance().getCustomerIdValue())
                , JobList.class)
                .filter(NetworkRequestStatus::isCompleted)
                .filter(NetworkRequestStatus::isNotError)
                .map(NetworkRequestStatus::getData)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        data -> {
                            jobList = data;
                            adapter.setItems(data.getJobs());
                        }
                );
    }

    @Click
    void fab() {
        WorkersMapActivity_.intent(this)
                .jobList(jobList)
                .start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (subscription != null)
            subscription.unsubscribe();

        if (subscription2 != null)
            subscription2.unsubscribe();
    }
}
