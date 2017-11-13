package ca.uqac.sosdoit.data;

import android.content.Context;
import android.location.Geocoder;
import android.text.TextUtils;

import java.util.List;

/**
 * Data structure for user address
 */
public class Address
{
    private String houseNumber;
    private String street;
    private String additionalAddress;
    private String city;
    private String postalCode;
    private String country;

    private LatitudeLongitude latitudeLongitude;

    public Address() {}

    public Address(String street, String city, String postalCode, String country)
    {
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country.toUpperCase();
    }

    public Address(String houseNumber, String street, String city, String postalCode, String country)
    {
        this(street, city, postalCode, country);
        this.houseNumber = houseNumber;
    }

    public boolean hasHouseNumber()
    {
        return houseNumber != null;
    }

    public boolean hasAdditionalAddress()
    {
        return additionalAddress != null;
    }

    public boolean hasGeographicalCoordinates()
    {
        return latitudeLongitude != null;
    }

    /** Set the latitude and longitude information from the address.
     * Return true if the location was found, false otherwise
     */
    public boolean findGeographicalCoordinates(Context context)
    {
        Geocoder coder = new Geocoder(context);
        List<android.location.Address> address;

        try {
            address = coder.getFromLocationName(this.toString(), 5);
            if (address == null) {
                return false;
            }
            android.location.Address location = address.get(0);
            latitudeLongitude = new LatitudeLongitude(location.getLatitude(), location.getLongitude());
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    // ----- GETTER ----- //

    public String getHouseNumber()
    {
        return houseNumber;
    }

    public String getStreet()
    {
        return street;
    }

    public String getAdditionalAddress()
    {
        return additionalAddress;
    }

    public String getCity()
    {
        return city;
    }

    public String getPostalCode()
    {
        return postalCode;
    }

    public String getCountry()
    {
        return country;
    }

    public LatitudeLongitude getLatitudeLongitude() {
        return latitudeLongitude;
    }

    // ----- SETTER ----- //

    public Address setHouseNumber(String houseNumber)
    {
        this.houseNumber = houseNumber;
        return this;
    }

    public Address setStreet(String street)
    {
        this.street = street;
        return this;
    }

    public Address setAdditionalAddress(String additionalAddress)
    {
        this.additionalAddress = additionalAddress;
        return this;
    }

    public Address setAdditionalAddressWithCheck(String additionalAddress)
    {
        if (!TextUtils.isEmpty(additionalAddress)) {
            this.additionalAddress = additionalAddress;
        }
        return this;
    }

    public Address setCity(String city)
    {
        this.city = city;
        return this;
    }

    public Address setPostalCode(String postalCode)
    {
        this.postalCode = postalCode;
        return this;
    }

    public Address setCountry(String country)
    {
        this.country = country;
        return this;
    }

    public void setLatitudeLongitude(LatitudeLongitude latitudeLongitude) {
        this.latitudeLongitude = latitudeLongitude;
    }

    @Override
    public String toString() {
        return (hasHouseNumber() ? houseNumber + " " : "") + street + ", " + (hasAdditionalAddress() ? additionalAddress + ", " : "") + city + ", " + postalCode + " " + country;
    }
}
