
package com.exflyer.oddiad.base;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;


public abstract class BaseDataBindingActivity<B extends ViewDataBinding> extends BaseActivity {
    @NonNull
    //@JvmField
    protected B binding;
}
