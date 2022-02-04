package com.exflyer.oddiad.network.model.common;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class CommonResponse implements Serializable, Parcelable {


    private static final long serialVersionUID = -8549635099815467539L;

    @SerializedName("code")
    @Expose
    public String code;

    @SerializedName("message")
    @Expose
    public String message;



    public final static Creator<CommonResponse> CREATOR = new Creator<CommonResponse>() {
        @SuppressWarnings({"unchecked"})
        public CommonResponse createFromParcel(Parcel in) {
            return new CommonResponse(in);
        }

        public CommonResponse[] newArray(int size) {
            return (new CommonResponse[size]);
        }
    };



    protected CommonResponse(Parcel in) {
        this.code = ((String) in.readValue((String.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));

    }

    public CommonResponse() {
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

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(code);
        dest.writeValue(message);
    }

    public int describeContents() {
        return 0;
    }

}
