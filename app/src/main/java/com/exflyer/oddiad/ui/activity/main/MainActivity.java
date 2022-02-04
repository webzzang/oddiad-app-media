package com.exflyer.oddiad.ui.activity.main;

import static android.content.Intent.ACTION_VIEW;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
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
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Util;
import com.exflyer.oddiad.AppConstants;
import com.exflyer.oddiad.R;
import com.exflyer.oddiad.alarm.Alarm;
import com.exflyer.oddiad.base.BaseDataBindingActivity;
import com.exflyer.oddiad.databinding.ActivityMainBinding;

import com.exflyer.oddiad.manager.FileDownloadManager;
import com.exflyer.oddiad.network.api.OddiAdApi;
import com.exflyer.oddiad.network.model.bottomad.BottomAdModel;
import com.exflyer.oddiad.network.model.common.AdList;
import com.exflyer.oddiad.network.model.common.CommonResponse;
import com.exflyer.oddiad.network.model.mainad.MainAdModel;
import com.exflyer.oddiad.network.model.sidead.SideAdModel;
import com.exflyer.oddiad.network.model.weather.WeatherModel;
import com.exflyer.oddiad.preference.UserPreference;
import com.exflyer.oddiad.util.MyExceptionHandler;
import com.exflyer.oddiad.util.Utils;
import com.exflyer.oddiad.network.model.ContentsDetailInfo;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends BaseDataBindingActivity<ActivityMainBinding> {

    private long backKeyPressedTime = 0;



    private EventReceiver mEventReceiver =null;
    private Handler mCommonHandler;


    private SimpleExoPlayer mMainPlayer = null;
    private SimpleExoPlayer mSubPlayer = null;

    private Boolean mMainPlayWhenReady = false;
    private Boolean mSubPlayWhenReady = false;

    private int mADNextCheckTime = 5000;

    //메인화면 광고 처리 부
    private ArrayList<AdList> mMainAdList = new ArrayList<>();

    private String mMainCurrentAD = "NONE";
    private int mMainCurrentADIndex = 0;

    private int mMainImageADTotalDurationTime = 15000;
    private int mMainImageADDurationTime = 0;
    private int mMainImageADPosition = 0;
    private int mMainImageADCount = 0;


    private ArrayList<AdList> mBottomAdList = new ArrayList<>();
    private int mBottomCurrentADIndex = 0;

    private int mBottomImageADTotalDurationTime = 15000;
    private int mBottomImageADDurationTime = 0;
    private int mBottomImageADPosition = 0;
    private int mBottomImageADCount = 0;



    private ArrayList<AdList> mSideAdList = new ArrayList<>();
    private int mSideCurrentADIndex = 0;

    private int mSideImageADTotalDurationTime = 15000;
    private int mSideImageADDurationTime = 0;
    private int mSideImageADPosition = 0;
    private int mSideImageADCount = 0;

    //위치정보 처리
    private LocationManager locationManager;
    private double mLocationLongitude =0;
    private double LocationmLatitude = 0;

    private boolean mIsPauseFlag = false;

    //FCM 으로 전송된 Action 으로 각광고가 끝나고 리스트를 새로 받아서 처리 한다.
    private boolean mIsMainAdChangeFlag = false;
    private boolean mIsSideAdChangeFlag = false;
    private boolean mIsBottomAdChangeFlag = false;

    //FCM 으로 전송된 Action 으로 각광고가 끝나고 화면을 숨김처리 한다.
    private boolean mIsSideAdFinishFlag = false;
    private boolean mIsBottomAdFinishFlag = false;

//    SimpleDateFormat timeFormat = new SimpleDateFormat("aa hh:mm:ss\nyyyy.MM.dd");

    //2021.24시간 기준 광고 갱신 로직 주석 처리
    //private String mADRequestDate = "";

    //2021.12.23 네트워크 단절후 재 연결시 모든걸 새로 시작 처리
    private boolean mIsNetWorkLost = false;

    private String mContentScreenType="divisions_1";

    private boolean mIsLowLevel = false;

    private ArrayList<ContentsDetailInfo> contentList = new ArrayList<>();
    private boolean mIsContentsDownLoadPlay = false;

    private boolean mIsUserFinishFlag = false;


    private String mTimestampMain ="";
    private String mTimestampSide ="";
    private String mTimestampBottom ="";

    int mRetryCount = 0;
    int mRetryMaxCount = 10;
    int mRetryDelayTime = 30000;

    private NetworkRequest networkRequest;
    private ConnectivityManager connectivityManager;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);

        binding.deviceIdTextView.setText("Device_ID : "+UserPreference.getInstance().getDeviceID());
        mCommonHandler = new Handler();

        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));
        setLoadingBar(true);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P)
        {
            mIsLowLevel = true;
        }

        //광고APP 시작을 알림

        sendDeviceStates("",AppConstants.DEVICE_STATES_TYPE_START);


        UserPreference.getInstance().saveExceptionTime(Utils.nowTimeYYYYMMDDHHMMSS());
        binding.weatherTimeDate.setTimeZone("Asia/Seoul");
        binding.weatherTimeDate.setFormat12Hour("aa h:mm");
        binding.weatherTimeDate.setFormat24Hour("aa h:mm");
        binding.weatherTimeClock.setTimeZone("Asia/Seoul");
        binding.weatherTimeClock.setFormat12Hour("yyyy년 MM월dd일 EE요일");
        binding.weatherTimeClock.setFormat24Hour("yyyy년 MM월dd일 EE요일");

        hideSystemUI();

        getMainAD();
//        setTestAD();

        networkRequest =
                new NetworkRequest.Builder()                                        // addTransportType : 주어진 전송 요구 사항을 빌더에 추가
                        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)   // TRANSPORT_CELLULAR : 이 네트워크가 셀룰러 전송을 사용함을 나타냅니다.
                        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)       // TRANSPORT_WIFI : 이 네트워크가 Wi-Fi 전송을 사용함을 나타냅니다.
                        .build();
        this.connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE); // CONNECTIVITY_SERVICE : 네트워크 연결 관리 처리를 검색
        this.connectivityManager.registerNetworkCallback(networkRequest, mNetworkCallback);
    }

    ConnectivityManager.NetworkCallback mNetworkCallback = new ConnectivityManager.NetworkCallback()
    {
        @Override public void onAvailable(Network network)
        {
            if(mIsNetWorkLost)
            {
                mIsNetWorkLost =false;
                mIsMainAdChangeFlag = true;
            }
            super.onAvailable(network);
        }

        @Override public void onLost(Network network)
        {
            mIsNetWorkLost = true;
            super.onLost(network);
        }
    };


