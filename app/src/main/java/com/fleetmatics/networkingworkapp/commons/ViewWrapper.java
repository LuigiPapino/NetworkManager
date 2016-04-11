package com.fleetmatics.networkingworkapp.commons;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by nietzsche on 27/12/15.
 */
public class ViewWrapper<V extends View> extends RecyclerView.ViewHolder {

    private V view;

    public ViewWrapper(V itemView) {
        super(itemView);
        view = itemView;
    }


    public V getView() {
        return view;
    }
}
