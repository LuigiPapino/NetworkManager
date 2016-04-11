package com.fleetmatics.networkingworkapp.commons;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nietzsche on 27/12/15.
 */
public abstract class UltimateRecyclerViewAdapterBase<T, V extends View> extends UltimateViewAdapter<ViewWrapper<View>> {

    public List<T> items = new ArrayList<T>();
    protected static final int TYPE_ITEM = 0;
    protected static final int TYPE_HEADER = 1;

    public boolean isEmpty() {
        return items != null && items.size() == 0;
    }

    protected abstract V onCreateItemView(View parent, int viewType);


    @Override
    public long generateHeaderId(int i) {
        return 0;
    }

    // additional methods to manipulate the items

    @Override
    public ViewWrapper<View> getViewHolder(View view) {

            return new ViewWrapper<View>(view);

    }

    @Override
    public ViewWrapper<View> onCreateViewHolder(ViewGroup viewGroup) {
        return new ViewWrapper<View>(onCreateItemView(viewGroup, TYPE_ITEM));
    }

    @Override
    public int getAdapterItemCount() {
        return items.size();
    }

    @Override
    public abstract void onBindViewHolder(ViewWrapper<View> holder, int position);


    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup viewGroup) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder, int i) {

    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
        notifyDataSetChanged();
    }
}