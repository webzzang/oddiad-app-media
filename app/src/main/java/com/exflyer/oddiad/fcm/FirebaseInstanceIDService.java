package com.exflyer.oddiad.fcm;


import android.content.Context;
import android.content.Intent;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.exflyer.oddiad.AppConstants;
import com.exflyer.oddiad.OddiAdApplication;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;


public class FirebaseInstanceIDService extends FirebaseMessagingService {



    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        try {
            if (remoteMessage != null && remoteMessage.getData().size() > 0) {
                Map<String, String> data = remoteMessage.getData();
                sendEventData(OddiAdApplication.getContext(),data.toString());

            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onDeletedMessages() {

    }

    public void sendEventData(Context mContext, String value) {
        Intent intent = new Intent();
        intent.setAction(AppConstants.LOCAL_BROADCAST_EVENT_RECEIVER);
        intent.putExtra("DATA",value);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }

}

