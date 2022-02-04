package com.exflyer.oddiad.network.model.bottomad;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.exflyer.oddiad.network.model.common.AdList;

import java.io.Serializable;
import java.util.List;


public class Data implements Serializable, Parcelable {


    private static final long serialVersionUID = 1214723909777582104L;
    @SerializedName("ad_list")
    @Expose
    public List<AdList> ad_list;


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

        in.readList(this.ad_list, (AdList.class.getClassLoader()));
    }

    public Data() {
    }


    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(ad_list);
    }

    public int describeContents() {
        return 0;
    }

}
