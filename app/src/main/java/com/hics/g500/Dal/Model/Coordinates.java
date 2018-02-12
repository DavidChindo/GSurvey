package com.hics.g500.Dal.Model;

/**
 * Created by david.barrera on 2/11/18.
 */

public class Coordinates {

    private long latitude;
    private long longitude;

    public Coordinates(long latitude, long longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public long getLatitude() {
        return latitude;
    }

    public void setLatitude(long latitude) {
        this.latitude = latitude;
    }

    public long getLongitude() {
        return longitude;
    }

    public void setLongitude(long longitude) {
        this.longitude = longitude;
    }
}
