package ca.uqac.sosdoit.data;


import android.content.Context;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Data structure for an address
 */

public class Address {

    private String houseNumber;
    private String street;
    private String city;
    private String zip;
    private String country;
    private LatLng latLng;

    public Address() {}

    public Address(String houseNumber, String street, String city, String zip, String country) {

        this.houseNumber = houseNumber;
        this.street = street;
        this.city = city;
        this.zip = zip;
        this.country = country;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    /** Set the latitude and longitude information from the address.
     * Return true if the location was found, false otherwise
     */
    public boolean setLatitudeLongitude(Context context) {
        Geocoder coder = new Geocoder(context);
        List<android.location.Address> address;

        try {
            address = coder.getFromLocationName(this.toString(),5);
            if (address == null) {
                return false;
            }
            android.location.Address location=address.get(0);
            latLng = new LatLng(location.getLatitude(), location.getLongitude());
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String toString() {
        return (houseNumber != null ? houseNumber + " " : "") + street + ", " + city + (zip != null ? ", " + zip : "") + " " + country;
    }
}
