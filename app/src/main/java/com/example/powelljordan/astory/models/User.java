package com.example.powelljordan.astory.models;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by jorda on 12/22/2017.
 */

public class User implements Serializable, Parcelable {
    public String id;
    public String username;
    public HashMap<String, Boolean> stories;
    public HashMap<String, Object> notifications;
    public Audience audience;
    public String profilePictureURL;

    public User(){}

    public User(String id, String username, Uri profilePictureURL){
        this.id = id;
        this.username = username;
        this.profilePictureURL = profilePictureURL.toString();
        this.stories = new HashMap<>();
        this.notifications = new HashMap<>();
        this.audience = new Audience(true);
    }

    public String toString(){
        HashMap<String, Object> userObj = new HashMap<>();
        userObj.put("id", this.id);
        userObj.put("username", this.username);
        userObj.put("profilePictureURL", this.profilePictureURL);
        userObj.put("stories", this.stories);
        userObj.put("notifications", this.notifications);
        userObj.put("audience", this.audience.toString());
        return userObj.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.username);
        dest.writeSerializable(this.stories);
        dest.writeSerializable(this.notifications);
        dest.writeParcelable(this.audience, flags);
        dest.writeString(this.profilePictureURL);
    }

    protected User(Parcel in) {
        this.id = in.readString();
        this.username = in.readString();
        this.stories = (HashMap<String, Boolean>) in.readSerializable();
        this.notifications = (HashMap<String, Object>) in.readSerializable();
        this.audience = in.readParcelable(Audience.class.getClassLoader());
        this.profilePictureURL = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
