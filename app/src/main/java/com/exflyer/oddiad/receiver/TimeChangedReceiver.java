package com.exflyer.oddiad.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class TimeChangedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
//            if(!MainActivity.isRunMainActivity)
//            {
//                System.out.println("=================TimeChangedReceiver   2=");
//                new Alarm().scheduleSet(context,4);
//            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
