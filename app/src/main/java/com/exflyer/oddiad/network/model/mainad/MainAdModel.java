package com.exflyer.oddiad.network.model.mainad;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


import java.io.Serializable;

public class MainAdModel implements Serializable, Parcelable {


    private static final long serialVersionUID = 4926781259987669453L;
    @SerializedName("code")
    @Expose
    public String code;

    @SerializedName("message")
    @Expose
    public String message;

    @SerializedName("data")
    @Expose
    public Data data;


    public final static Creator<MainAdModel> CREATOR = new Creator<MainAdModel>() {
        @SuppressWarnings({"unchecked"})
        public MainAdModel createFromParcel(Parcel in) {
            return new MainAdModel(in);
        }

        public MainAdModel[] newArray(int size) {
            return (new MainAdModel[size]);
        }
    };


    protected MainAdModel(Parcel in) {
        this.code = ((String) in.readValue((String.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        this.data = ((Data) in.readValue((Data.class.getClassLoader())));
    }

    public MainAdModel() {
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
