package com.fleetmatics.networkingworkapp.ui.jobs_list;

import android.content.Context;
import android.view.View;

import com.fleetmatics.networkingworkapp.commons.UltimateRecyclerViewAdapterBase;
import com.fleetmatics.networkingworkapp.commons.ViewWrapper;
import com.fleetmatics.networkingworkapp.model.Job;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

/**
 * Created by luigi.papino on 19/04/16.
 */
@EBean
public class JobsListAdapter extends UltimateRecyclerViewAdapterBase<Job, JobView> {

    @RootContext
    Context context;

    @Override
    protected JobView onCreateItemView(View parent, int viewType) {
        return JobView_.build(context);
    }

    @Override
    public void onBindViewHolder(ViewWrapper<View> holder, int position) {
        if (holder.getView() != null && holder.getView() instanceof JobView) {
            JobView view = (JobView) holder.getView();
            Job item = items.get(position);
            view.bind(item);
        }
    }
}
