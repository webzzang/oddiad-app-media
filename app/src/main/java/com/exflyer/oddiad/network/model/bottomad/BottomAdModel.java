package com.exflyer.oddiad.network.model.bottomad;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


import java.io.Serializable;

public class BottomAdModel implements Serializable, Parcelable {


    private static final long serialVersionUID = -2457018080430631489L;
    @SerializedName("code")
    @Expose
    public String code;

    @SerializedName("message")
    @Expose
    public String message;


    @SerializedName("data")
    @Expose
    public Data data;


    public final static Creator<BottomAdModel> CREATOR = new Creator<BottomAdModel>() {
        @SuppressWarnings({"unchecked"})
        public BottomAdModel createFromParcel(Parcel in) {
            return new BottomAdModel(in);
        }

        public BottomAdModel[] newArray(int size) {
            return (new BottomAdModel[size]);
        }
    };


    protected BottomAdModel(Parcel in) {
        this.code = ((String) in.readValue((String.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        this.data = ((Data) in.readValue((Data.class.getClassLoader())));
    }

    public BottomAdModel() {
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
