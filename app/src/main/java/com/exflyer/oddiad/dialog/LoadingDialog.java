
package com.exflyer.oddiad.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.exflyer.oddiad.R;


public class LoadingDialog extends Dialog {
    private ImageView dialogLoadingProgressBar;


    public LoadingDialog(Context context) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getWindow();
        if (window != null) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            window.setBackgroundDrawableResource(R.color.color_transparent);
        }



        setContentView(R.layout.dialog_loading);
        setCancelable(false);
        setCanceledOnTouchOutside(false);

//        dialogLoadingProgressBar = findViewById(R.id.dialog_loading_progress_bar);
//
//        AnimationDrawable frameAnimation = (AnimationDrawable) dialogLoadingProgressBar.getBackground();
//        dialogLoadingProgressBar.post(() -> frameAnimation.start());
    }

    @Override
    public void show() {
        super.show();

    }
}
