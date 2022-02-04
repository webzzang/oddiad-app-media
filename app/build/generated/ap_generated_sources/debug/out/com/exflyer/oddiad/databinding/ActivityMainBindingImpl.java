package com.exflyer.oddiad.databinding;
import com.exflyer.oddiad.R;
import com.exflyer.oddiad.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ActivityMainBindingImpl extends ActivityMainBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.ad_zone, 1);
        sViewsWithIds.put(R.id.main_player_view, 2);
        sViewsWithIds.put(R.id.sub_player_view, 3);
        sViewsWithIds.put(R.id.image_ad_zone, 4);
        sViewsWithIds.put(R.id.main_image, 5);
        sViewsWithIds.put(R.id.right_zone, 6);
        sViewsWithIds.put(R.id.right_image, 7);
        sViewsWithIds.put(R.id.weather_ad_zone, 8);
        sViewsWithIds.put(R.id.weather_location_text, 9);
        sViewsWithIds.put(R.id.weather_icon, 10);
        sViewsWithIds.put(R.id.weather_text_temp_now, 11);
        sViewsWithIds.put(R.id.weather_text_temp_icon, 12);
        sViewsWithIds.put(R.id.weather_text_temp_unit, 13);
        sViewsWithIds.put(R.id.weather_div_line, 14);
        sViewsWithIds.put(R.id.weather_text_view, 15);
        sViewsWithIds.put(R.id.weather_temp_max_level, 16);
        sViewsWithIds.put(R.id.weather_temp_max_level_icon, 17);
        sViewsWithIds.put(R.id.weather_temp_low_level, 18);
        sViewsWithIds.put(R.id.weather_temp_low_level_icon, 19);
        sViewsWithIds.put(R.id.weather_dust_layout, 20);
        sViewsWithIds.put(R.id.weather_fine_dust_state_text, 21);
        sViewsWithIds.put(R.id.weather_fine_dust_text, 22);
        sViewsWithIds.put(R.id.weather_ultra_dust_layout, 23);
        sViewsWithIds.put(R.id.weather_ultra_fine_dust_state_text, 24);
        sViewsWithIds.put(R.id.weather_ultra_fine_dust_text, 25);
        sViewsWithIds.put(R.id.weather_time_date, 26);
        sViewsWithIds.put(R.id.weather_time_clock, 27);
        sViewsWithIds.put(R.id.bottom_zone, 28);
        sViewsWithIds.put(R.id.bottom_image, 29);
        sViewsWithIds.put(R.id.device_id_text_view, 30);
        sViewsWithIds.put(R.id.loading_ad_zone, 31);
        sViewsWithIds.put(R.id.bi_view, 32);
        sViewsWithIds.put(R.id.bi_pro, 33);
        sViewsWithIds.put(R.id.error_zone, 34);
        sViewsWithIds.put(R.id.device_noti, 35);
        sViewsWithIds.put(R.id.bi_text_1, 36);
        sViewsWithIds.put(R.id.bi_text_2, 37);
    }
    // views
    @NonNull
    private final androidx.constraintlayout.widget.ConstraintLayout mboundView0;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ActivityMainBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 38, sIncludes, sViewsWithIds));
    }
    private ActivityMainBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (androidx.constraintlayout.widget.ConstraintLayout) bindings[1]
            , (android.widget.ProgressBar) bindings[33]
            , (android.widget.TextView) bindings[36]
            , (android.widget.TextView) bindings[37]
            , (android.view.View) bindings[32]
            , (android.widget.ImageView) bindings[29]
            , (androidx.constraintlayout.widget.ConstraintLayout) bindings[28]
            , (android.widget.TextView) bindings[30]
            , (android.widget.TextView) bindings[35]
            , (androidx.constraintlayout.widget.ConstraintLayout) bindings[34]
            , (androidx.constraintlayout.widget.ConstraintLayout) bindings[4]
            , (androidx.constraintlayout.widget.ConstraintLayout) bindings[31]
            , (android.widget.ImageView) bindings[5]
            , (com.google.android.exoplayer2.ui.PlayerView) bindings[2]
            , (android.widget.ImageView) bindings[7]
            , (androidx.constraintlayout.widget.ConstraintLayout) bindings[6]
            , (com.google.android.exoplayer2.ui.PlayerView) bindings[3]
            , (androidx.constraintlayout.widget.ConstraintLayout) bindings[8]
            , (android.view.View) bindings[14]
            , (android.widget.LinearLayout) bindings[20]
            , (android.widget.TextView) bindings[21]
            , (android.widget.TextView) bindings[22]
            , (android.widget.ImageView) bindings[10]
            , (android.widget.TextView) bindings[9]
            , (android.widget.TextView) bindings[18]
            , (android.widget.ImageView) bindings[19]
            , (android.widget.TextView) bindings[16]
            , (android.widget.ImageView) bindings[17]
            , (android.widget.ImageView) bindings[12]
            , (android.widget.TextView) bindings[11]
            , (android.widget.TextView) bindings[13]
            , (android.widget.TextView) bindings[15]
            , (android.widget.TextClock) bindings[27]
            , (android.widget.TextClock) bindings[26]
            , (android.widget.LinearLayout) bindings[23]
            , (android.widget.TextView) bindings[24]
            , (android.widget.TextView) bindings[25]
            );
        this.mboundView0 = (androidx.constraintlayout.widget.ConstraintLayout) bindings[0];
        this.mboundView0.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x1L;
        }
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean setVariable(int variableId, @Nullable Object variable)  {
        boolean variableSet = true;
            return variableSet;
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
        }
        return false;
    }

    @Override
    protected void executeBindings() {
        long dirtyFlags = 0;
        synchronized(this) {
            dirtyFlags = mDirtyFlags;
            mDirtyFlags = 0;
        }
        // batch finished
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): null
    flag mapping end*/
    //end
}