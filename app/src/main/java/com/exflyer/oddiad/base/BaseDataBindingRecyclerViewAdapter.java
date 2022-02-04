
package com.exflyer.oddiad.base;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public abstract class BaseDataBindingRecyclerViewAdapter<T, VH extends BaseDataBindingViewHolder, A> extends BaseRecyclerViewAdapter<T, VH, A> {
    protected Resources resources;
    private ArrayList<VH> viewHolders = new ArrayList<>();

    public BaseDataBindingRecyclerViewAdapter(Context context) {
        super(context);

        resources = context.getResources();
    }

    @Override
    public void onBindViewHolder(@NonNull VH viewHolder, int position) {
        if (!viewHolders.contains(viewHolder)) {
            viewHolders.add(viewHolder);
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        for (VH viewHolder : viewHolders) {
            viewHolder.onConfigurationChanged(newConfig);
        }
    }
}
