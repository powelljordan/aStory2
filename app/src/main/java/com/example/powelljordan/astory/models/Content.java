package com.example.powelljordan.astory.models;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by jorda on 12/26/2017.
 */

public class Content implements Serializable, Parcelable {
    public String text;
    public String imageURL;
    public String videoURL;
    public String video360URL;
    public String musicURL;

    public Content(){}

    public String toString(){
        HashMap<String, String> contentObj = new HashMap<>();
        contentObj.put("text", this.text);
        contentObj.put("imageURL", this.imageURL);
        contentObj.put("videoURL", this.videoURL);
        contentObj.put("video360URL", this.video360URL);
        contentObj.put("musicURL", this.musicURL);
        return contentObj.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.text);
        dest.writeString(this.imageURL);
        dest.writeString(this.videoURL);
        dest.writeString(this.video360URL);
        dest.writeString(this.musicURL);
    }

    protected Content(Parcel in) {
        this.text = in.readString();
        this.imageURL = in.readString();
        this.videoURL = in.readString();
        this.video360URL = in.readString();
        this.musicURL = in.readString();
    }

    public static final Parcelable.Creator<Content> CREATOR = new Parcelable.Creator<Content>() {
        @Override
        public Content createFromParcel(Parcel source) {
            return new Content(source);
        }

        @Override
        public Content[] newArray(int size) {
            return new Content[size];
        }
    };
}
