package com.exflyer.oddiad.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * 로그 통합관리
 */
public class GLog {
    // 배포 시 isRetrofitLog, isLogPrint false 처리 필요.
    public static  boolean isRetrofitLog = true;
    private static  boolean isLogPrint = true;
    public static final boolean isRequestHeaderLog = true;
    public static final boolean isResponseHeaderLog = true;
    public static  boolean isRootingCheck = false;
    private static final String TAG = "====";
    private static final int MAX_INDEX = 2000;
    private static final int LOG_DEBUG = 0;
    private static final int LOG_VERBOSE = 1;
    private static final int LOG_INFO = 2;
    private static final int LOG_WARN = 3;
    private static final int LOG_ERROR = 4;

    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * 현재 디버그모드여부를 리턴
     *
     * @param context
     * @return
     */
    public static boolean isDebuggable(Context context) {
        boolean debuggable = false;

        PackageManager pm = context.getPackageManager();
        try {
            ApplicationInfo appinfo = pm.getApplicationInfo(context.getPackageName(), 0);
            debuggable = (0 != (appinfo.flags & ApplicationInfo.FLAG_DEBUGGABLE));
        } catch (PackageManager.NameNotFoundException e) {
            /* debuggable variable will remain false */
        }

        return debuggable;
    }

    public static synchronized void d(String msg) {
        if (isLogPrint) dLong(msg, LOG_DEBUG);
    }

    public static synchronized void v(String msg) {
        if (isLogPrint) dLong(msg, LOG_VERBOSE);
    }

    public static synchronized void i(String msg) {
        if (isLogPrint) dLong(msg, LOG_INFO);
    }

    public static synchronized void w(String msg) {
        if (isLogPrint) dLong(msg, LOG_WARN);
    }

    public static synchronized void e(String msg) {
        if (isLogPrint) dLong(msg, LOG_ERROR);
    }

    public static void e(String msg, Exception e) {
        if (isLogPrint) Log.e(TAG, msg, e);
    }

    public static String toJson(Object src) {
        //JSON 이쁘게 출력
        if (isLogPrint) return gson.toJson(src);
        return "";
    }

    public static String getPretty(String jsonString) {
        final String INDENT = "    ";
        StringBuffer prettyJsonSb = new StringBuffer();

        int indentDepth = 0;
        String targetString = null;
        for (int i = 0; i < jsonString.length(); i++) {
            targetString = jsonString.substring(i, i + 1);
            if (targetString.equals("{") || targetString.equals("[")) {
                prettyJsonSb.append(targetString).append("\n");
                indentDepth++;
                for (int j = 0; j < indentDepth; j++) {
                    prettyJsonSb.append(INDENT);
                }
            } else if (targetString.equals("}") || targetString.equals("]")) {
                prettyJsonSb.append("\n");
                indentDepth--;
                for (int j = 0; j < indentDepth; j++) {
                    prettyJsonSb.append(INDENT);
                }
                prettyJsonSb.append(targetString);
            } else if (targetString.equals(",")) {
                prettyJsonSb.append(targetString);
                prettyJsonSb.append("\n");
                for (int j = 0; j < indentDepth; j++) {
                    prettyJsonSb.append(INDENT);
                }
            } else {
                prettyJsonSb.append(targetString);
            }
        }
        return prettyJsonSb.toString();
    }

    /**
     * 메서드명 포함한 로그 문자열로 리턴
     */
    private static String getWithMethodName(String log) {
        //로그 찍을때 메서드명도 찍기
        try {
            StackTraceElement ste = Thread.currentThread().getStackTrace()[5];
            StringBuffer sb = new StringBuffer();
            sb.append("[");
            sb.append(ste.getFileName().replace(".java", ""));
            sb.append("::");
            sb.append(ste.getMethodName());
            sb.append("]   ");
            sb.append(log);
            return sb.toString();
        } catch (Throwable e) {
            e.printStackTrace();
            return log;
        }
    }

    private static synchronized void dLong(String theMsg, int logType) {
        // String to be logged is longer than the max...
        if (theMsg.length() > MAX_INDEX) {
            String theSubstring = theMsg.substring(0, MAX_INDEX);
            int theIndex = MAX_INDEX;

            //로그 찍을때 메서드명도 찍기
            theSubstring = getWithMethodName(theSubstring);

            // Log the substring.
            switch (logType) {
                case LOG_DEBUG:
                    Log.d(TAG, theSubstring);
                    break;
                case LOG_VERBOSE:
                    Log.v(TAG, theSubstring);
                    break;
                case LOG_INFO:
                    Log.i(TAG, theSubstring);
                    break;
                case LOG_WARN:
                    Log.w(TAG, theSubstring);
                    break;
                case LOG_ERROR:
                    Log.e(TAG, theSubstring);
                    break;
            }

            // Recursively log the remainder.
            dLong(theMsg.substring(theIndex), logType);
        }
        // String to be logged is shorter than the max...
        else {
            //로그 찍을때 메서드명도 찍기
            theMsg = getWithMethodName(theMsg);

            // Log the substring.
            switch (logType) {
                case LOG_DEBUG:
                    Log.d(TAG, theMsg);
                    break;
                case LOG_VERBOSE:
                    Log.v(TAG, theMsg);
                    break;
                case LOG_INFO:
                    Log.i(TAG, theMsg);
                    break;
                case LOG_WARN:
                    Log.w(TAG, theMsg);
                    break;
                case LOG_ERROR:
                    Log.e(TAG, theMsg);
                    break;
            }
        }
    }
}
