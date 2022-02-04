package com.exflyer.oddiad.util;

import android.app.Activity;

import com.exflyer.oddiad.alarm.Alarm;
import com.exflyer.oddiad.network.api.OddiAdApi;
import com.exflyer.oddiad.network.model.common.CommonResponse;
import com.exflyer.oddiad.preference.UserPreference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyExceptionHandler implements Thread.UncaughtExceptionHandler {
    private Activity activity;
    public MyExceptionHandler(Activity a) {
        activity = a;
    }
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {

        try {
            UserPreference.getInstance().setInstance(activity);

            try
            {
                OddiAdApi.getInstance().onDeviceException("1").enqueue(new Callback<CommonResponse>() {
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

            if(UserPreference.getInstance().getExceptionCount() ==0)
            {
                //최초 비정상종료 시간 체크처리
                UserPreference.getInstance().saveExceptionTime(Utils.nowTimeYYYYMMDDHHMMSS());
                UserPreference.getInstance().saveExceptionCount(1);
            }
            else
            {
                if(Utils.get5MinuteDifference(UserPreference.getInstance().getExceptionTime(), Utils.nowTimeYYYYMMDDHHMMSS()))
                {
                    int temp = UserPreference.getInstance().getExceptionCount() + 1;
                    UserPreference.getInstance().saveExceptionCount(temp);
                }
                else
                {
                    UserPreference.getInstance().saveExceptionCount(0);
                }

            }

            if(UserPreference.getInstance().getExceptionCount() < 3)
            {
                new Alarm().scheduleSet(activity,1);
            }

            android.os.Process.killProcess(android.os.Process.myPid());
            activity.finish();
            System.gc();
            System.exit(0);
        }catch (Exception e)
        {

        }


    }
}