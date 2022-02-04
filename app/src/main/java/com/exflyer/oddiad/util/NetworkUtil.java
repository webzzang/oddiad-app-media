
package com.exflyer.oddiad.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;

import androidx.core.content.ContextCompat;

import java.lang.reflect.Method;

public class NetworkUtil {
    public static final boolean isConnected(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

            return isConnected;
        } catch (Exception e) {
        }

        return false;
    }

    public static String getNetworkCellName(Context context) {
        boolean isWifiConnected = isWifiConnected(context);
        if (isWifiConnected) {
            return "WIFI";
        } else {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                return "LTE";
            } else {
                int nrStatus = getNrStatus(context);
                if (nrStatus == 3) {
                    return "5G";
                } else {
                    //테스트 단말로 확인중이라 5G 처리
                    return "LTE";
//                    return "5G";
                }
            }
        }
    }

    public static boolean isWifiConnected(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            Network activeNetwork = connectivityManager.getActiveNetwork();
            NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork);
            if (networkCapabilities == null) {
                return false;
            }

            boolean isConnectedWifi = networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI);
            return isConnectedWifi;
        } catch (Exception e) {
        }

        return false;
    }

    public static int getNrStatus(Context context) {
        // The device isn't camped on an LTE cell or the LTE cell doesn't support E-UTRA-FIVE_G\n
        // Dual Connectivity(EN-DC).
        // public static final int NR_STATUS_NONE = -1;

        // The device is camped on an LTE cell that supports E-UTRA-FIVE_G Dual Connectivity(EN-DC) but
        // either the use of dual connectivity with FIVE_G(DCNR) is restricted or FIVE_G is not supported by
        // the selected PLMN.
        // public static final int NR_STATUS_RESTRICTED = 1;

        // The device is camped on an LTE cell that supports E-UTRA-FIVE_G Dual Connectivity(EN-DC) and both\n" +
        // the use of dual connectivity with FIVE_G(DCNR) is not restricted and FIVE_G is supported by the\n" +
        // selected PLMN.
        // public static final int NR_STATUS_NOT_RESTRICTED = 2;

        // The device is camped on an LTE cell that supports E-UTRA-FIVE_G Dual Connectivity(EN-DC) and
        // also connected to at least one 5G cell as a secondary serving cell.
        // public static final int NR_STATUS_CONNECTED = 3;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                int readPhoneStatusGranted = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE);
                if (readPhoneStatusGranted == PackageManager.PERMISSION_GRANTED) {
                    TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                    ServiceState serviceState = telephonyManager.getServiceState();

                    Class<?> cls = Class.forName("android.telephony.ServiceState");
                    Method method = cls.getMethod("getNrStatus");
                    int nrStatus = (int) method.invoke(serviceState);

                    return nrStatus;
                }
            } catch (Exception e) {
                //if(Log.DBG) e.printStackTrace();
            }

            return -1;
        }

        return -1;
    }
}
