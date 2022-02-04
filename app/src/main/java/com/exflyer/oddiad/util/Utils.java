
package com.exflyer.oddiad.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.util.Base64;
import android.util.DisplayMetrics;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.exflyer.oddiad.AppConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Utils {

    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";



    /**
     * Dp 값을 pixel로 변환
     *
     * @param dp
     * @return
     */
    public static float convertDpToPixel(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    public static void launchAnotherApp(Context context, String packageName) {
        try {
            boolean isInstalledAnotherApp = isInstalledAnotherApp(context, packageName);

            if (isInstalledAnotherApp) {
                Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } else {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName));
                context.startActivity(intent);
            }
        } catch (Exception e) {
        }
    }

    private static boolean isInstalledAnotherApp(Context context, String packageName) {
        try {
            PackageManager packageManager = context.getPackageManager();
            Intent mainIntent = new Intent(Intent.ACTION_MAIN, null).addCategory(Intent.CATEGORY_LAUNCHER);
            List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(mainIntent, 0);

            for (ResolveInfo resolveInfo : resolveInfoList) {
                if (resolveInfo.activityInfo.packageName.startsWith(packageName)) {
                    return true;
                }
            }
        } catch (Exception e) {
        }

        return false;
    }



    public static String encodeBase64(byte[] enc) {
        String ret = "";
        try {
            ret = Base64.encodeToString(enc, Base64.NO_WRAP);
        } catch(Throwable e) {

        }

        return ret;
    }

    public static byte[] decodeBase64(String encoded) {
        byte[] ret = null;
        try {
            ret = Base64.decode(encoded, 0);
        } catch(Throwable e) {
        }

        return ret;
    }

    public static String urlEncoding(String data, String encType) {
        String ret = "";
        try {
            ret = URLEncoder.encode(data, encType);
        } catch(Throwable e) {

        }

        return ret;
    }

    public static String urlDecoding(String data, String decType) {
        String ret = "";
        try {
            ret = URLDecoder.decode(data, decType);
        } catch(Throwable e) {
        }

        return ret;
    }


    public static String convertByteArrayToString(byte[] convert) {
        String ret = "";
        try {
            StringBuilder strBuild = new StringBuilder();
            for(int i = 0; i<convert.length; i++) {
                strBuild.append(String.format("0x%02X", convert[i]));
                if(i < (convert.length-1)) strBuild.append(", ");
            }
            ret = strBuild.toString();
        } catch(Throwable e) {
        }

        return ret;
    }




    public static JsonObject merge(JsonObject source, JsonObject target)
    {
        JsonObject total = new JsonObject();

        for(Map.Entry<String, JsonElement> entry : source.entrySet())
        {
            total.add(entry.getKey(), entry.getValue());
        }

        for(Map.Entry<String, JsonElement> entry : target.entrySet())
        {
            total.add(entry.getKey(), entry.getValue());
        }
        return total;
    }


    public static JSONObject deepMerge(JSONObject source, JSONObject target) throws JSONException {
        for (Iterator<String> it = source.keys(); it.hasNext(); ) {
            String key = it.next();
            Object value = source.get(key);
            if (!target.has(key)) {
                // new value for "key":
                target.put(key, value);
            } else {
                // existing value for "key" - recursively deep merge:
                if (value instanceof JSONObject) {
                    JSONObject valueJson = (JSONObject)value;
                    deepMerge(valueJson, target.getJSONObject(key));
                } else {
                    target.put(key, value);
                }
            }
        }
        return target;
    }



    public static String nowTimeYYYYMMDDHHMM() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd. HH:mm");
        Date now = new Date(System.currentTimeMillis());
        return format.format(now);
    }



    public static String nowTimeHH() {
        SimpleDateFormat format = new SimpleDateFormat("HH");
        Date now = new Date(System.currentTimeMillis());
        return format.format(now);
    }

    public static String nowTimeYYYYMMDD() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Date now = new Date(System.currentTimeMillis());
        return format.format(now);
    }

    public static String nowTimeYYYYMMDDHHMMSS() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date now = new Date(System.currentTimeMillis());
        return format.format(now);
    }

    public static boolean get5MinuteDifference(String beforeTime, String nowTime )
    {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
            Date beforeTime_date = format.parse(beforeTime);
            Date nowTime_date = format.parse(nowTime);

            long diff = nowTime_date.getTime() - beforeTime_date.getTime() ;
            long min = diff / 60000;

            if(min <= 5)
                return true;

        }catch (Exception e)
        {
            return false;
        }

        return false;
    }

    public static String getLocale(Context context) {
        String localefoLaunguage;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            localefoLaunguage = context.getResources().getConfiguration().getLocales().get(0).getLanguage();
        } else {
            localefoLaunguage = context.getResources().getConfiguration().locale.getLanguage();
        }
        return localefoLaunguage;
    }


    final static  String[] files={
            "/sbin/su","/system/su","/system/bin/su","/system/sbin/su"
            ,"/system/xbin/su","/system/xbin/mu","/system/bin/.ext/.su"
            ,"/system/bin/.ext","/system/xbin/.ext"
            ,"/system/usr/su-backup","/data/data/com.noshufou.android.su"
            ,"/system/app/Superuser.apk","/system/sd/xbin/su","/system/bin/failsafe/su"
            ,"/data/local/su","/su/bin/su"
    };

    final static  String[] packages={
            "com.devadvance.rootcloak",
            "com.devadvance.rootcloakplus",
            "com.koushikdutta.superuser",
            "com.thirdparty.superuser",
            "eu.chainfire.supersu",
            "de.robv.android.xposed.installer",
            "com.saurik.substrate",
            "com.zachspong.temprootremovejb",
            "com.amphoras.hidemyroot",
            "com.amphoras.hidemyrootadfree",
            "com.formyhm.hiderootPremium",
            "com.formyhm.hideroot",
            "com.noshufou.android.su",
            "com.noshufou.android.su.elite",
            "com.yellowes.su",
            "com.topjohnwu.magisk",
            "com.kingroot.kinguser",
            "com.kingo.root",
            "com.smedialink.oneclickroot",
            "com.zhiqupk.root.global",
            "com.alephzain.framaroot"

    };

    public static  boolean checkRooting(Context context)
    {
        if(!GLog.isRootingCheck)
            return false;

        String buildTags = Build.TAGS;
        Process process= null;
        PackageManager pm = context.getPackageManager();
        if(buildTags!=null && buildTags.contains("test-keys"))
            return true;
        for(String path :files) {
            if(new File(path).exists())
                return true;
        }
        try {
            process = Runtime.getRuntime().exec(new String[]{"/system/xbin/which","su"});
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            if(in.readLine()!=null)
                return true;
        }catch (Throwable e){ }finally {
            if(process !=null)
                process.destroy();
        }

        if(pm != null){
            for (String pk :packages) {
                try {
                    pm.getPackageInfo(pk,0);
                    return true;
                }catch (Exception e) { }
            }
        }

        if(!isInstalledByGooglePlayStore(context))
            return true;

        try {
            Runtime.getRuntime().exec("su");
            return true;
        }catch (Exception e)
        {
            return false;
        }
    }



    public static boolean isInstalledByGooglePlayStore(Context context) {
        boolean result = false;

        String packageName = context.getPackageName();

        PackageManager m = context.getPackageManager();
        String installerPackageName = m.getInstallerPackageName(packageName);

        if ("com.android.vending".equalsIgnoreCase(installerPackageName)) {
            result = true;
        } else {
            String sourceApk = null;
            try {
                ApplicationInfo ai = m.getApplicationInfo(packageName, 0);
                sourceApk = ai.publicSourceDir;
            } catch (Throwable e) {
                e.printStackTrace();
            }

            if (sourceApk != null && sourceApk.startsWith("/system/")) {
                result = true;
            }

        }

        return result;
    }


    public static String getSingMD5(Context context)
    {
        String cert = "";
        try {
            Signature sig = context.getPackageManager().getPackageInfo(context.getPackageName(),PackageManager.GET_SIGNATURES).signatures[0];
            MessageDigest msgDigest = MessageDigest.getInstance("MD5");
            msgDigest.update(sig.toByteArray());
            byte byteData[] = msgDigest.digest();
            StringBuffer sb = new StringBuffer();
            for(int i = 0; i< byteData.length; i++)
            {
                sb.append(Integer.toString((byteData[i]&0xff)+0x100,16).substring(1));
            }
            cert = sb.toString();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return cert;
    }

    public static String getSingSHA256(Context context)
    {
        String cert = "";
        try {
            Signature sig = context.getPackageManager().getPackageInfo(context.getPackageName(),PackageManager.GET_SIGNATURES).signatures[0];
            MessageDigest msgDigest = MessageDigest.getInstance("SHA-256");
            msgDigest.update(sig.toByteArray());
            byte btyeData[] = msgDigest.digest();
            StringBuffer sb = new StringBuffer();
            for(int i = 0; i< btyeData.length; i++)
            {
                sb.append(Integer.toString((btyeData[i]&0xff)+0x100,16).substring(1));
            }
            cert = sb.toString();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return cert;
    }


    // 현재 시간을 "yyyy-MM-dd hh:mm:ss"로 표시하는 메소드
    public static String getTime()
    {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String getTime = dateFormat.format(date);
        return getTime;
    }

    public static void delDownLoadFile()
    {
        try {
            File storageDir = new File(AppConstants.LOCAL_SAVE_DATA_PATH);
            File[] files = storageDir.listFiles();

            for(int i = 0 ; i< files.length; i++)
            {
                files[i].delete();
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static String byteCalculation(String bytes) {
        String retFormat = "0";
        try {

            Double size = Double.parseDouble(bytes);

            String[] s = { "bytes", "KB", "MB", "GB", "TB", "PB" };

            if (bytes != "0") {
                int idx = (int) Math.floor(Math.log(size) / Math.log(1024));
                DecimalFormat df = new DecimalFormat("#,###.##");
                double ret = ((size / Math.pow(1024, Math.floor(idx))));
                retFormat = df.format(ret) + " " + s[idx];
            } else {
                retFormat += " " + s[0];
            }
        }catch (Exception e)
        {
            retFormat = "0";
        }

        return retFormat;
    }


    public static byte[] ivBytes = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
    public static String secretKey = "oddi#ad#app@key!";

    //AES256 암호화
    public static String AES_Encode(String str)	throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {

        byte[] textBytes = str.getBytes("UTF-8");
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        SecretKeySpec newKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "AES");
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);

        return Base64.encodeToString(cipher.doFinal(textBytes), 0);
    }

    //AES256 복호화
    public static String AES_Decode(String str)	throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {

        byte[] textBytes =Base64.decode(str,0);
        //byte[] textBytes = str.getBytes("UTF-8");
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        SecretKeySpec newKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
        return new String(cipher.doFinal(textBytes), "UTF-8");
    }




    public static String getNowTimestamp()
    {
        try{
            Long tsLong = System.currentTimeMillis();
            return tsLong.toString();
        }catch (Exception e)
        {

            return "";
        }

    }
}
