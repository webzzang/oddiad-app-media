package com.exflyer.oddiad.network.model.sidead;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


import java.io.Serializable;

public class SideAdModel implements Serializable, Parcelable {


    private static final long serialVersionUID = 6096895836075754305L;
    @SerializedName("code")
    @Expose
    public String code;

    @SerializedName("message")
    @Expose
    public String message;

    @SerializedName("data")
    @Expose
    public Data data;


    public final static Creator<SideAdModel> CREATOR = new Creator<SideAdModel>() {
        @SuppressWarnings({"unchecked"})
        public SideAdModel createFromParcel(Parcel in) {
            return new SideAdModel(in);
        }

        public SideAdModel[] newArray(int size) {
            return (new SideAdModel[size]);
        }
    };


    protected SideAdModel(Parcel in) {
        this.code = ((String) in.readValue((String.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        this.data = ((Data) in.readValue((Data.class.getClassLoader())));
    }

    public SideAdModel() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(code);
        dest.writeValue(message);
        dest.writeValue(data);
    }

    public int describeContents() {
        return 0;
    }
}
