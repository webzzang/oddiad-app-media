
package com.exflyer.oddiad.ui.activity.splash;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.exflyer.oddiad.AppConstants;
import com.exflyer.oddiad.R;
import com.exflyer.oddiad.base.BaseDataBindingActivity;
import com.exflyer.oddiad.databinding.ActivitySplashBinding;
import com.exflyer.oddiad.manager.AudioControlManager;
import com.exflyer.oddiad.network.api.OddiAdApi;
import com.exflyer.oddiad.network.model.devicecreate.DeviceCreateModel;
import com.exflyer.oddiad.preference.UserPreference;
import com.exflyer.oddiad.receiver.BootCompletedReceiver;
import com.exflyer.oddiad.ui.activity.main.MainActivity;
import com.exflyer.oddiad.util.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Response;


public class SplashActivity extends BaseDataBindingActivity<ActivitySplashBinding> {

    private static final int PERMISSIONS_REQUEST_CODE = 4865;
    private static final int REQ_CODE_OVERLAY_PERMISSION = 4866;

    String[] REQUIRED_PERMISSIONS  = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION


    };


    private EventReceiver mEventReceiver = null;
    private String mDeviceId = "";


    private boolean mIsBootCompleted = false;

    private NetworkRequest networkRequest;
    private ConnectivityManager connectivityManager;

    //2021.12.23 ???????????? ????????? ??? ????????? ????????? ?????? ?????? ??????
    private boolean mIsNetWorkLost = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            String tempString = getIntent().getStringExtra("BootCompleted");

            if(tempString !=null)
            {
                if("Y".equals(tempString))
                {
                    mIsBootCompleted = true;
                }
            }
        }catch (Exception e)
        {

        }

        binding = DataBindingUtil.setContentView(SplashActivity.this, R.layout.activity_splash);
        setLoadingBar(true);
