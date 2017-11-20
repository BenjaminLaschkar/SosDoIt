package ca.uqac.sosdoit.data;

import com.google.android.gms.maps.model.LatLng;

/**
 * A class to store geographical coordinates
 */

public class Coordinates
{
    private double latitude;
    private double longitude;

    public Coordinates() {}

    public Coordinates(double latitude, double longitude)
    {
        if (-180.0D <= longitude && longitude < 180.0D) {
            this.longitude = longitude;
        } else {
            this.longitude = ((longitude - 180.0D) % 360.0D + 360.0D) % 360.0D - 180.0D;
        }

        this.latitude = Math.max(-90.0D, Math.min(90.0D, latitude));
    }

    public static LatLng convertToLatTng(Coordinates coordinates)
    {
        return new LatLng(coordinates.getLatitude(), coordinates.getLongitude());
    }

    public double getLatitude()
    {
        return latitude;
    }

    public void setLatitude(double latitude)
    {
        this.latitude = latitude;
    }

    public double getLongitude()
    {
        return longitude;
    }

    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }

    @Override
    public String toString()
    {
        return String.format("{ latitude: %s, longitude: %s }", latitude, longitude);
    }
}
