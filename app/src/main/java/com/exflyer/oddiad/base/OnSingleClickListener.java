
package com.exflyer.oddiad.base;

import android.os.SystemClock;
import android.view.View;

public abstract class OnSingleClickListener implements View.OnClickListener {
    private long interval = 300L;
    private long lastClickTime;

    public OnSingleClickListener() {
        this(300L);
    }

    public OnSingleClickListener(long interval) {
        this.interval = interval;
    }

    @Override
    public final void onClick(View view) {
        long currentClickTime = SystemClock.uptimeMillis();
        long elapsedTime = currentClickTime - lastClickTime;
        lastClickTime = currentClickTime;

        if (elapsedTime <= interval) {
            return;
        }

        onSingleClick(view);
    }

    public abstract void onSingleClick(View view);
}
