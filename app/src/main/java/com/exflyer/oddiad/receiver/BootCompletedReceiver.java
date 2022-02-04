package com.exflyer.oddiad.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.exflyer.oddiad.preference.UserPreference;
import com.exflyer.oddiad.ui.activity.splash.SplashActivity;

public class BootCompletedReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        try
        {
            if(UserPreference.getInstance().getBootCompleted().equals("N"))
            {
                UserPreference.getInstance().saveBootCompleted("Y");
                Intent alarmIntent = new Intent("android.intent.action.MAIN");
                alarmIntent.setClass(context, SplashActivity.class);
                alarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                alarmIntent.putExtra("BootCompleted","Y");
                context.startActivity(alarmIntent);
            }
        }catch (Exception e)
        {

        }

    }
}
