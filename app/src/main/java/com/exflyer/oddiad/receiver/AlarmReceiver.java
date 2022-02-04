package com.exflyer.oddiad.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.exflyer.oddiad.ui.activity.splash.SplashActivity;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            //        // 작동할 액티비티를 설정한다

            Toast.makeText(context, "수신 !! AlarmReceiver : !!", Toast.LENGTH_SHORT).show();
            Intent alarmIntent = new Intent("android.intent.action.MAIN");
            alarmIntent.setClass(context, SplashActivity.class);
            alarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(alarmIntent);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
