
package com.exflyer.oddiad.preference;

import android.content.Context;
import android.content.SharedPreferences;


public class UserPreference {

    public static UserPreference mInstance;
    private final String USER_PREFERENCE_NAME = "user.preference.name.exflyer.oddiad";

    private final String USER_FCM_ID = "user.fcm.id";

    private final String USER_REBOOT_COMPLETED = "user.reboot.completed";

    //디바이스 아이디저장
    private final String USER_DEVICE_ID = "user.device.id";

    private final String USER_DEVICE_BOOT_COMPLETED = "user.device.boot.completed";
    private final String USER_DEVICE_EXCEPTION_TIME = "user.device.exception.time";
    private final String USER_DEVICE_EXCEPTION_COUNT = "user.device.exception.count";

    private static Context mContext;


    public UserPreference()
    {

    }

    public void setInstance(Context context)
    {
        mContext = context;
    }

    public static synchronized UserPreference getInstance() {
        if (mInstance == null)
        {
            mInstance = new UserPreference();
        }
        return mInstance;
    }

    public void saveUserFCMID(String fcmid) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(USER_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(USER_FCM_ID, fcmid);
        editor.apply();
    }

    public  String getUserFCMID(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(USER_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(USER_FCM_ID, null);
    }


    public  void saveUserRebootAlarm( String setYn) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(USER_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(USER_REBOOT_COMPLETED, setYn);
        editor.apply();
    }

    public  String getUserRebootAlarm(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(USER_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(USER_REBOOT_COMPLETED, "N");
    }


    public void saveDeviceID(String device_id) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(USER_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(USER_DEVICE_ID, device_id);
        editor.apply();
    }

    public String getDeviceID(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(USER_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(USER_DEVICE_ID, null);
    }


    public void saveBootCompleted(String bootCompleted) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(USER_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(USER_DEVICE_BOOT_COMPLETED, bootCompleted);
        editor.apply();
    }

    public String getBootCompleted(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(USER_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(USER_DEVICE_BOOT_COMPLETED, "N");
    }

    public void saveExceptionTime(String exceptionTime) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(USER_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(USER_DEVICE_EXCEPTION_TIME, exceptionTime);
        editor.apply();
    }

    public String getExceptionTime(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(USER_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(USER_DEVICE_EXCEPTION_TIME, "");
    }


    public void saveExceptionCount(int exceptionCount) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(USER_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(USER_DEVICE_EXCEPTION_COUNT, exceptionCount);
        editor.apply();
    }

    public int getExceptionCount(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(USER_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(USER_DEVICE_EXCEPTION_COUNT, 0);
    }
}
