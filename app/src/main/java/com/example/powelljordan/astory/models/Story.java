package com.example.powelljordan.astory.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.firebase.firestore.GeoPoint;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by jorda on 12/22/2017.
 */

public class Story implements Serializable, Parcelable {
    public String id;
    public String title;
    public String author;
    public GeoPoint location;
    public Content content = new Content();
    public Date date;
    public Audience audience = new Audience();

    public Story(){}

    public String toString(){
        HashMap<String, Object> storyObj = new HashMap<>();
        storyObj.put("id", this.id);
        storyObj.put("title", this.title);
        storyObj.put("author", this.author);
        storyObj.put("content", this.content.toString());
        storyObj.put("location", this.location);
        storyObj.put("date", this.date);
        storyObj.put("audience", this.audience);
        return storyObj.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.author);
        dest.writeDouble(this.location.getLatitude());
        dest.writeDouble(this.location.getLongitude());
        dest.writeParcelable(this.content, flags);
        dest.writeLong(this.date != null ? this.date.getTime() : -1);
        dest.writeParcelable(this.audience, flags);
    }

    protected Story(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.author = in.readString();
        Double lat = in.readDouble();
        Double lng = in.readDouble();
        this.location = new GeoPoint(lat, lng);
        this.content = in.readParcelable(Content.class.getClassLoader());
        long tmpDate = in.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
        this.audience = in.readParcelable(Audience.class.getClassLoader());
    }

    public static final Parcelable.Creator<Story> CREATOR = new Parcelable.Creator<Story>() {
        @Override
        public Story createFromParcel(Parcel source) {
            return new Story(source);
        }

        @Override
        public Story[] newArray(int size) {
            return new Story[size];
        }
    };
}
