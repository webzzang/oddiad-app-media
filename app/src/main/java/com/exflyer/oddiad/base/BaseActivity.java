
package com.exflyer.oddiad.base;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.exflyer.oddiad.OddiAdApplication;
import com.exflyer.oddiad.R;
import com.exflyer.oddiad.dialog.LoadingDialog;
import com.exflyer.oddiad.network.api.OddiAdApi;
import com.exflyer.oddiad.network.model.common.CommonResponse;
import com.exflyer.oddiad.util.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public abstract class BaseActivity extends Activity {
    //private LoadingDialog loadingDialog;


    private boolean needPermissionCheck = false;
    private LoadingDialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setTopNavigationBarTranslucent();
        super.onCreate(savedInstanceState);

        topBaseActivity = this;

        hideSystemUI();
    }

    // TODO : should be prettry code.
    private static BaseActivity topBaseActivity;


    public static void killThemAll() {
        try {
            if (topBaseActivity != null) {
                OddiAdApplication.killApp(topBaseActivity);
            }
        } catch (Exception e) {

        }
    }

    protected final void needPermissionCheck(boolean needPermissionCheck) {
        this.needPermissionCheck = needPermissionCheck;
    }

    @Override
    protected void onResume() {
        super.onResume();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if(Utils.checkRooting(BaseActivity.this))
        {
            Toast.makeText(BaseActivity.this,getString(R.string.user_rooting_use_kill_app),Toast.LENGTH_LONG).show();
            killThemAll();
        }
    }

    @Override
    protected void onPause() {

        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected void hideKeyBoard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    protected void showKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

//    public void forceHideNavigationBarWhenLoadingShowing(boolean forceHideNavigationBarWhenLoadingShowing) {
//        this.forceHideNavigationBarWhenLoadingShowing = forceHideNavigationBarWhenLoadingShowing;
//    }

    public final void showLoading() {
        try {
            if (loadingDialog == null) {
                loadingDialog = new LoadingDialog(this);
            }

            if (!loadingDialog.isShowing()) {
                if(!BaseActivity.this.isFinishing())
                    loadingDialog.show();
            }
        }catch (Exception e)
        {

        }

    }

    public final void hideLoading() {
        try {
            if (loadingDialog != null) {
                loadingDialog.dismiss();
            }
        }catch (Exception e)
        {

        }

    }

    public  boolean isShowLoading() {
        if (loadingDialog != null) {
            return loadingDialog.isShowing();
        }
        else
        {
            return false;
        }

    }

    public void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(0);
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    public void hideSystemUI() {
        int visibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY|
                View.SYSTEM_UI_FLAG_FULLSCREEN;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            visibility |= View.SYSTEM_UI_FLAG_IMMERSIVE;
        }
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(visibility);
    }

    public void sendFCMResponse(String fcm_id)
    {
        OddiAdApi.getInstance().onPushResponse(fcm_id).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {

            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {

            }
        });
    }
}
