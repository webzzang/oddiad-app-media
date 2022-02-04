package com.exflyer.oddiad.network.model.devicecreate;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Data implements Serializable, Parcelable {


    private static final long serialVersionUID = -5934660238006243973L;
    @SerializedName("device_id")
    @Expose
    public String device_id;




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
        this.device_id = ((String) in.readValue((Object.class.getClassLoader())));

    }

    public Data() {
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(device_id);

    }

    public int describeContents() {
        return 0;
    }

}
