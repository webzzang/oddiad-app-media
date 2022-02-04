
package com.exflyer.oddiad.base;

import android.content.Context;
import android.content.res.Configuration;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseDataBindingViewHolder<B extends ViewDataBinding, D> extends RecyclerView.ViewHolder {
    @NonNull
    protected B binding;

    @NonNull
    protected Context context;

    public BaseDataBindingViewHolder(B binding, Context context) {
        super(binding.getRoot());

        this.binding = binding;
        this.context = context;
    }

    public abstract void bind(D data);

    public B getBinding() {
        return binding;
    }

    public void onConfigurationChanged(Configuration newConfig) {
    }
}
