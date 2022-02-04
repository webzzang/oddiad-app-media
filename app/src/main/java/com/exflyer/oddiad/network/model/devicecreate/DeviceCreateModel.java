package com.exflyer.oddiad.network.model.devicecreate;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DeviceCreateModel implements Serializable, Parcelable {

    @SerializedName("code")
    @Expose
    public String code;

    @SerializedName("message")
    @Expose
    public String message;


    @SerializedName("data")
    @Expose
    public Data data;


    public final static Creator<DeviceCreateModel> CREATOR = new Creator<DeviceCreateModel>() {
        @SuppressWarnings({"unchecked"})
        public DeviceCreateModel createFromParcel(Parcel in) {
            return new DeviceCreateModel(in);
        }

        public DeviceCreateModel[] newArray(int size) {
            return (new DeviceCreateModel[size]);
        }
    };


    protected DeviceCreateModel(Parcel in) {
        this.code = ((String) in.readValue((String.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        this.data = ((Data) in.readValue((Data.class.getClassLoader())));
    }

    public DeviceCreateModel() {
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
