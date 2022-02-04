
package com.exflyer.oddiad;

import android.app.Activity;
import android.content.Context;

import androidx.core.app.ActivityCompat;
import androidx.multidex.MultiDexApplication;

import com.exflyer.oddiad.preference.UserPreference;

import java.io.File;


public class OddiAdApplication extends MultiDexApplication {
    public static OddiAdApplication oddiAdApplication;


    @Override
    public void onCreate() {
        super.onCreate();

        oddiAdApplication = this;

        UserPreference.getInstance().setInstance(this);

        AppConstants.LOCAL_SAVE_DATA_PATH = getExternalFilesDir(null) + AppConstants.LOCAL_SAVE_DATA_FOLDER;
        try
        {
            File file = new File( AppConstants.LOCAL_SAVE_DATA_PATH) ;
            if (file.exists() == false) {
                file.mkdirs();
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void killApp(Activity activity) {
        ActivityCompat.finishAffinity(activity);
        //System.exit(0);
    }

    public static Context getContext() {
        return oddiAdApplication.getApplicationContext();
    }

    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }

    public static OddiAdApplication getInstance() {
        return oddiAdApplication;
    }

}
