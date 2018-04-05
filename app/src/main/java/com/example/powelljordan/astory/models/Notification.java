package com.example.powelljordan.astory.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

/**
 * Created by jorda on 12/28/2017.
 */

public class Notification implements Parcelable {
    public HashMap<String, String> data;
    public String type;

    public Notification(String type){
        this.type = type;
    }

    public HashMap<String, Object> toObject(){
        HashMap<String, Object> notificationObj = new HashMap<>();
        notificationObj.put(this.type, this.data);
        notificationObj.put("type", type);
        return notificationObj;
    }

    public String toString(){
        return this.toObject().toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this.data);
        dest.writeString(this.type);
    }

    protected Notification(Parcel in) {
        this.data = (HashMap<String, String>) in.readSerializable();
        this.type = in.readString();
    }

    public static final Parcelable.Creator<Notification> CREATOR = new Parcelable.Creator<Notification>() {
        @Override
        public Notification createFromParcel(Parcel source) {
            return new Notification(source);
        }

        @Override
        public Notification[] newArray(int size) {
            return new Notification[size];
        }
    };
}
