package com.example.powelljordan.astory.events;

import android.location.Location;

import com.example.powelljordan.astory.models.Story;
import com.example.powelljordan.astory.models.User;

/**
 * Created by jorda on 12/26/2017.
 */

public class TransitionEvent {
    public int transitionType;
    public String id;
    public Story story;
    public User user;
    public Location location;

    public TransitionEvent(int type){
        this.transitionType = type;
    }

    public TransitionEvent(int type, String id){
        this.transitionType = type;
        this.id = id;
    }

    public TransitionEvent(int type, Story story){
        this.transitionType = type;
        this.story = story;
    }

    public TransitionEvent(int type, User user){
        this.transitionType = type;
        this.user = user;
    }

    public TransitionEvent(int type, Story story, User user) {
        this.transitionType = type;
        this.story = story;
        this.user = user;
    }

    public TransitionEvent(int type, Story story, User user, Location location) {
        this.transitionType = type;
        this.story = story;
        this.user = user;
        this.location = location;
    }
}
