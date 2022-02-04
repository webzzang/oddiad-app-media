package com.exflyer.oddiad.manager;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;

import com.exflyer.oddiad.AppConstants;

import java.io.File;

public class FileDownloadManager {

    private OnCallBackListener mOnCallBackListener =null;
    private long mDownloadId ;
    private String mDownloadpath;
    private boolean isRegisterReceiver = false;
    public static FileDownloadManager mInstance;
    public static FileDownloadManager getInstance() {
        if (mInstance == null)
        {
            mInstance = new FileDownloadManager();
        }
        return mInstance;
    }

    public FileDownloadManager() {

    }

    public interface OnCallBackListener {
        void onFileDownLoadStart(long downloadId, String fileName);
        void onFileDownLoadComplete(long downloadId, String filePath);
    }

    public void startDownload(Context context, String url, OnCallBackListener onCallBackListener) {
        setRegisterReceiver(context);
        mOnCallBackListener = onCallBackListener;
        String fileName = url.substring(url.lastIndexOf("/")+1,url.length());
        File dlpath = new File(AppConstants.LOCAL_SAVE_DATA_PATH ,fileName);

        // Make a request
        DownloadManager.Request request
                = new DownloadManager.Request(Uri.parse(url))
                .setAllowedOverRoaming(false)
                .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI)
                .setTitle(fileName+"Down Load..")
                .setDestinationUri(Uri.fromFile(dlpath))
                .setDescription("Down Load");
        mDownloadpath = dlpath.getPath();
        // enqueue
        DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        mDownloadId = dm.enqueue(request);
        if(mOnCallBackListener !=null)
            mOnCallBackListener.onFileDownLoadStart(mDownloadId, fileName);

    }

    public void setRegisterReceiver(Context context)
    {
        if(!isRegisterReceiver)
        {
            isRegisterReceiver = true;
            context.registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        }

    }

    public void setUnRegisterReceiver(Context context)
    {
        isRegisterReceiver = false;
        context.unregisterReceiver(receiver);
    }


    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                long fileSize = intent.getLongExtra(DownloadManager.INTENT_EXTRAS_SORT_BY_SIZE, -1);

                if(mOnCallBackListener !=null)
                    mOnCallBackListener.onFileDownLoadComplete(downloadId, mDownloadpath);


            }
        }
    };

}
