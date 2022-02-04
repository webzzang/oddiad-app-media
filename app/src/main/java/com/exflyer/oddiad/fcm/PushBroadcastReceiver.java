package com.exflyer.oddiad.fcm;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class PushBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        try {
            NotificationManager notifiyMgr = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            notifiyMgr.cancelAll();
        }catch (Exception e)
        {

        }

//        if (Utils.checkAppClosed(context)) {
//            HomeLauncher.startActivityNewTask(context);
//        }
//        else
//        {
//            Bundle extras = intent.getExtras();
//            String tradeid ="";
//            if(extras == null) {
//            } else {
//                tradeid = extras.getString("tradeId");
//            }
//            Intent sendInten = new Intent();
//            sendInten.setAction(Def.FCM_LOCAL_BROADCAST);
//            if(!tradeid.equals(""))
//                sendInten.putExtra(Def.FCM_DELIVERY_DATA, tradeid);
//            LocalBroadcastManager.getInstance(context).sendBroadcast(sendInten);
//        }
    }
}
