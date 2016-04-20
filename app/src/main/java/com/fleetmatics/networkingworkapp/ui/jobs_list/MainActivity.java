package com.fleetmatics.networkingworkapp.ui.jobs_list;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.fleetmatics.networkingworkapp.MyApplication;
import com.fleetmatics.networkingworkapp.R;
import com.fleetmatics.networkingworkapp.model.JobList;
import com.fleetmatics.networkingworkapp.model.JobStatus;
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

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (MyApplication.getInstance().getCustomerIdValue() == null) {
            LoginActivity_.intent(this).start();
        } else {
            update();
            subscription2 = MyApplication.getInstance().getGCMObserver()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(bundle -> {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        if (bundle.getString("type").contentEquals("0")) {
                            builder.setTitle(bundle.getString("jobTitle"))
                                    .setMessage("New Status: " + JobStatus.values()[Integer.valueOf(bundle.getString("value"))].toString());
                        } else if (bundle.getString("type").contentEquals("1")) {
                            builder.setTitle(bundle.getString("workerName"))
                                    .setMessage("is in late " + bundle.getString("value"));
                        }
                        builder.setPositiveButton(R.string.ok, null);
                        builder.show();
                        update();
                    });
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
                            Log.d(TAG, "update() called with: " + "");
                            jobList = data;
                            adapter.setItems(data.getJobs());
                            adapter.notifyDataSetChanged();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
