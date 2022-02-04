package com.exflyer.oddiad.network.model.mainad;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.exflyer.oddiad.network.model.common.AdList;

import java.io.Serializable;
import java.util.List;


public class Data implements Serializable, Parcelable {


    private static final long serialVersionUID = 8665409457859854048L;
    @SerializedName("ad_screen_type")
    @Expose
    public String ad_screen_type;

    @SerializedName("ad_list")
    @Expose
    public List<AdList> ad_list;

    @SerializedName("ad_type")
    @Expose
    public String ad_type;


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
        this.ad_screen_type = ((String) in.readValue((Object.class.getClassLoader())));
        in.readList(this.ad_list, (AdList.class.getClassLoader()));
        this.ad_type = ((String) in.readValue((Object.class.getClassLoader())));
    }

    public Data() {
    }

    public String getAd_screen_type() {
        return ad_screen_type;
    }

    public void setAd_screen_type(String ad_screen_type) {
        this.ad_screen_type = ad_screen_type;
    }

    public List<AdList> getAd_list() {
        return ad_list;
    }

    public void setAd_list(List<AdList> ad_list) {
        this.ad_list = ad_list;
    }

    public String getAd_type() {
        return ad_type;
    }

    public void setAd_type(String ad_type) {
        this.ad_type = ad_type;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(ad_screen_type);
        dest.writeValue(ad_list);
        dest.writeValue(ad_type);
    }

    public int describeContents() {
        return 0;
    }

}