//    private void setTestAD()
//    {
//        setLoadingBar(false);
//        binding.adZone.setVisibility(View.VISIBLE);
//        binding.imageAdZone.setVisibility(View.VISIBLE);
//        Glide.with(getApplicationContext())
//                .load("https://skd-s3.s3.ap-northeast-2.amazonaws.com/oddiad/attachments/69fc601e690c439f93d1b6ed38ad10b9.jpeg")
//                .transition(DrawableTransitionOptions.withCrossFade(800))
//                .into(binding.mainImage);
//
//        Glide.with(getApplicationContext())
//                .load(R.drawable.temp3)
//                .transition(DrawableTransitionOptions.withCrossFade(800))
//                .into(binding.bottomImage);
//
//        Glide.with(getApplicationContext())
//                .load(R.drawable.temp2)
//                .transition(DrawableTransitionOptions.withCrossFade(800))
//                .into(binding.rightImage);
//
//        binding.rightZone.setVisibility(View.VISIBLE);
//        binding.bottomZone.setVisibility(View.VISIBLE);
//    }


    private void getLocation()
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if (lastKnownLocation != null) {
            mLocationLongitude = lastKnownLocation.getLongitude();
            LocationmLatitude = lastKnownLocation.getLatitude();
        }

        Location lastKnownLocation_GPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (lastKnownLocation_GPS != null) {
            mLocationLongitude = lastKnownLocation_GPS.getLongitude();
            LocationmLatitude = lastKnownLocation_GPS.getLatitude();
        }

    }


    private void getMainAD()
    {

        //2021.24시간 기준 광고 갱신 로직 주석 처리
//        mADRequestDate = Utils.nowTimeYYYYMMDD();
        mIsContentsDownLoadPlay = false;
        try {
            //다운로드 파일이 없는 버전이라 주석처리
            //Utils.delDownLoadFile();
        }catch (Exception e)
        {

        }

        getLocation();

        OddiAdApi.getInstance().getMainAD(LocationmLatitude==0 ?"":""+LocationmLatitude ,mLocationLongitude==0 ?"":""+mLocationLongitude).enqueue(new Callback<MainAdModel>() {
            @Override
            public void onResponse(Call<MainAdModel> call, Response<MainAdModel> response) {
                try {
                    if(response !=null &&  response.body() !=null &&  response.body().getData() !=null)
                    {
                        if(response.body().getData().ad_list !=null && response.body().getData().ad_list.size() >0)
                        {
                            mRetryCount = 0;
                            mMainAdList = (ArrayList<AdList>) response.body().getData().ad_list;
                            onMainNextADCheck();

                            if(response.body().getData().ad_screen_type !=null)
                            {
                                setDivision(response.body().getData().ad_screen_type);
                            }
                            else
                            {
                                if(binding !=null)
                                {
                                    binding.rightZone.setVisibility(View.GONE);
                                    binding.bottomZone.setVisibility(View.GONE);
                                }
                            }

                            if(response.body().getData().ad_type !=null && response.body().getData().ad_type.equals("download"))
                            {
                                mIsContentsDownLoadPlay = true;
                                for(int i = 0; i < mMainAdList.size(); i++)
                                {
                                    if(mMainAdList.get(i).getContent_type().equals("video"))
                                    {
                                        ContentsDetailInfo item = new ContentsDetailInfo();
                                        item.setDownload_yn(false);
                                        item.setDownload_url(mMainAdList.get(i).getContent_url().get(0));
                                        item.setContent_id(mMainAdList.get(i).getContent_id());
                                        String fileName = item.getDownload_url().substring(item.getDownload_url().lastIndexOf("/")+1,item.getDownload_url().length());
                                        item.setFile_name(fileName);
                                        contentList.add(item);
                                    }
                                }
                                startDownLoadFile();
                            }
                        }
                        else
                        {
                            if(mRetryCount < mRetryMaxCount)
                            {
                                mRetryCount++;
                                mRetryHandler.removeMessages(1);
                                mRetryHandler.sendEmptyMessageDelayed(1,mRetryDelayTime);
                            }
                            else
                            {
                                if(binding!=null)
                                {
                                    setLoadingBar(false);
                                    mRetryHandler.removeMessages(1);
                                    binding.errorZone.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }
                    else
                    {
                        if(mRetryCount < mRetryMaxCount)
                        {
                            mRetryCount++;
                            mRetryHandler.removeMessages(1);
                            mRetryHandler.sendEmptyMessageDelayed(1,mRetryDelayTime);
                        }
                        else
                        {
                            if(binding!=null)
                            {
                                setLoadingBar(false);
                                mRetryHandler.removeMessages(1);
                                binding.errorZone.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }catch (Exception e)
                {
                    if(mRetryCount < mRetryMaxCount)
                    {
                        mRetryCount++;
                        mRetryHandler.removeMessages(1);
                        mRetryHandler.sendEmptyMessageDelayed(1,mRetryDelayTime);
                    }
                    else
                    {
                        if(binding!=null)
                        {
                            setLoadingBar(false);
                            mRetryHandler.removeMessages(1);
                            binding.errorZone.setVisibility(View.VISIBLE);
                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<MainAdModel> call, Throwable t) {
                if(mRetryCount < mRetryMaxCount)
                {
                    mRetryCount++;
                    mRetryHandler.removeMessages(1);
                    mRetryHandler.sendEmptyMessageDelayed(1,mRetryDelayTime);
                }
                else
                {
                    if(binding!=null)
                    {
                        setLoadingBar(false);
                        mRetryHandler.removeMessages(1);
                        binding.errorZone.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }


    private void setDivision(String division)
    {
//        Ex) "divisions_1" -> 메인광고 full 화면
//        "divisions_2" -> 메인광고 + 오른쪽 배너 광고 화면
//        "divisions_3" -> 메인광고 + 오른쪽 배너 + 하단 배너 광고 화면
        mContentScreenType = division;
        if(division.equals(AppConstants.SCREEN_TYPE_DIVISION_1))
        {
            binding.rightZone.setVisibility(View.GONE);
            binding.bottomZone.setVisibility(View.GONE);
        }
        else if(division.equals(AppConstants.SCREEN_TYPE_DIVISION_2))
        {
            getSideAD();
            binding.bottomZone.setVisibility(View.GONE);
        }
        else if(division.equals(AppConstants.SCREEN_TYPE_DIVISION_3))
        {
            getSideAD();
            getBottomAD();
        }

    }

    private void getBottomAD()
    {
        OddiAdApi.getInstance().getBottomAD().enqueue(new Callback<BottomAdModel>() {
            @Override
            public void onResponse(Call<BottomAdModel> call, Response<BottomAdModel> response) {

                try {

                    if(response !=null &&  response.body().getData()!=null &&response.body().getData().ad_list !=null && response.body().getData().ad_list.size() >0)
                    {
                        mBottomAdList = (ArrayList<AdList>) response.body().getData().ad_list;
                        initBottomImageAD();
                    }
                    else
                    {
                        binding.bottomZone.setVisibility(View.GONE);
                    }
                }catch (Exception e)
                {

                }

            }

            @Override
            public void onFailure(Call<BottomAdModel> call, Throwable t) {
                binding.bottomZone.setVisibility(View.GONE);
            }
        });
    }


    private void getSideAD()
    {
        OddiAdApi.getInstance().getSideAD().enqueue(new Callback<SideAdModel>() {
            @Override
            public void onResponse(Call<SideAdModel> call, Response<SideAdModel> response) {
                try
                {
                    if(response !=null && response.body().getData() !=null &&response.body().getData().ad_list !=null && response.body().getData().ad_list.size() >0)
                    {
                        mSideAdList = (ArrayList<AdList>) response.body().getData().ad_list;
                        initSideAD();
                    }
                    else
                    {
                        binding.rightZone.setVisibility(View.GONE);
                    }
                }catch (Exception e)
                {

                }

            }

            @Override
            public void onFailure(Call<SideAdModel> call, Throwable t) {
                binding.rightZone.setVisibility(View.GONE);
            }
        });

    }

    private void onMainNextADCheck()
    {
        if(mMainCurrentAD.equals("NONE"))
        {
            if(mMainAdList.get(mMainCurrentADIndex).getContent_type().equals("image"))
            {
                mMainCurrentAD = mMainAdList.get(mMainCurrentADIndex).getContent_type();
                binding.adZone.setVisibility(View.VISIBLE);
                //컨텐츠 재생상태 처리 시작
                mTimestampMain =  Utils.getNowTimestamp();
                sendContentStates(
                        mMainAdList.get(mMainCurrentADIndex).getContent_id(),
                        AppConstants.CONTENT_STATES_TYPE_START,
                        mMainAdList.get(mMainCurrentADIndex).getContent_type(),
                        AppConstants.CONTENT_SCREEN_POSITION_MAIN, mTimestampMain);

                if(mMainAdList !=null && mMainAdList.size() >0)
                {
                    sendDeviceStates(mMainAdList.get(mMainCurrentADIndex).getContent_id(),AppConstants.DEVICE_STATES_TYPE_RUN);
                }

                initMainImageAD();
            }
            else
            {
                binding.adZone.setVisibility(View.VISIBLE);
                setMainPlayer(mMainAdList.get(mMainCurrentADIndex).getContent_url().get(0));
            }
        }
        else
        {
            if(!mIsLowLevel)
            {
                int nextADIndex = 0;

                if(mMainAdList.size() > mMainCurrentADIndex+1)
                {
                    nextADIndex = mMainCurrentADIndex + 1;
                }
                else
                {
                    nextADIndex = 0;
                }

                if(mMainAdList.get(nextADIndex).getContent_type().equals("video"))
                {
                    if(binding.mainPlayerView.getVisibility() == View.VISIBLE)
                    {
                        setSubPlayer(mMainAdList.get(nextADIndex).getContent_url().get(0));
                    }
                    else if(binding.subPlayerView.getVisibility() == View.VISIBLE)
                    {
                        setMainPlayer(mMainAdList.get(nextADIndex).getContent_url().get(0));
                    }
                    else
                    {
                        setMainPlayer(mMainAdList.get(nextADIndex).getContent_url().get(0));
                    }
                }
            }

        }
    }

    private String getMainAdNextType()
    {
        String returnString = "";
        try {
            int nextADIndex = 0;
            if(mMainAdList.size() > mMainCurrentADIndex+1)
            {
                nextADIndex = mMainCurrentADIndex + 1;
            }
            else
            {
                nextADIndex = 0;
            }
            returnString = mMainAdList.get(nextADIndex).getContent_type();
        }catch (Exception e)
        {
            returnString ="";
        }
        return  returnString;
    }

    private void onMainNextADPlay()
    {
        if(mIsMainAdChangeFlag)
        {
            clearADProcess();
            releasePlayer();
            mIsMainAdChangeFlag = false;
            getMainAD();
            return;
        }

        //2021.24시간 기준 광고 갱신 로직 주석 처리
//        if(!mADRequestDate.equals(Utils.nowTimeYYYYMMDD()))
//        {
//            clearADProcess();
//            releasePlayer();
//            getMainAD();
//            return;
//        }

        if(mMainAdList.size() > mMainCurrentADIndex+1)
        {
            mMainCurrentADIndex ++;
        }
        else
        {
            mMainCurrentADIndex =0;
        }

        mMainCurrentAD = mMainAdList.get(mMainCurrentADIndex).getContent_type();
        if(mMainCurrentAD.equals("image"))
        {

            mMainVideoADHandler.removeMessages(1);
            mMainVideoADHandler.removeMessages(2);
            mMainVideoADHandler.removeMessages(3);

            binding.mainPlayerView.setVisibility(View.GONE);
            binding.subPlayerView.setVisibility(View.GONE);
            releasePlayer();
            binding.imageAdZone.setVisibility(View.VISIBLE);

            //컨텐츠 재생상태 처리 시작
            mTimestampMain =  Utils.getNowTimestamp();
            sendContentStates(
                    mMainAdList.get(mMainCurrentADIndex).getContent_id(),
                    AppConstants.CONTENT_STATES_TYPE_START,
                    mMainAdList.get(mMainCurrentADIndex).getContent_type(),
                    AppConstants.CONTENT_SCREEN_POSITION_MAIN, mTimestampMain);

            if(mMainAdList !=null && mMainAdList.size() >0)
            {
                sendDeviceStates(mMainAdList.get(mMainCurrentADIndex).getContent_id(),AppConstants.DEVICE_STATES_TYPE_RUN);
            }

            initMainImageAD();
        }
        else
        {
            mMainImageADHandler.removeMessages(1);
            mMainImageADHandler.removeMessages(2);
            mMainImageADHandler.removeMessages(3);
            binding.imageAdZone.setVisibility(View.GONE);
            binding.mainImage.setVisibility(View.GONE);
            setLoadingBar(false);

            if(binding.mainPlayerView.getVisibility() == View.VISIBLE)
            {

                binding.mainPlayerView.setVisibility(View.GONE);
                if(mIsLowLevel)
                {
                    releaseMainPlayer();
                    binding.subPlayerView.setVisibility(View.VISIBLE);
                    setSubPlayer(mMainAdList.get(mMainCurrentADIndex).getContent_url().get(0));
                    mSubPlayer.setPlayWhenReady(true);
                    //컨텐츠 재생상태 처리 시작
                    mTimestampMain =  Utils.getNowTimestamp();
                    sendContentStates(
                            mMainAdList.get(mMainCurrentADIndex).getContent_id(),
                            AppConstants.CONTENT_STATES_TYPE_START,
                            mMainAdList.get(mMainCurrentADIndex).getContent_type(),
                            AppConstants.CONTENT_SCREEN_POSITION_MAIN, mTimestampMain);

                    if(mMainAdList !=null && mMainAdList.size() >0)
                    {
                        sendDeviceStates(mMainAdList.get(mMainCurrentADIndex).getContent_id(),AppConstants.DEVICE_STATES_TYPE_RUN);
                    }
                }
                else
                {
                    if(mSubPlayWhenReady)
                    {
                        releaseMainPlayer();
                        binding.subPlayerView.setVisibility(View.VISIBLE);
                        mSubPlayer.setPlayWhenReady(true);
                        mMainVideoADHandler.sendEmptyMessageDelayed(1, Integer.parseInt(mMainAdList.get(mMainCurrentADIndex).getContent_duration()) - mADNextCheckTime);
                        //컨텐츠 재생상태 처리 시작
                        mTimestampMain =  Utils.getNowTimestamp();
                        sendContentStates(
                                mMainAdList.get(mMainCurrentADIndex).getContent_id(),
                                AppConstants.CONTENT_STATES_TYPE_START,
                                mMainAdList.get(mMainCurrentADIndex).getContent_type(),
                                AppConstants.CONTENT_SCREEN_POSITION_MAIN, mTimestampMain);

                        if(mMainAdList !=null && mMainAdList.size() >0)
                        {
                            sendDeviceStates(mMainAdList.get(mMainCurrentADIndex).getContent_id(),AppConstants.DEVICE_STATES_TYPE_RUN);
                        }
                    }
                }

            }
            else if(binding.subPlayerView.getVisibility() == View.VISIBLE)
            {
                binding.subPlayerView.setVisibility(View.GONE);
                if(mIsLowLevel)
                {
                    releaseSubPlayer();
                    binding.mainPlayerView.setVisibility(View.VISIBLE);
                    setMainPlayer(mMainAdList.get(mMainCurrentADIndex).getContent_url().get(0));
                    mMainPlayer.setPlayWhenReady(true);
                    //컨텐츠 재생상태 처리 시작
                    mTimestampMain =  Utils.getNowTimestamp();
                    sendContentStates(
                            mMainAdList.get(mMainCurrentADIndex).getContent_id(),
                            AppConstants.CONTENT_STATES_TYPE_START,
                            mMainAdList.get(mMainCurrentADIndex).getContent_type(),
                            AppConstants.CONTENT_SCREEN_POSITION_MAIN, mTimestampMain);

                    if(mMainAdList !=null && mMainAdList.size() >0)
                    {
                        sendDeviceStates(mMainAdList.get(mMainCurrentADIndex).getContent_id(),AppConstants.DEVICE_STATES_TYPE_RUN);
                    }
                }
                else
                {
                    if(mMainPlayWhenReady)
                    {
                        releaseSubPlayer();
                        binding.mainPlayerView.setVisibility(View.VISIBLE);
                        mMainPlayer.setPlayWhenReady(true);
                        mMainVideoADHandler.sendEmptyMessageDelayed(1, Integer.parseInt(mMainAdList.get(mMainCurrentADIndex).getContent_duration()) - mADNextCheckTime);
                        //컨텐츠 재생상태 처리 시작
                        mTimestampMain =  Utils.getNowTimestamp();
                        sendContentStates(
                                mMainAdList.get(mMainCurrentADIndex).getContent_id(),
                                AppConstants.CONTENT_STATES_TYPE_START,
                                mMainAdList.get(mMainCurrentADIndex).getContent_type(),
                                AppConstants.CONTENT_SCREEN_POSITION_MAIN, mTimestampMain);

                        if(mMainAdList !=null && mMainAdList.size() >0)
                        {
                            sendDeviceStates(mMainAdList.get(mMainCurrentADIndex).getContent_id(),AppConstants.DEVICE_STATES_TYPE_RUN);
                        }
                    }
                }

            }
            else
            {
                if(mIsLowLevel)
                {
                    releaseSubPlayer();
                    binding.mainPlayerView.setVisibility(View.VISIBLE);
                    setMainPlayer(mMainAdList.get(mMainCurrentADIndex).getContent_url().get(0));
                    mMainPlayer.setPlayWhenReady(true);
                    //컨텐츠 재생상태 처리 시작
                    mTimestampMain =  Utils.getNowTimestamp();
                    sendContentStates(
                            mMainAdList.get(mMainCurrentADIndex).getContent_id(),
                            AppConstants.CONTENT_STATES_TYPE_START,
                            mMainAdList.get(mMainCurrentADIndex).getContent_type(),
                            AppConstants.CONTENT_SCREEN_POSITION_MAIN, mTimestampMain);

                    if(mMainAdList !=null && mMainAdList.size() >0)
                    {
                        sendDeviceStates(mMainAdList.get(mMainCurrentADIndex).getContent_id(),AppConstants.DEVICE_STATES_TYPE_RUN);
                    }
                }
                else
                {
                    if(mMainPlayWhenReady)
                    {
                        binding.mainPlayerView.setVisibility(View.VISIBLE);
                        mMainPlayer.setPlayWhenReady(true);
                        mMainVideoADHandler.sendEmptyMessageDelayed(1, Integer.parseInt(mMainAdList.get(mMainCurrentADIndex).getContent_duration()) - mADNextCheckTime);
                        //컨텐츠 재생상태 처리 시작
                        mTimestampMain = Utils.getNowTimestamp();
                        sendContentStates(
                                mMainAdList.get(mMainCurrentADIndex).getContent_id(),
                                AppConstants.CONTENT_STATES_TYPE_START,
                                mMainAdList.get(mMainCurrentADIndex).getContent_type(),
                                AppConstants.CONTENT_SCREEN_POSITION_MAIN, mTimestampMain);

                        if(mMainAdList !=null && mMainAdList.size() >0)
                        {
                            sendDeviceStates(mMainAdList.get(mMainCurrentADIndex).getContent_id(),AppConstants.DEVICE_STATES_TYPE_RUN);
                        }
                    }
                }


            }
        }
    }


    private void initMainImageAD()
    {
        if(binding !=null)
        {
            setLoadingBar(false);
            mMainImageADPosition = 0;
            binding.imageAdZone.setVisibility(View.VISIBLE);
            binding.mainImage.setVisibility(View.VISIBLE);
            try
            {
                mMainImageADTotalDurationTime = Integer.parseInt(mMainAdList.get(mMainCurrentADIndex).getContent_duration());
                mMainImageADDurationTime = mMainImageADTotalDurationTime / mMainAdList.get(mMainCurrentADIndex).getContent_url().size();
                mMainImageADCount = mMainAdList.get(mMainCurrentADIndex).getContent_url().size();

                Glide.with(getApplicationContext())
                    .load(mMainAdList.get(mMainCurrentADIndex).getContent_url().get(mMainImageADPosition))
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            //컨텐츠 재생상태 처리 에러
                            sendContentStates(
                                    mMainAdList.get(mMainCurrentADIndex).getContent_id(),
                                    AppConstants.CONTENT_STATES_TYPE_ERROR,
                                    mMainAdList.get(mMainCurrentADIndex).getContent_type(),
                                    AppConstants.CONTENT_SCREEN_POSITION_MAIN, mTimestampMain);
                            mMainImageADPosition++;
                            if(mMainImageADCount > 1)
                            {
                                mMainImageADHandler.sendEmptyMessageDelayed(1, 1);
                            }
                            else
                            {
                                onMainNextADCheck();
                                mMainImageADHandler.sendEmptyMessageDelayed(3, 500);
                            }
                            return true;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                            mMainImageADPosition++;
                            if(mMainImageADCount > 1)
                            {
                                mMainImageADHandler.sendEmptyMessageDelayed(1, mMainImageADDurationTime);
                            }
                            else
                            {
                                mMainImageADHandler.sendEmptyMessageDelayed(2, mMainImageADTotalDurationTime - mADNextCheckTime);
                            }
                            return false;
                        }
                    })
                    .transition(DrawableTransitionOptions.withCrossFade(800))
                    .into(binding.mainImage);
            }catch (Exception e)
            {

                sendContentStates(
                        mMainAdList.get(mMainCurrentADIndex).getContent_id(),
                        AppConstants.CONTENT_STATES_TYPE_ERROR,
                        mMainAdList.get(mMainCurrentADIndex).getContent_type(),
                        AppConstants.CONTENT_SCREEN_POSITION_MAIN, mTimestampMain);
                mMainImageADPosition++;
                if(mMainImageADCount > 1)
                {
                    mMainImageADHandler.sendEmptyMessageDelayed(1, 1);
                }
                else
                {
                    onMainNextADCheck();
                    mMainImageADHandler.sendEmptyMessageDelayed(3, 500);
                }
            }




        }
    }


    private void onBottomNextADPlay()
    {

        if(mIsBottomAdChangeFlag)
        {
            mIsBottomAdChangeFlag = false;
            clearBottomADProcess();
            getBottomAD();
            return;
        }

        if(mIsBottomAdFinishFlag)
        {
            mIsBottomAdFinishFlag= false;
            clearBottomADProcess();
            binding.bottomZone.setVisibility(View.GONE);
            return;
        }

        if(mBottomAdList.size() > mBottomCurrentADIndex+1)
        {
            mBottomCurrentADIndex ++;
        }
        else
        {
            mBottomCurrentADIndex = 0;
        }
        initBottomImageAD();
    }

    private void initBottomImageAD()
    {
        if(binding !=null)
        {
            mBottomImageADPosition = 0;
            binding.bottomZone.setVisibility(View.VISIBLE);
            binding.bottomImage.setVisibility(View.VISIBLE);

            try
            {

                mBottomImageADTotalDurationTime = Integer.parseInt(mBottomAdList.get(mBottomCurrentADIndex).getContent_duration());
                mBottomImageADDurationTime = mBottomImageADTotalDurationTime / mBottomAdList.get(mBottomCurrentADIndex).getContent_url().size();
                mBottomImageADCount = mBottomAdList.get(mBottomCurrentADIndex).getContent_url().size();

                //컨텐츠 재생상태 처리 시작
                mTimestampBottom =  Utils.getNowTimestamp();

                sendContentStates(
                        mBottomAdList.get(mBottomCurrentADIndex).getContent_id(),
                        AppConstants.CONTENT_STATES_TYPE_START,
                        mBottomAdList.get(mBottomCurrentADIndex).getContent_type(),
                        AppConstants.CONTENT_SCREEN_POSITION_BOTTOM, mTimestampBottom);

                Glide.with(getApplicationContext())
                        .load(mBottomAdList.get(mBottomCurrentADIndex).getContent_url().get(mBottomImageADPosition))
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                //컨텐츠 재생상태 처리 에러
                                sendContentStates(
                                        mBottomAdList.get(mBottomCurrentADIndex).getContent_id(),
                                        AppConstants.CONTENT_STATES_TYPE_ERROR,
                                        mBottomAdList.get(mBottomCurrentADIndex).getContent_type(),
                                        AppConstants.CONTENT_SCREEN_POSITION_BOTTOM, mTimestampBottom);

                                mBottomImageADPosition++;
                                if(mBottomImageADCount > 1)
                                {
                                    mBottomImageADHandler.sendEmptyMessageDelayed(1, 1);
                                }
                                else
                                {
                                    mBottomImageADHandler.sendEmptyMessageDelayed(2, 1);
                                }
                                return true;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                                mBottomImageADPosition++;
                                if(mBottomImageADCount > 1)
                                {
                                    mBottomImageADHandler.sendEmptyMessageDelayed(1, mBottomImageADDurationTime);
                                }
                                else
                                {
                                    mBottomImageADHandler.sendEmptyMessageDelayed(2, mBottomImageADTotalDurationTime);
                                }
                                return false;
                            }
                        })
                        .transition(DrawableTransitionOptions.withCrossFade(800))
                        .into(binding.bottomImage);
            }catch (Exception e)
            {
                //컨텐츠 재생상태 처리 에러
                sendContentStates(
                        mBottomAdList.get(mBottomCurrentADIndex).getContent_id(),
                        AppConstants.CONTENT_STATES_TYPE_ERROR,
                        mBottomAdList.get(mBottomCurrentADIndex).getContent_type(),
                        AppConstants.CONTENT_SCREEN_POSITION_BOTTOM, mTimestampBottom);

                mBottomImageADPosition++;
                if(mBottomImageADCount > 1)
                {
                    mBottomImageADHandler.sendEmptyMessageDelayed(1, 1);
                }
                else
                {
                    mBottomImageADHandler.sendEmptyMessageDelayed(2, 1);
                }
            }

        }
    }

    private void onSideNextADPlay()
    {

        if(mIsSideAdChangeFlag)
        {
            mIsSideAdChangeFlag = false;
            clearSideADProcess();
            getSideAD();
            return;
        }

        if(mIsSideAdFinishFlag)
        {
            mIsSideAdFinishFlag = false;
            clearSideADProcess();
            binding.rightZone.setVisibility(View.GONE);
            return;
        }

        if(mSideAdList.size() > mSideCurrentADIndex+1)
        {
            mSideCurrentADIndex ++;
        }
        else
        {
            mSideCurrentADIndex = 0;
        }
        initSideAD();
    }

    private void initSideAD()
    {
        if(binding !=null)
        {
            binding.rightZone.setVisibility(View.VISIBLE);
            if(mSideAdList.get(mSideCurrentADIndex).getContent_type().equals("image"))
            {
                mSideImageADPosition = 0;
                binding.weatherAdZone.setVisibility(View.GONE);
                binding.rightImage.setVisibility(View.VISIBLE);

                mSideImageADTotalDurationTime = Integer.parseInt(mSideAdList.get(mSideCurrentADIndex).getContent_duration());
                mSideImageADDurationTime = mSideImageADTotalDurationTime / mSideAdList.get(mSideCurrentADIndex).getContent_url().size();
                mSideImageADCount = mSideAdList.get(mSideCurrentADIndex).getContent_url().size();

                mTimestampSide =  Utils.getNowTimestamp();
                sendContentStates(
                        mSideAdList.get(mSideCurrentADIndex).getContent_id(),
                        AppConstants.CONTENT_STATES_TYPE_START,
                        mSideAdList.get(mSideCurrentADIndex).getContent_type(),
                        AppConstants.CONTENT_SCREEN_POSITION_SIDE, mTimestampSide);
                try
                {
                    Glide.with(getApplicationContext())
                        .load(mSideAdList.get(mSideCurrentADIndex).getContent_url().get(mSideImageADPosition))
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                //컨텐츠 재생상태 처리 에러
                                sendContentStates(
                                        mSideAdList.get(mSideCurrentADIndex).getContent_id(),
                                        AppConstants.CONTENT_STATES_TYPE_ERROR,
                                        mSideAdList.get(mSideCurrentADIndex).getContent_type(),
                                        AppConstants.CONTENT_SCREEN_POSITION_SIDE, mTimestampSide);

                                //1장 이미지는 바로 노출 처리
                                mSideImageADPosition++;
                                if(mSideImageADCount > 1)
                                {
                                    mSideImageADHandler.sendEmptyMessageDelayed(1, 1);
                                }
                                else
                                {
                                    mSideImageADHandler.sendEmptyMessageDelayed(2, 1);
                                }
                                return true;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                                mSideImageADPosition++;
                                if(mSideImageADCount > 1)
                                {
                                    mSideImageADHandler.sendEmptyMessageDelayed(1, mSideImageADDurationTime);
                                }
                                else
                                {
                                    mSideImageADHandler.sendEmptyMessageDelayed(2, mSideImageADTotalDurationTime);
                                }
                                return false;
                            }
                        })
                        .transition(DrawableTransitionOptions.withCrossFade(800))
                        .into(binding.rightImage);
                }catch (Exception e)
                {
                    //컨텐츠 재생상태 처리 에러
                    sendContentStates(
                            mSideAdList.get(mSideCurrentADIndex).getContent_id(),
                            AppConstants.CONTENT_STATES_TYPE_ERROR,
                            mSideAdList.get(mSideCurrentADIndex).getContent_type(),
                            AppConstants.CONTENT_SCREEN_POSITION_SIDE, mTimestampSide);

                    //1장 이미지는 바로 노출 처리
                    mSideImageADPosition++;
                    if(mSideImageADCount > 1)
                    {
                        mSideImageADHandler.sendEmptyMessageDelayed(1, 1);
                    }
                    else
                    {
                        mSideImageADHandler.sendEmptyMessageDelayed(2, 1);
                    }
                }
            }
            else
            {
                mTimestampSide =  Utils.getNowTimestamp();
                sendContentStates(
                        mSideAdList.get(mSideCurrentADIndex).getContent_id(),
                        AppConstants.CONTENT_STATES_TYPE_START,
                        mSideAdList.get(mSideCurrentADIndex).getContent_type(),
                        AppConstants.CONTENT_SCREEN_POSITION_SIDE, mTimestampSide);
                binding.rightImage.setVisibility(View.GONE);
                getWeatherData();
            }
        }
    }

    private void getWeatherData()
    {
        OddiAdApi.getInstance().getLocationWeather().enqueue(new Callback<WeatherModel>() {
            @Override
            public void onResponse(Call<WeatherModel> call, Response<WeatherModel> response) {
                try {
                    if(response !=null && response.body().getData() !=null)
                    {
                        initWeather(response.body());
                    }
                }catch (Exception e)
                {
                    mSideImageADHandler.sendEmptyMessageDelayed(2, 1);
                }

            }

            @Override
            public void onFailure(Call<WeatherModel> call, Throwable t) {

            }
        });
    }

    private void initWeather(WeatherModel item)
    {
        if(binding !=null)
        {
            try
            {
                binding.weatherLocationText.setText(item.getData().addr);
                binding.weatherIcon.setBackgroundResource(getResources().getIdentifier("ico_w_"+item.getData().icon.substring(item.getData().icon.indexOf("w")+1),"drawable",getPackageName()));
                binding.weatherTextTempNow.setText(item.getData().tmp);
                binding.weatherTextView.setText(item.getData().sky);
                binding.weatherTempMaxLevel.setText(String.format(getString(R.string.weather_temp_max_level_text), item.getData().tmx));
                binding.weatherTempLowLevel.setText(String.format(getString(R.string.weather_temp_low_level_text), item.getData().tmn));
//            binding.weatherPrecipitationText.setText(String.format(getString(R.string.weather_precipitation_text), item.getData().pop));
//            binding.weatherHumidityText.setText(String.format(getString(R.string.weather_humidity_text), item.getData().reh));
//            binding.weatherWindSpeedText.setText(String.format(getString(R.string.weather_wind_speed_text), item.getData().wsd));

                try
                {

                    int pm10Level = Integer.parseInt(item.getData().pm10);

                    if(pm10Level < 31)
                    {
                        binding.weatherFineDustStateText.setText("좋음");
                        binding.weatherFineDustStateText.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.color_459dff));
                    }
                    else if(pm10Level > 30 && pm10Level < 81 )
                    {
                        binding.weatherFineDustStateText.setText("보통");
                        binding.weatherFineDustStateText.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.color_11c44e));
                    }
                    else if(pm10Level > 80 && pm10Level < 151 )
                    {
                        binding.weatherFineDustStateText.setText("나쁨");
                        binding.weatherFineDustStateText.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.color_ff8208));
                    }
                    else
                    {
                        binding.weatherFineDustStateText.setText("매우 나쁨");
                        binding.weatherFineDustStateText.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.color_ff0808));
                    }
                    binding.weatherFineDustText.setText(String.format(getString(R.string.weather_fine_dust_text), item.getData().pm10));
                }catch (Exception e)
                {
                    binding.weatherDustLayout.setVisibility(View.GONE);
                }


                try
                {
                    int pm25Level = Integer.parseInt(item.getData().pm25);
                    if(pm25Level < 16)
                    {
                        binding.weatherUltraFineDustStateText.setText("좋음");
                        binding.weatherUltraFineDustStateText.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.color_459dff));
                    }
                    else if(pm25Level > 15 && pm25Level < 36 )
                    {
                        binding.weatherUltraFineDustStateText.setText("보통");
                        binding.weatherUltraFineDustStateText.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.color_11c44e));
                    }
                    else if(pm25Level > 35 && pm25Level < 76 )
                    {
                        binding.weatherUltraFineDustStateText.setText("나쁨");
                        binding.weatherUltraFineDustStateText.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.color_ff8208));
                    }
                    else
                    {
                        binding.weatherUltraFineDustStateText.setText("매우 나쁨");
                        binding.weatherUltraFineDustStateText.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.color_ff0808));
                    }

                    binding.weatherUltraFineDustText.setText(String.format(getString(R.string.weather_fine_dust_text), item.getData().pm25));
                }catch (Exception e)
                {
                    binding.weatherUltraDustLayout.setVisibility(View.GONE);
                }


                binding.weatherAdZone.setVisibility(View.VISIBLE);
                mSideImageADHandler.sendEmptyMessageDelayed(2, Integer.parseInt(mSideAdList.get(mSideCurrentADIndex).getContent_duration()));
            }catch (Exception e)
            {
                mSideImageADHandler.sendEmptyMessageDelayed(2, 1);
            }

        }
    }
    private void startDownLoadFile()
    {
        try {
            if(contentList !=null && contentList.size() >0)
            {
                for (ContentsDetailInfo item :contentList)
                {
                    if(!item.isDownload_yn())
                    {
                        sendDownRequest(item.getDownload_url());
                        break;
                    }

                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void sendDownRequest(String url)
    {
        FileDownloadManager.getInstance().startDownload(MainActivity.this, url, new FileDownloadManager.OnCallBackListener() {
            @Override
            public void onFileDownLoadStart(long downloadId, String fileName) {
                for (ContentsDetailInfo item :contentList)
                {
                    if(item.getFile_name().equals(fileName))
                    {
                        item.setDownload_id(downloadId);
                    }
                }
            }

            @Override
            public void onFileDownLoadComplete(long downloadId, String filePath) {
                for (ContentsDetailInfo item :contentList)
                {
                    if(item.getDownload_id() == downloadId)
                    {
                        item.setDownload_yn(true);
                        item.setPlay_url(filePath);
                    }
                }

                for (ContentsDetailInfo item :contentList)
                {
                    if(!item.isDownload_yn())
                    {
                        sendDownRequest(item.getDownload_url());
                        break;
                    }
                }


            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initEventReceiver();
        FileDownloadManager.getInstance().setRegisterReceiver(MainActivity.this);
        if(mIsPauseFlag)
        {
            //광고APP 재 시작을 알림
            if(mMainAdList !=null && mMainAdList.size() >0)
            {
                sendDeviceStates(mMainAdList.get(mMainCurrentADIndex).getContent_id(),AppConstants.DEVICE_STATES_TYPE_RESUME);
            }
            mIsPauseFlag = false;
            resumeADProcess();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        FileDownloadManager.getInstance().setUnRegisterReceiver(MainActivity.this);
        mIsPauseFlag = true;
        //광고APP pause 알림
        if(!isFinishing())
        {
            //종료가 아닌 상태의 pause
            if(mMainAdList !=null && mMainAdList.size() >0)
            {
                sendDeviceStates(mMainAdList.get(mMainCurrentADIndex).getContent_id(),AppConstants.DEVICE_STATES_TYPE_PAUSE);
            }
        }

    }



    @Override
    protected void onStop() {
        super.onStop();
        //광고APP stop 알림
        if(isFinishing())
        {
            destroyEventReceiver();
            sendDeviceStates("",mIsUserFinishFlag ? AppConstants.DEVICE_STATES_TYPE_DESTROY_USER : AppConstants.DEVICE_STATES_TYPE_DESTROY_EXCEPTION);
        }
        else
        {
            //종료가 아닌 상태의 onStop
            if(mMainAdList !=null && mMainAdList.size() >0)
            {
                sendDeviceStates(mMainAdList.get(mMainCurrentADIndex).getContent_id(),AppConstants.DEVICE_STATES_TYPE_STOP);
            }
            stopADProcess();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRetryHandler.removeMessages(1);
        this.connectivityManager.unregisterNetworkCallback( mNetworkCallback);
        destroyEventReceiver();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public boolean onBackPressedFinish() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000)
        {
            Toast.makeText(MainActivity.this, getString(R.string.finish_use_kill_app), Toast.LENGTH_SHORT).show();
            backKeyPressedTime = System.currentTimeMillis();
            return true;
        } else {
            mIsUserFinishFlag = true;
            setFinishAD();
        }
        return false;
    }

    @Override
    public void onBackPressed() {

        if (onBackPressedFinish()) {
            return;
        }
        super.onBackPressed();
    }


    private void initEventReceiver() {

        if(mEventReceiver ==null)
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




    private static final String EXTENSION_EXTRA = "extension";


    private void setMainPlayer(String path)
    {
        if(mIsContentsDownLoadPlay)
        {
            for (ContentsDetailInfo item :contentList)
            {
                if(item.getDownload_url().equals(path))
                {
                    if(item.isDownload_yn())
                    {
                        path = item.getPlay_url();
                    }
                }
            }
        }
        Intent intent = getIntent();
        String action = intent.getAction();
        Uri uri = ACTION_VIEW.equals(action)
                ? Assertions.checkNotNull(intent.getData())
                : Uri.parse(path);
        DrmSessionManager drmSessionManager;
        drmSessionManager = DrmSessionManager.DRM_UNSUPPORTED;

        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this);
        MediaSource mediaSource;
        @C.ContentType int type = Util.inferContentType(uri, intent.getStringExtra(EXTENSION_EXTRA));
        if (type == C.TYPE_DASH) {
            mediaSource =
                    new DashMediaSource.Factory(dataSourceFactory)
                            .setDrmSessionManager(drmSessionManager)
                            .createMediaSource(MediaItem.fromUri(uri));
        }
        else if (type == C.TYPE_OTHER) {
            mediaSource =
                    new ProgressiveMediaSource.Factory(dataSourceFactory)
                            .setDrmSessionManager(drmSessionManager)
                            .createMediaSource(MediaItem.fromUri(uri));
        }
        else if (type == C.TYPE_HLS) {
            mediaSource =
                    new HlsMediaSource.Factory(dataSourceFactory)
                            .setDrmSessionManager(drmSessionManager)
                            .createMediaSource(MediaItem.fromUri(uri));
        }
        else {
            throw new IllegalStateException();
        }

        if(mMainPlayer == null)
        {

            mMainPlayer = new SimpleExoPlayer.Builder(getApplicationContext()).build();
            mMainPlayer.prepare();
            mMainPlayer.setRepeatMode(Player.REPEAT_MODE_OFF);
            mMainPlayer.addListener(new Player.Listener() {

                @Override
                public void onPlaybackStateChanged(int playbackState) {

                    if (playbackState == 1) {
                    } else if (playbackState == 2 || playbackState == 3) {
                        mMainPlayWhenReady = true;
                        if(playbackState == 3 && mMainCurrentAD.equals("NONE"))
                        {
                            binding.mainPlayerView.setVisibility(View.VISIBLE);
                            mMainCurrentAD = mMainAdList.get(mMainCurrentADIndex).getContent_type();
                            binding.mainPlayerView.requestFocus();
                            setLoadingBar(false);
                            mMainPlayer.setPlayWhenReady(true);
                            mMainVideoADHandler.sendEmptyMessageDelayed(1, Integer.parseInt(mMainAdList.get(mMainCurrentADIndex).getContent_duration()) - mADNextCheckTime);

                            //컨텐츠 재생상태 처리 시작
                            mTimestampMain = Utils.getNowTimestamp();
                            sendContentStates(
                                    mMainAdList.get(mMainCurrentADIndex).getContent_id(),
                                    AppConstants.CONTENT_STATES_TYPE_START,
                                    mMainAdList.get(mMainCurrentADIndex).getContent_type(),
                                    AppConstants.CONTENT_SCREEN_POSITION_MAIN, mTimestampMain);

                            if(mMainAdList !=null && mMainAdList.size() >0)
                            {

                                sendDeviceStates(mMainAdList.get(mMainCurrentADIndex).getContent_id(),AppConstants.DEVICE_STATES_TYPE_RUN);
                            }
                        }
                    } else if (playbackState == 4) {

                    }

                }

                @Override
                public void onIsLoadingChanged(boolean isLoading) {
                }

                @Override
                public void onIsPlayingChanged(boolean isPlaying) {
                    if(isPlaying)
                    {
                        mMainVideoADHandler.sendEmptyMessageDelayed(2, Integer.parseInt(mMainAdList.get(mMainCurrentADIndex).getContent_duration()));
                    }

                }

                @Override
                public void onPlayerError(PlaybackException error) {

                    mMainVideoADHandler.removeMessages(1);
                    mMainVideoADHandler.removeMessages(2);
                    mMainVideoADHandler.removeMessages(3);

                    //컨텐츠 재생상태 처리 에러
                    sendContentStates(
                            mMainAdList.get(mMainCurrentADIndex).getContent_id(),
                            AppConstants.CONTENT_STATES_TYPE_ERROR,
                            mMainAdList.get(mMainCurrentADIndex).getContent_type(),
                            AppConstants.CONTENT_SCREEN_POSITION_MAIN, mTimestampMain);

                    releasePlayer();

                    if(getMainAdNextType().equals("video"))
                    {
                        mMainVideoADHandler.sendEmptyMessageDelayed(1, 1);
                        mMainVideoADHandler.sendEmptyMessageDelayed(2, mADNextCheckTime);
                    }
                    else
                    {
                        mMainVideoADHandler.sendEmptyMessageDelayed(2, 1);
                    }

                }

            });
        }

        mMainPlayer.setMediaSource(mediaSource);

        binding.mainPlayerView.setPlayer(mMainPlayer);
        binding.mainPlayerView.setUseController(false);
        binding.mainPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
    }


    private void setSubPlayer(String path)
    {
        if(mIsContentsDownLoadPlay)
        {
            for (ContentsDetailInfo item :contentList)
            {
                if(item.getDownload_url().equals(path))
                {
                    if(item.isDownload_yn())
                    {
                        path = item.getPlay_url();
                    }
                }
            }
        }

        Intent intent = getIntent();
        String action = intent.getAction();
        Uri uri = ACTION_VIEW.equals(action)
                ? Assertions.checkNotNull(intent.getData())
                : Uri.parse(path);
        DrmSessionManager drmSessionManager;
        drmSessionManager = DrmSessionManager.DRM_UNSUPPORTED;

        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this);
        MediaSource mediaSource;
        @C.ContentType int type = Util.inferContentType(uri, intent.getStringExtra(EXTENSION_EXTRA));

        if (type == C.TYPE_DASH) {
            mediaSource =
                    new DashMediaSource.Factory(dataSourceFactory)
                            .setDrmSessionManager(drmSessionManager)
                            .createMediaSource(MediaItem.fromUri(uri));
        }
        else if (type == C.TYPE_OTHER) {
            mediaSource =
                    new ProgressiveMediaSource.Factory(dataSourceFactory)
                            .setDrmSessionManager(drmSessionManager)
                            .createMediaSource(MediaItem.fromUri(uri));
        }
        else if (type == C.TYPE_HLS) {
            mediaSource =
                    new HlsMediaSource.Factory(dataSourceFactory)
                            .setDrmSessionManager(drmSessionManager)
                            .createMediaSource(MediaItem.fromUri(uri));
        }
        else {
            throw new IllegalStateException();
        }

        if(mSubPlayer ==null)
        {

            mSubPlayer = new SimpleExoPlayer.Builder(getApplicationContext()).build();
            mSubPlayer.prepare();
            mSubPlayer.setRepeatMode(Player.REPEAT_MODE_OFF);
            mSubPlayer.addListener(new Player.Listener() {


                @Override
                public void onPlaybackStateChanged(int playbackState) {
                    if (playbackState == 1) {
                    }else if (playbackState == 2 || playbackState == 3) {
                        mSubPlayWhenReady = true;

                    } else if (playbackState == 4) {

                    }

                }

                @Override
                public void onIsLoadingChanged(boolean isLoading) {
                }

                @Override
                public void onIsPlayingChanged(boolean isPlaying) {
                    if(isPlaying)
                    {
                        mMainVideoADHandler.sendEmptyMessageDelayed(3, Integer.parseInt(mMainAdList.get(mMainCurrentADIndex).getContent_duration()));
                    }
                }

                @Override
                public void onPlayerError(PlaybackException error) {
                    setLoadingBar(true);
                    //컨텐츠 재생상태 처리 에러
                    mMainVideoADHandler.removeMessages(1);
                    mMainVideoADHandler.removeMessages(2);
                    mMainVideoADHandler.removeMessages(3);
                    error.printStackTrace();
                    sendContentStates(
                            mMainAdList.get(mMainCurrentADIndex).getContent_id(),
                            AppConstants.CONTENT_STATES_TYPE_ERROR,
                            mMainAdList.get(mMainCurrentADIndex).getContent_type(),
                            AppConstants.CONTENT_SCREEN_POSITION_MAIN, mTimestampMain);

                    releasePlayer();
                    if(getMainAdNextType().equals("video"))
                    {
                        mMainVideoADHandler.sendEmptyMessageDelayed(1, 1);
                        mMainVideoADHandler.sendEmptyMessageDelayed(3, mADNextCheckTime);
                    }
                    else
                    {
                        mMainVideoADHandler.sendEmptyMessageDelayed(3, 1);
                    }
                }
            });

        }

        mSubPlayer.setMediaSource(mediaSource);

        binding.subPlayerView.setPlayer(mSubPlayer);
        binding.subPlayerView.setUseController(false);
        binding.subPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);

    }

    private void releasePlayer() {
        try {
            if (mMainPlayer != null) {
                mMainPlayer.setPlayWhenReady(false);
                binding.mainPlayerView.setPlayer(null);
                mMainPlayer.release();
                mMainPlayer = null;
            }

            if (mSubPlayer != null) {

                mSubPlayer.setPlayWhenReady(false);
                binding.subPlayerView.setPlayer(null);
                mSubPlayer.release();
                mSubPlayer = null;
            }
        }catch (Throwable e)
        {
        }

    }

    private void releaseMainPlayer() {
        try {

            if (mMainPlayer != null) {
                mMainPlayer.setPlayWhenReady(false);
                binding.mainPlayerView.setPlayer(null);

                if(mIsLowLevel)
                {

                    mMainPlayer.release();
                    mMainPlayer = null;
                }
            }

            System.gc();

        }catch (Exception e)
        {

        }
    }

    private void releaseSubPlayer() {

        try {

            if (mSubPlayer != null) {

                mSubPlayer.setPlayWhenReady(false);
                binding.subPlayerView.setPlayer(null);

                if(mIsLowLevel)
                {

                    mSubPlayer.release();
                    mSubPlayer = null;
                }
            }

            System.gc();

        }catch (Exception e)
        {

        }
    }


    private AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {

            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_GAIN :
                    break;
                case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT :

                    break;
                case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK :
                    break;
                case AudioManager.AUDIOFOCUS_LOSS :

                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT :
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK :
                    break;
                default:
                    break;
            }
        }
    };



    Handler mMainImageADHandler = new Handler(Looper.getMainLooper())
    {
        @Override
        public void handleMessage(Message msg)
        {
            try
            {
                mMainImageADHandler.removeMessages(1);
                mMainImageADHandler.removeMessages(2);
                mMainImageADHandler.removeMessages(3);
                if(msg.what == 1)
                {

                    if(mMainImageADCount > mMainImageADPosition)
                    {

                        try {
                                Glide.with(getApplicationContext())
                                .load(mMainAdList.get(mMainCurrentADIndex).getContent_url().get(mMainImageADPosition))
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                                        mMainImageADPosition++;
                                        mMainImageADHandler.sendEmptyMessageDelayed(1, 1);
                                        //컨텐츠 재생상태 처리 에러
                                        sendContentStates(
                                                mMainAdList.get(mMainCurrentADIndex).getContent_id(),
                                                AppConstants.CONTENT_STATES_TYPE_ERROR,
                                                mMainAdList.get(mMainCurrentADIndex).getContent_type(),
                                                AppConstants.CONTENT_SCREEN_POSITION_MAIN, mTimestampMain);

                                        return true;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {

                                        mMainImageADHandler.sendEmptyMessageDelayed(1, mMainImageADDurationTime);

                                        if(mMainImageADCount - mMainImageADPosition == 1)
                                        {

                                            //이미지 다음장 보여주기 전에
                                            onMainNextADCheck();
                                        }
                                        mMainImageADPosition++;
                                        return false;
                                    }
                                })
                                .transition(DrawableTransitionOptions.withCrossFade(800))
                                .into(binding.mainImage);
                        }catch (Exception e)
                        {
                            e.printStackTrace();

                            mMainImageADPosition++;
                            mMainImageADHandler.sendEmptyMessageDelayed(1, 1);
                            //컨텐츠 재생상태 처리 에러
                            sendContentStates(
                                    mMainAdList.get(mMainCurrentADIndex).getContent_id(),
                                    AppConstants.CONTENT_STATES_TYPE_ERROR,
                                    mMainAdList.get(mMainCurrentADIndex).getContent_type(),
                                    AppConstants.CONTENT_SCREEN_POSITION_MAIN, mTimestampMain);
                        }

                    }
                    else
                    {

                        try {
                            System.gc();
                        }catch (Exception e)
                        {

                        }
                        binding.imageAdZone.setVisibility(View.GONE);
                        binding.mainImage.setVisibility(View.GONE);
                        onMainNextADPlay();
                    }
                }
                else if(msg.what == 2)
                {
                    onMainNextADCheck();
                    mMainImageADHandler.sendEmptyMessageDelayed(3, mADNextCheckTime);
                }
                else if(msg.what == 3)
                {

                    onMainNextADPlay();

                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                mMainImageADHandler.removeMessages(1);
                mMainImageADHandler.removeMessages(2);
                mMainImageADHandler.removeMessages(3);
            }
        }
    };


    Handler mBottomImageADHandler = new Handler(Looper.getMainLooper())
    {
        @Override
        public void handleMessage(Message msg)
        {
            try
            {
                mBottomImageADHandler.removeMessages(1);
                mBottomImageADHandler.removeMessages(2);
                if(msg.what == 1)
                {
                    if(mBottomImageADCount > mBottomImageADPosition)
                    {

                        try {
                            Glide.with(getApplicationContext())
                                    .load(mBottomAdList.get(mBottomCurrentADIndex).getContent_url().get(mBottomImageADPosition))
                                    .listener(new RequestListener<Drawable>() {
                                        @Override
                                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                            //컨텐츠 재생상태 처리 에러
                                            sendContentStates(
                                                    mBottomAdList.get(mBottomCurrentADIndex).getContent_id(),
                                                    AppConstants.CONTENT_STATES_TYPE_ERROR,
                                                    mBottomAdList.get(mBottomCurrentADIndex).getContent_type(),
                                                    AppConstants.CONTENT_SCREEN_POSITION_BOTTOM, mTimestampBottom);

                                            mBottomImageADPosition++;
                                            mBottomImageADHandler.sendEmptyMessageDelayed(1, 1);
                                            return true;
                                        }

                                        @Override
                                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                                            mBottomImageADHandler.sendEmptyMessageDelayed(1, mBottomImageADDurationTime);
                                            mBottomImageADPosition++;
                                            return false;
                                        }
                                    })
                                    .transition(DrawableTransitionOptions.withCrossFade(800))
                                    .into(binding.bottomImage);
                        }catch (Exception e)
                        {
                            sendContentStates(
                                    mBottomAdList.get(mBottomCurrentADIndex).getContent_id(),
                                    AppConstants.CONTENT_STATES_TYPE_ERROR,
                                    mBottomAdList.get(mBottomCurrentADIndex).getContent_type(),
                                    AppConstants.CONTENT_SCREEN_POSITION_BOTTOM, mTimestampBottom);

                            mBottomImageADPosition++;
                            mBottomImageADHandler.sendEmptyMessageDelayed(1, 1);
                        }
                    }
                    else
                    {
                        try {
                            System.gc();
                        }catch (Exception e)
                        {

                        }
                        onBottomNextADPlay();
                    }
                }
                else if(msg.what == 2)
                {
                    onBottomNextADPlay();
                }

            }
            catch (Exception e)
            {
                e.printStackTrace();
                mBottomImageADHandler.removeMessages(1);
                mBottomImageADHandler.removeMessages(2);
            }
        }
    };


    Handler mSideImageADHandler = new Handler(Looper.getMainLooper())
    {
        @Override
        public void handleMessage(Message msg)
        {
            try
            {
                mSideImageADHandler.removeMessages(1);
                mSideImageADHandler.removeMessages(2);
                if(msg.what == 1)
                {
                    if(mSideImageADCount > mSideImageADPosition)
                    {

                        try {
                            Glide.with(getApplicationContext())
                                    .load(mSideAdList.get(mSideCurrentADIndex).getContent_url().get(mSideImageADPosition))
                                    .listener(new RequestListener<Drawable>() {
                                        @Override
                                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                            //컨텐츠 재생상태 처리 에러
                                            sendContentStates(
                                                    mSideAdList.get(mSideCurrentADIndex).getContent_id(),
                                                    AppConstants.CONTENT_STATES_TYPE_ERROR,
                                                    mSideAdList.get(mSideCurrentADIndex).getContent_type(),
                                                    AppConstants.CONTENT_SCREEN_POSITION_SIDE, mTimestampSide);

                                            mSideImageADPosition++;
                                            mSideImageADHandler.sendEmptyMessageDelayed(1, 1);

                                            return true;
                                        }

                                        @Override
                                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                                            mSideImageADHandler.sendEmptyMessageDelayed(1, mSideImageADDurationTime);
                                            mSideImageADPosition++;
                                            return false;
                                        }
                                    })
                                    .transition(DrawableTransitionOptions.withCrossFade(800))
                                    .into(binding.rightImage);
                        }catch (Exception e)
                        {
                            //컨텐츠 재생상태 처리 에러
                            sendContentStates(
                                    mSideAdList.get(mSideCurrentADIndex).getContent_id(),
                                    AppConstants.CONTENT_STATES_TYPE_ERROR,
                                    mSideAdList.get(mSideCurrentADIndex).getContent_type(),
                                    AppConstants.CONTENT_SCREEN_POSITION_SIDE, mTimestampSide);

                            mSideImageADPosition++;
                            mSideImageADHandler.sendEmptyMessageDelayed(1, 1);
                        }

                    }
                    else
                    {
                        try {
                            System.gc();
                        }catch (Exception e)
                        {

                        }
                        onSideNextADPlay();
                    }
                }
                else if(msg.what == 2)
                {
                    onSideNextADPlay();
                }

            }
            catch (Exception e)
            {
                e.printStackTrace();
                mSideImageADHandler.removeMessages(1);
                mSideImageADHandler.removeMessages(2);
            }
        }
    };


    Handler mMainVideoADHandler = new Handler(Looper.getMainLooper())
    {
        @Override
        public void handleMessage(Message msg)
        {
            try
            {
                if(msg.what == 1)
                {
                    mMainVideoADHandler.removeMessages(1);
                    onMainNextADCheck();
                }
                else if(msg.what == 2)
                {
                    mMainVideoADHandler.removeMessages(2);
                    releaseMainPlayer();
                    onMainNextADPlay();
                }
                else if(msg.what == 3)
                {
                    mMainVideoADHandler.removeMessages(3);
                    releaseSubPlayer();
                    onMainNextADPlay();

                }

            }
            catch (Exception e)
            {
                mMainVideoADHandler.removeMessages(1);
                mMainVideoADHandler.removeMessages(2);
                mMainVideoADHandler.removeMessages(3);
            }
        }
    };


    public void getUsedMemorySize() {
        long freeSize = 0L;
        long totalSize = 0L;
        long usedSize = -1L;
        try {
            Runtime info = Runtime.getRuntime();
            freeSize = info.freeMemory();
            totalSize = info.totalMemory();
            usedSize = totalSize - freeSize;

//            System.out.println("===================Memory IN=====================");
//            System.out.println("======Memory freeSize="+(freeSize/ 1048576L)+" MB");
//            System.out.println("======Memory totalSize="+(totalSize/ 1048576L)+" MB");
//            System.out.println("======Memory usedSize="+(usedSize/ 1048576L)+" MB");


            final long usedMemInMB=(info.totalMemory() - info.freeMemory()) / 1048576L;
            final long maxHeapSizeInMB=info.maxMemory() / 1048576L;
            final long availHeapSizeInMB = maxHeapSizeInMB - usedMemInMB;

//            System.out.println("======Memory usedMemInMB="+usedMemInMB+" MB");
//            System.out.println("======Memory maxHeapSizeInMB="+maxHeapSizeInMB+" MB");
//            System.out.println("======Memory availHeapSizeInMB="+availHeapSizeInMB+" MB");
//            System.out.println("===================Memory OUT=====================");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setFinishAD()
    {
        try{
            clearADProcess();
            finish();
        }catch (Exception e)
        {

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
                        if(jsonObject.get(AppConstants.PUSH_ID) !=null)
                        {
                            sendFCMResponse(jsonObject.get(AppConstants.PUSH_ID).toString());
                        }

                        if(jsonObject.get(AppConstants.PUSH_ACTION) !=null)
                        {
                            if(AppConstants.ACTION_DEFINITION_FINISH_APP.equals(jsonObject.get(AppConstants.PUSH_ACTION)))
                            {
                                //종료처리
                                Toast.makeText(MainActivity.this,"관리자권한으로 종료됩니다.", Toast.LENGTH_SHORT).show();
                                setFinishAD();
                            }
                            else if(AppConstants.ACTION_DEFINITION_RESTART_APP.equals(jsonObject.get(AppConstants.PUSH_ACTION)))
                            {
                                //종료후 재시작 처리
                                Toast.makeText(MainActivity.this,"관리자권한으로 종료후 3분뒤 재시작합니다.", Toast.LENGTH_SHORT).show();
                                new Alarm().scheduleSet(context,3);
                                try {
                                    System.gc();
                                }catch (Exception e)
                                {

                                }
                                setFinishAD();
                            }
                            else if(AppConstants.ACTION_DEFINITION_SCREEN_TYPE.equals(jsonObject.get(AppConstants.PUSH_ACTION)))
                            {
                                if(AppConstants.SCREEN_TYPE_DIVISION_1.equals(jsonObject.get(AppConstants.PUSH_SCREEN_TYPE)))
                                {
                                    if(binding.rightZone.getVisibility() == View.VISIBLE)
                                        mIsSideAdFinishFlag = true;

                                    if(binding.bottomZone.getVisibility() == View.VISIBLE)
                                        mIsBottomAdFinishFlag = true;
                                }
                                else if(AppConstants.SCREEN_TYPE_DIVISION_2.equals(jsonObject.get(AppConstants.PUSH_SCREEN_TYPE)))
                                {

                                    if(binding.rightZone.getVisibility() != View.VISIBLE)
                                    {
                                        clearSideADProcess();
                                        getSideAD();
                                    }

                                    if(binding.bottomZone.getVisibility() == View.VISIBLE)
                                        mIsBottomAdFinishFlag = true;

                                }
                                else if(AppConstants.SCREEN_TYPE_DIVISION_3.equals(jsonObject.get(AppConstants.PUSH_SCREEN_TYPE)))
                                {
                                    if(binding.rightZone.getVisibility() != View.VISIBLE)
                                    {
                                        clearSideADProcess();
                                        getSideAD();
                                    }
                                    if(binding.bottomZone.getVisibility() != View.VISIBLE)
                                    {
                                        clearBottomADProcess();
                                        getBottomAD();
                                    }

                                }
                            }
                            else if(AppConstants.ACTION_DEFINITION_REQUEST_MAIN_AD.equals(jsonObject.get(AppConstants.PUSH_ACTION)))
                            {
                                mIsMainAdChangeFlag = true;
                            }
                            else if(AppConstants.ACTION_DEFINITION_REQUEST_SIDE_AD.equals(jsonObject.get(AppConstants.PUSH_ACTION)))
                            {
                                mIsSideAdChangeFlag = true;
                            }
                            else if(AppConstants.ACTION_DEFINITION_REQUEST_BOTTOM_AD.equals(jsonObject.get(AppConstants.PUSH_ACTION)))
                            {
                                mIsBottomAdChangeFlag = true;
                            }
                            else
                            {
                                //2021.24시간 기준 광고 갱신 로직 주석 처리
                                //mADRequestDate ="";
                            }

                        }

                    }catch (Exception e)
                    {

                    }
                }
            }
        }
    }


    private void sendDeviceStates(String content_id, String device_state)
    {
        try
        {
            OddiAdApi.getInstance().onDeviceState(content_id, device_state).enqueue(new Callback<CommonResponse>() {
                @Override
                public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {

                }

                @Override
                public void onFailure(Call<CommonResponse> call, Throwable t) {

                }
            });
        }catch (Exception e)
        {

        }
    }

    private void sendContentStates(String content_id, String content_state, String content_type, String content_screen_position, String content_ts)
    {
        try {
            OddiAdApi.getInstance().onContentState(content_id, content_state,content_type,content_screen_position,mContentScreenType, content_ts).enqueue(new Callback<CommonResponse>() {
                @Override
                public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {

                }

                @Override
                public void onFailure(Call<CommonResponse> call, Throwable t) {

                }
            });
        }catch (Exception e)
        {

        }
    }

    private void clearADProcess()
    {
        //컨텐츠 재생상태 처리 완료
        try{
            stopADProcess();

            clearMainADProcess();
            clearSideADProcess();
            clearBottomADProcess();

            binding.loadingAdZone.setVisibility(View.VISIBLE);
            binding.adZone.setVisibility(View.GONE);
            binding.rightZone.setVisibility(View.GONE);
            binding.bottomZone.setVisibility(View.GONE);

            try {
                binding.mainImage.setImageResource(0);
            }catch (Exception e)
            {

            }
        }catch (Exception e)
        {

        }
    }


    private void stopADProcess()
    {
        //컨텐츠 재생상태 처리 완료
        try{

            releasePlayer();

            mMainVideoADHandler.removeMessages(1);
            mMainVideoADHandler.removeMessages(2);
            mMainVideoADHandler.removeMessages(3);
            mSideImageADHandler.removeMessages(1);
            mSideImageADHandler.removeMessages(2);
            mBottomImageADHandler.removeMessages(1);
            mBottomImageADHandler.removeMessages(2);
            mMainImageADHandler.removeMessages(1);
            mMainImageADHandler.removeMessages(2);
            mMainImageADHandler.removeMessages(3);

            mMainPlayWhenReady = false;
            mSubPlayWhenReady = false;

        }catch (Exception e)
        {

        }
    }


    private void resumeADProcess()
    {
        //언제 복귀 할지 모르니 그냥 새로 시작 합시다.
        try{

            clearMainADProcess();
            clearSideADProcess();
            clearBottomADProcess();

            setLoadingBar(true);
            binding.rightZone.setVisibility(View.GONE);
            binding.bottomZone.setVisibility(View.GONE);

            getMainAD();

        }catch (Exception e)
        {

        }
    }

    private void clearMainADProcess()
    {
        try {

            mMainAdList.clear();
            mMainCurrentAD  = "NONE";
            mMainCurrentADIndex = 0;

            mMainImageADTotalDurationTime = 15000;
            mMainImageADDurationTime = 0;
            mMainImageADPosition = 0;
            mMainImageADCount = 0;

        }catch (Exception e)
        {

        }
    }

    private void clearSideADProcess()
    {
        try {

            mSideAdList.clear();
            mSideCurrentADIndex = 0;

            mSideImageADTotalDurationTime = 15000;
            mSideImageADDurationTime = 0;
            mSideImageADPosition = 0;
            mSideImageADCount = 0;

        }catch (Exception e)
        {

        }
    }

    private void clearBottomADProcess()
    {
        try {

            mBottomAdList.clear();
            mBottomCurrentADIndex = 0;

            mBottomImageADTotalDurationTime = 15000;
            mBottomImageADDurationTime = 0;
            mBottomImageADPosition = 0;
            mBottomImageADCount = 0;

        }catch (Exception e)
        {

        }
    }

    private int mLoadingCount = 0;
    private void setLoadingBar(boolean isVisible)
    {
        if(isVisible)
        {

            binding.loadingAdZone.setVisibility(View.VISIBLE);
            mLoadingCount = 16;
            mLoadingBarHandler.removeMessages(1);
            mLoadingBarHandler.sendEmptyMessageDelayed(1,50);


        }
        else
        {
            mLoadingCount = 16;
            binding.loadingAdZone.setVisibility(View.GONE);
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




    Handler mRetryHandler = new Handler(Looper.getMainLooper())
    {
        @Override
        public void handleMessage(Message msg)
        {
            try
            {
                mRetryHandler.removeMessages(1);
                getMainAD();
            }
            catch (Exception e)
            {

            }
        }
    };

}