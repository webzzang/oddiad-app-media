package com.exflyer.oddiad.network.model.weather;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Data implements Serializable, Parcelable {

    private static final long serialVersionUID = -8053965423088824000L;
    //    하늘 상태
    @SerializedName("sky")
    public String sky;

    //    하늘 상태 이미지 아이콘 파일 명
    @SerializedName("icon")
    public String icon;

    //    기온
    @SerializedName("tmp")
    public String tmp;

    //    일 최저 기온
    @SerializedName("tmn")
    public String tmn;

    //    일 최고 기온
    @SerializedName("tmx")
    public String tmx;

    //    강수 확률
    @SerializedName("pop")
    public String pop;

    //    습도
    @SerializedName("reh")
    public String reh;

    //    풍속
    @SerializedName("wsd")
    public String wsd;

    //    미세먼지 농도
    @SerializedName("pm10")
    public String pm10;

    //    초미세먼지 농도
    @SerializedName("pm25")
    public String pm25;

    //    미세먼지 등급
    @SerializedName("pm10_grade")
    public String pm10_grade;

    //    초미세먼지 등급
    @SerializedName("pm25_grade")
    public String pm25_grade;

    //    위치정보
    @SerializedName("addr")
    public String addr;


    public final static Creator<Data> CREATOR = new Creator<Data>() {
        @SuppressWarnings({"unchecked"})
        public Data createFromParcel(Parcel in) {
            return new Data(in);
        }

        public Data[] newArray(int size) {
            return (new Data[size]);
        }
    };



    protected Data(Parcel in) {
        this.sky = ((String) in.readValue((String.class.getClassLoader())));
        this.icon = ((String) in.readValue((String.class.getClassLoader())));
        this.tmp = ((String) in.readValue((String.class.getClassLoader())));
        this.tmn = ((String) in.readValue((String.class.getClassLoader())));
        this.tmx = ((String) in.readValue((String.class.getClassLoader())));
        this.pop = ((String) in.readValue((String.class.getClassLoader())));
        this.reh = ((String) in.readValue((String.class.getClassLoader())));
        this.wsd = ((String) in.readValue((String.class.getClassLoader())));
        this.pm10 = ((String) in.readValue((String.class.getClassLoader())));
        this.pm25 = ((String) in.readValue((String.class.getClassLoader())));
        this.pm10_grade = ((String) in.readValue((String.class.getClassLoader())));
        this.pm25_grade = ((String) in.readValue((String.class.getClassLoader())));
        this.addr = ((String) in.readValue((String.class.getClassLoader())));

    }

    public Data() {
    }


    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(sky);
        dest.writeValue(icon);
        dest.writeValue(tmp);
        dest.writeValue(tmn);
        dest.writeValue(tmx);
        dest.writeValue(pop);
        dest.writeValue(reh);
        dest.writeValue(wsd);
        dest.writeValue(pm10);
        dest.writeValue(pm25);
        dest.writeValue(pm10_grade);
        dest.writeValue(pm25_grade);
        dest.writeValue(addr);
    }

    public int describeContents() {
        return 0;
    }

}
