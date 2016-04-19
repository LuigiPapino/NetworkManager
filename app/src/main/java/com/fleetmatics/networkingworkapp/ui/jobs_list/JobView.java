package com.fleetmatics.networkingworkapp.ui.jobs_list;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fleetmatics.networkingworkapp.R;
import com.fleetmatics.networkingworkapp.model.Job;
import com.fleetmatics.networkingworkapp.model.JobStatus;
import com.fleetmatics.networkingworkapp.ui.job_map.JobMapActivity_;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.text.SimpleDateFormat;

/**
 * Created by luigi.papino on 19/04/16.
 */
@EViewGroup(R.layout.view_job)
public class JobView extends RelativeLayout {
    public static SimpleDateFormat sdfETA = new SimpleDateFormat("HH:mm");
    static SimpleDateFormat sdf = new SimpleDateFormat("dd MMM HH:mm");
    @ViewById
    TextView title;
    @ViewById
    TextView scheduledTime;
    @ViewById
    TextView eta;
    @ViewById
    TextView status;
    private Job job;

    public JobView(Context context) {
        super(context);
    }

    public JobView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public JobView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public JobView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void bind(@NonNull Job job) {
        this.job = job;

        title.setText(job.getTitle());
        scheduledTime.setText(sdf.format(job.getScheduleTime()));
        eta.setText("ETA " + sdfETA.format(job.getEstimateTime()));
        status.setText(JobStatus.values()[job.getStatus()].toString());
        eta.setVisibility(job.getStatus() == 1 ? VISIBLE : INVISIBLE);

    }

    @Click
    void mainLayout() {
        JobMapActivity_.intent(getContext())
                .job(job)
                .start();
    }
}
