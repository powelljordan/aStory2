package com.example.powelljordan.astory.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

/**
 * Created by jorda on 12/23/2017.
 */

public class Audience implements Parcelable {
    public Boolean isPublic;
    public HashMap<String, Boolean> members;

    public Audience(){}

    public Audience(Boolean isPublic){
        this.isPublic = isPublic;
        this.members = new HashMap<>();
    }
    public Audience(HashMap<String, Boolean> members){
        this.isPublic = false;
        this.members =  members;
    }

    public void addMember(String userId){
        this.members.put(userId, true);
    }

    public void removeMember(String userId){
        this.members.remove(userId);
    }

    public void setIsPublic(Boolean newValue){
        this.isPublic = newValue;
    }


    public String toString(){
        HashMap<String, Object> audienceObj = new HashMap<>();
        audienceObj.put("isPublic", this.isPublic);
        audienceObj.put("members", this.members);
        return audienceObj.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.isPublic);
        dest.writeSerializable(this.members);
    }

    protected Audience(Parcel in) {
        this.isPublic = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.members = (HashMap<String, Boolean>) in.readSerializable();
    }

    public static final Parcelable.Creator<Audience> CREATOR = new Parcelable.Creator<Audience>() {
        @Override
        public Audience createFromParcel(Parcel source) {
            return new Audience(source);
        }

        @Override
        public Audience[] newArray(int size) {
            return new Audience[size];
        }
    };
}