//        UserPreference.getInstance().saveDeviceID("tmwwej");
        try
        {
            AudioControlManager.init(this);

            ComponentName receiver = new ComponentName(SplashActivity.this, BootCompletedReceiver.class);
            PackageManager pm = SplashActivity.this.getPackageManager();
            pm.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);


        }catch (Exception e)
        {
            e.printStackTrace();
        }


        if(!Utils.checkRooting(SplashActivity.this))
        {

            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(new OnCompleteListener<String>() {
                        @Override
                        public void onComplete(@NonNull Task<String> task) {
                            if (!task.isSuccessful()) {

                                return;
                            }

                            // Get new FCM registration token
                            UserPreference.getInstance().saveUserFCMID(task.getResult());
//                            ClipboardManager clipboardManager = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
//                            ClipData clipData = ClipData.newPlainText("FCM Token", task.getResult());
//                            clipboardManager.setPrimaryClip(clipData);
//                            Toast.makeText(SplashActivity.this,"FCM Token ??? ??????????????? ?????????????????????.",Toast.LENGTH_SHORT).show();
                        }
                    });

            networkRequest =
                    new NetworkRequest.Builder()                                        // addTransportType : ????????? ?????? ?????? ????????? ????????? ??????
                            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)   // TRANSPORT_CELLULAR : ??? ??????????????? ????????? ????????? ???????????? ???????????????.
                            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)       // TRANSPORT_WIFI : ??? ??????????????? Wi-Fi ????????? ???????????? ???????????????.
                            .build();
            this.connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE); // CONNECTIVITY_SERVICE : ???????????? ?????? ?????? ????????? ??????
            this.connectivityManager.registerNetworkCallback(networkRequest, mNetworkCallback);


        }



    }

    ConnectivityManager.NetworkCallback mNetworkCallback = new ConnectivityManager.NetworkCallback()
    {
        @Override public void onAvailable(Network network)
        {
            checkRunTimePermission();
            super.onAvailable(network);
        }

        @Override public void onLost(Network network)
        {
            super.onLost(network);
        }
    };

    private void checkRunTimePermission(){

        //????????? ????????? ??????
        // 1. ?????? ???????????? ????????? ????????? ???????????????.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);


//
//        //?????????
//        int hasWritePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        int hasReadPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);




        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED
//                && hasWritePermission == PackageManager.PERMISSION_GRANTED
//                && hasReadPermission == PackageManager.PERMISSION_GRANTED

        ) {




            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O && !Settings.canDrawOverlays(SplashActivity.this)) {
                onObtainingPermissionOverlayWindow();

            } else {
                onDeviceCreateCheck();

            }



        } else {  //2. ????????? ????????? ????????? ?????? ????????? ????????? ????????? ???????????????. 2?????? ??????(3-1, 4-1)??? ????????????.

            // 3-1. ???????????? ????????? ????????? ??? ?????? ?????? ????????????
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])) {

                // 3-3. ??????????????? ????????? ????????? ?????????. ?????? ????????? onRequestPermissionResult?????? ???????????????.
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);

            } else {
                // 4-1. ???????????? ????????? ????????? ??? ?????? ?????? ???????????? ????????? ????????? ?????? ?????????.
                // ?????? ????????? onRequestPermissionResult?????? ???????????????.
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            }

        }

    }

    /*
     * ActivityCompat.requestPermissions??? ????????? ????????? ????????? ????????? ???????????? ??????????????????.
     */
    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        super.onRequestPermissionsResult(permsRequestCode, permissions, grandResults);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O && !Settings.canDrawOverlays(SplashActivity.this)) {
            onObtainingPermissionOverlayWindow();

        } else {
            onDeviceCreateCheck();
        }

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
        setLoadingBar(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        initEventReceiver();
        UserPreference.getInstance().saveBootCompleted("N");
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setLoadingBar(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyEventReceiver();
        this.connectivityManager.unregisterNetworkCallback( mNetworkCallback);
        setLoadingBar(false);
    }

    public void onObtainingPermissionOverlayWindow() {
        try {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, REQ_CODE_OVERLAY_PERMISSION);
        }catch (Exception e)
        {
            e.printStackTrace();
            reTryRequest();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_OVERLAY_PERMISSION) {
            if (!Settings.canDrawOverlays(SplashActivity.this)) {
                // TODO ????????? ?????? ????????? ????????? ??????
                reTryRequest();
            } else {
                onDeviceCreateCheck();
            }
        }
    }

    private void reTryRequest()
    {
        Toast.makeText(SplashActivity.this,"???->?????? ??? ?????????->?????? ??? ?????? ??????->\n??????AD ???????????? APP ????????? ??????????????????.",Toast.LENGTH_LONG).show();
        startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), REQ_CODE_OVERLAY_PERMISSION);
    }

    private void nextPage()
    {
        MainActivity.startActivity(SplashActivity.this);
        finish();
    }

    private void onDeviceCreateCheck()
    {
        mOneTimeEventHandler.removeMessages(1);
        mOneTimeEventHandler.sendEmptyMessageDelayed(1, 1000);
    }


    private void initEventReceiver() {

        if(mEventReceiver == null)
        {
            mEventReceiver = new EventReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(AppConstants.LOCAL_BROADCAST_EVENT_RECEIVER);
            LocalBroadcastManager.getInstance(this).registerReceiver(mEventReceiver, filter);
        }
    }

    private void destroyEventReceiver() {
        if (mEventReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mEventReceiver);
            mEventReceiver = null;
        }
    }

    private class EventReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if(binding!=null)
            {
                if(intent.getExtras().getString("DATA")!=null)
                {


                    try
                    {
                        JSONObject jsonObject = new JSONObject(intent.getExtras().getString("DATA"));

                        if(jsonObject.get(AppConstants.PUSH_ACTION) !=null)
                        {
                            if(AppConstants.ACTION_DEFINITION_DEVICE_CREATE.equals(jsonObject.get(AppConstants.PUSH_ACTION)))
                            {
                                UserPreference.getInstance().saveDeviceID(mDeviceId);

                                if(jsonObject.get(AppConstants.PUSH_ID) !=null)
                                {
                                    sendFCMResponse(jsonObject.get(AppConstants.PUSH_ID).toString());
                                }

                                nextPage();
                            }
                        }

                    }catch (Exception e)
                    {

                    }
                }
            }
        }
    }


    Handler mOneTimeEventHandler = new Handler(Looper.getMainLooper())
    {
        @Override
        public void handleMessage(Message msg)
        {
            try
            {
                mOneTimeEventHandler.removeMessages(1);

                if(UserPreference.getInstance().getDeviceID() !=null && !UserPreference.getInstance().getDeviceID().equals(""))
                {
                    nextPage();
                }
                else
                {
                    if(UserPreference.getInstance().getUserFCMID() !=null && !UserPreference.getInstance().getUserFCMID().equals(""))
                    {

                        String fcm = UserPreference.getInstance().getUserFCMID();
                        String model = android.os.Build.MODEL;
                        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                        OddiAdApi.getInstance().onDeviceCreate(fcm,model,androidId).enqueue(new retrofit2.Callback<DeviceCreateModel>() {
                            @Override
                            public void onResponse(Call<DeviceCreateModel> call, Response<DeviceCreateModel> response) {
//                                binding.notiZone.setVisibility(View.VISIBLE);
                                try
                                {
                                    if(response.body().getData().getDevice_id() !=null)
                                    {
                                        setLoadingBar(false);
                                        mDeviceId = response.body().getData().getDevice_id();
                                        binding.deviceIdText.setVisibility(View.VISIBLE);
                                        binding.deviceNoti.setVisibility(View.VISIBLE);
                                        binding.deviceIdText.setText(response.body().getData().getDevice_id());
                                    }
                                }
                                catch (Exception e)
                                {

                                }



//                                mOneTimeEventHandler.postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        UserPreference.getInstance().saveDeviceID(mDeviceId);
//                                        nextPage();
//                                    }
//                                },30000);



                            }

                            @Override
                            public void onFailure(Call<DeviceCreateModel> call, Throwable t) {

                            }
                        });

                    }
                    else
                    {
                        Toast.makeText(SplashActivity.this,"FCM Token ??? ???????????? ?????? ????????? ??????????????? ????????????.",Toast.LENGTH_SHORT).show();
                        finish();
                    }

                }
            }
            catch (Exception e)
            {
                mOneTimeEventHandler.removeMessages(1);
            }
        }
    };


    private int mLoadingCount = 0;
    private void setLoadingBar(boolean isVisible)
    {
        if(isVisible)
        {

            binding.loadingZone.setVisibility(View.VISIBLE);
            mLoadingCount = 4;
            mLoadingBarHandler.removeMessages(1);
            mLoadingBarHandler.sendEmptyMessageDelayed(1,50);


        }
        else
        {
            mLoadingCount = 4;
            binding.loadingZone.setVisibility(View.GONE);
            mLoadingBarHandler.removeMessages(1);
        }
    }

    Handler mLoadingBarHandler = new Handler(Looper.getMainLooper())
    {
        @Override
        public void handleMessage(Message msg)
        {
            try
            {

                mLoadingBarHandler.removeMessages(1);
                mLoadingCount = mLoadingCount +2;
                if(mLoadingCount >= 100)
                {
                    mLoadingCount = 0;
                }
                binding.biPro.setProgress(mLoadingCount);
                mLoadingBarHandler.sendEmptyMessageDelayed(1,50);

            }
            catch (Exception e)
            {

            }
        }
    };

    Handler mBootCompletedHandler = new Handler(Looper.getMainLooper())
    {
        @Override
        public void handleMessage(Message msg)
        {
            try
            {

                mBootCompletedHandler.removeMessages(1);
//                onDeviceCreateCheck();
                checkRunTimePermission();

            }
            catch (Exception e)
            {

            }
        }
    };
}