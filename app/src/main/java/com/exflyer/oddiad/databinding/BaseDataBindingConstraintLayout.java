
package com.exflyer.oddiad.databinding;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.ViewDataBinding;


public abstract class BaseDataBindingConstraintLayout<B extends ViewDataBinding> extends ConstraintLayout {
    @NonNull
    //@JvmField
    protected B binding;

    public BaseDataBindingConstraintLayout(Context context) {
        super(context);
    }

    public BaseDataBindingConstraintLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseDataBindingConstraintLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
