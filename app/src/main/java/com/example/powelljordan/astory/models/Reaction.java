package com.example.powelljordan.astory.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.List;

/**
 * Created by jorda on 12/26/2017.
 */

public class Reaction implements Interaction, Parcelable {
    String author;
    HashMap<String, String> viewers;
    HashMap<String, String> topic;
    String content;
    String summary;

    public Reaction(){}

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

    protected Reaction(Parcel in) {
        this.author = in.readString();
        this.viewers = (HashMap<String, String>) in.readSerializable();
        this.topic = (HashMap<String, String>) in.readSerializable();
        this.content = in.readString();
        this.summary = in.readString();
    }

    public static final Parcelable.Creator<Reaction> CREATOR = new Parcelable.Creator<Reaction>() {
        @Override
        public Reaction createFromParcel(Parcel source) {
            return new Reaction(source);
        }

        @Override
        public Reaction[] newArray(int size) {
            return new Reaction[size];
        }
    };
}
