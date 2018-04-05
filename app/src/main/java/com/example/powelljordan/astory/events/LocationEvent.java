package com.example.powelljordan.astory.events;

import android.location.Location;

/**
 * Created by jorda on 12/28/2017.
 */

public class LocationEvent {
    public Location location;
    public LocationEvent(Location location){
        this.location = location;
    }
}
