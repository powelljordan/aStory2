package com.example.powelljordan.astory.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.List;

/**
 * Created by jorda on 12/26/2017.
 */

public class Comment implements Interaction, Parcelable {
    String author;
    HashMap<String, String> viewers;
    HashMap<String, String> topic;
    String content;
    String summary;

    public Comment(){}

    @Override
    public String getInitiator() {
        return null;
    }

    @Override
    public List getResponders() {
        return null;
    }

    @Override
    public HashMap<String, String> getTopic() {
        return null;
    }

    @Override
    public Content getContent() {
        return null;
    }

    @Override
    public String getSummary() {
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.author);
        dest.writeSerializable(this.viewers);
        dest.writeSerializable(this.topic);
        dest.writeString(this.content);
        dest.writeString(this.summary);
    }

    protected Comment(Parcel in) {
        this.author = in.readString();
        this.viewers = (HashMap<String, String>) in.readSerializable();
        this.topic = (HashMap<String, String>) in.readSerializable();
        this.content = in.readString();
        this.summary = in.readString();
    }

    public static final Parcelable.Creator<Comment> CREATOR = new Parcelable.Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel source) {
            return new Comment(source);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };
}
