package com.exflyer.oddiad.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.util.DisplayMetrics;

public class ViewUtil {

    private static Typeface typefaceBlackFont;
    private static Typeface typefaceBoldFont;
    private static Typeface typefaceRegularFont;
    private static Typeface typefaceRBBlackFont;
    private static Typeface typefaceRBRegularFont;

    //dp를 px로 변환 (dp를 입력받아 px을 리턴)
    public static float dpToPx(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    //px을 dp로 변환 (px을 입력받아 dp를 리턴)
    public static float pxTodp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }


    public static Typeface getBoldFont(Context mContext) {
        try {
            if (typefaceBoldFont == null) {
                typefaceBoldFont = Typeface.createFromAsset(mContext.getAssets(), "font/notosanskrbold.otf");
            }
        } catch (Exception e) {


        }
        return typefaceBoldFont;
    }

    public static Typeface getRegularFont(Context mContext) {
        try {
            if (typefaceRegularFont == null) {
                typefaceRegularFont = Typeface.createFromAsset(mContext.getAssets(), "font/notosanskrregular.otf");
            }
        } catch (Exception e) {


        }
        return typefaceRegularFont;

    }

    public static Typeface getBlackFont(Context mContext) {
        try {
            if (typefaceBlackFont == null) {
                typefaceBlackFont = Typeface.createFromAsset(mContext.getAssets(), "font/notosanskrblack.otf");
            }
        } catch (Exception e) {


        }
        return typefaceBlackFont;
    }

    public static Typeface getRBBlackFont(Context mContext) {
        try {
            if (typefaceRBBlackFont == null) {
                typefaceRBBlackFont = Typeface.createFromAsset(mContext.getAssets(), "font/robotoblack.ttf");
            }
        } catch (Exception e) {


        }
        return typefaceRBBlackFont;
    }


    public static Typeface getRBRegularFont(Context mContext) {
        try {
            if (typefaceRBRegularFont == null) {
                typefaceRBRegularFont = Typeface.createFromAsset(mContext.getAssets(), "font/robotoregular.ttf");
            }
        } catch (Exception e) {

        }
        return typefaceRBRegularFont;
    }
}
