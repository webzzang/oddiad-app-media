package com.exflyer.oddiad.network.model.common;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class AdList implements Serializable, Parcelable {


    private static final long serialVersionUID = 8066723709970913969L;
    @SerializedName("content_id")
    @Expose
    public String content_id;

    @SerializedName("content_type")
    @Expose
    public String content_type;


    @SerializedName("content_url")
    @Expose
    public List<String> content_url;


    @SerializedName("content_duration")
    @Expose
    public String content_duration;

    public final static Creator<AdList> CREATOR = new Creator<AdList>() {
        @SuppressWarnings({"unchecked"})
        public AdList createFromParcel(Parcel in) {
            return new AdList(in);
        }

        public AdList[] newArray(int size) {
            return (new AdList[size]);
        }
    };


    protected AdList(Parcel in) {
        this.content_id = ((String) in.readValue((Object.class.getClassLoader())));
        this.content_type = ((String) in.readValue((Object.class.getClassLoader())));
        in.readList(this.content_url, (String.class.getClassLoader()));
        this.content_duration = ((String) in.readValue((Object.class.getClassLoader())));

    }

    public AdList() {
    }

    public String getContent_id() {
        return content_id;
    }

    public void setContent_id(String content_id) {
        this.content_id = content_id;
    }

    public String getContent_type() {
        return content_type;
    }

    public void setContent_type(String content_type) {
        this.content_type = content_type;
    }

    public List<String> getContent_url() {
        return content_url;
    }

    public void setContent_url(List<String> content_url) {
        this.content_url = content_url;
    }

    public String getContent_duration() {
        return content_duration;
    }

    public void setContent_duration(String content_duration) {
        this.content_duration = content_duration;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(content_id);
        dest.writeValue(content_type);
        dest.writeValue(content_url);
        dest.writeValue(content_duration);

    }

    public int describeContents() {
        return 0;
    }

}
