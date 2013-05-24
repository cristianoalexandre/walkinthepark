package sdis.walkinthepark.types;

import android.location.Location;

public class NavPoint {
    private double longitude;
    private double latitude;
    private double altitude;
    private double instantSpeed;

    public NavPoint(Location location) {
        longitude = location.getLongitude();
        latitude = location.getLatitude();
        altitude = location.getAltitude();
        instantSpeed = location.getSpeed();
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public double getInstantSpeed() {
        return instantSpeed;
    }

    @Override
    public String toString() {
        return "Latitude: "+latitude+"\nLongitude: "+longitude+"\nAltitude: "+altitude+"\nSpeed: "+instantSpeed;
    }
}