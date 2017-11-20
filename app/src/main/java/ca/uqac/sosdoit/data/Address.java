package ca.uqac.sosdoit.data;

import android.content.Context;
import android.location.Geocoder;
import android.text.TextUtils;

import com.google.firebase.database.Exclude;

import java.util.List;

import ca.uqac.sosdoit.util.Util;

/**
 * Data structure for user address
 */

public class Address
{
    private String houseNumber;
    private String street;
    private String additionalAddress;
    private String city;
    private String state;
    private String postalCode;
    private String country;

    private Coordinates coordinates;

    public Address() {}

    public Address(Address other)
    {
        this.houseNumber = other.houseNumber;
        this.street = other.street;
        this.additionalAddress = other.additionalAddress;
        this.city = other.city;
        this.state = other.state;
        this.postalCode = other.postalCode;
        this.country = other.country;
        this.coordinates = other.coordinates;
    }

    public Address(String street, String city, String postalCode, String country)
    {
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
    }

    public boolean hasHouseNumber()
    {
        return houseNumber != null;
    }

    public String getHouseNumber()
    {
        return houseNumber;
    }

    public Address setHouseNumber(String houseNumber)
    {
        this.houseNumber = houseNumber;
        return this;
    }

    public Address setHouseNumberWithCheck(String houseNumber)
    {
        this.houseNumber = TextUtils.isEmpty(houseNumber) ? null : houseNumber;
        return this;
    }

    public boolean hasStreet()
    {
        return street != null;
    }

    public String getStreet()
    {
        return street;
    }

    public Address setStreet(String street)
    {
        this.street = street;
        return this;
    }

    public Address setStreetWithCheck(String street)
    {
        this.street = TextUtils.isEmpty(street) ? null : street;
        return this;
    }

    public boolean hasAdditionalAddress()
    {
        return additionalAddress != null;
    }

    public String getAdditionalAddress()
    {
        return additionalAddress;
    }

    public Address setAdditionalAddress(String additionalAddress)
    {
        this.additionalAddress = additionalAddress;
        return this;
    }

    public Address setAdditionalAddressWithCheck(String additionalAddress)
    {
        this.additionalAddress = TextUtils.isEmpty(additionalAddress) ? null : additionalAddress;
        return this;
    }

    public boolean hasCity()
    {
        return city != null;
    }

    public String getCity()
    {
        return city;
    }

    public Address setCity(String city)
    {
        this.city = city;
        return this;
    }

    public Address setCityWithCheck(String city)
    {
        this.city = TextUtils.isEmpty(city) ? null : city;
        return this;
    }

    public boolean hasState()
    {
        return state != null;
    }

    public String getState()
    {
        return state;
    }

    public Address setState(String state)
    {
        this.state = state;
        return this;
    }

    public Address setStateWithCheck(String state)
    {
        this.state = TextUtils.isEmpty(state) ? null : state;
        return this;
    }

    public boolean hasPostalCode()
    {
        return postalCode != null;
    }

    public String getPostalCode()
    {
        return postalCode;
    }

    public Address setPostalCode(String postalCode)
    {
        this.postalCode = postalCode;
        return this;
    }

    public Address setPostalCodeWithCheck(String postalCode)
    {
        this.postalCode = TextUtils.isEmpty(postalCode) ? null : postalCode;
        return this;
    }

    public boolean hasCountry()
    {
        return country != null;
    }

    public String getCountry()
    {
        return country;
    }

    public Address setCountry(String country)
    {
        this.country = country;
        return this;
    }

    public Address setCountryWithCheck(String country)
    {
        this.country = TextUtils.isEmpty(country) ? null : country;
        return this;
    }

    public boolean hasCoordinates()
    {
        return coordinates != null;
    }

    public Coordinates getCoordinates()
    {
        return coordinates;
    }

    /**
     * Set the latitude and longitude information from the address.
     * Return true if the location was found, false otherwise
     */
    public boolean findCoordinates(Context context)
    {
        Geocoder coder = new Geocoder(context);
        List<android.location.Address> address;

        try {
            address = coder.getFromLocationName(this.toString(), 5);
            if (address == null) {
                return false;
            }
            android.location.Address location = address.get(0);
            coordinates = new Coordinates(location.getLatitude(), location.getLongitude());
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    @Exclude
    public boolean isEmpty()
    {
        return !hasHouseNumber() && !hasStreet() && !hasAdditionalAddress() && !hasCity() && !hasState() && !hasPostalCode() && !hasCountry();
    }

    public boolean equals(Address other)
    {
        return Util.equals(houseNumber, other.houseNumber) && street.equals(other.street) && Util.equals(additionalAddress, other.additionalAddress) && city.equals(other.city) && Util.equals(state, other.state) && postalCode.equals(other.postalCode) && country.equals(other.country);
    }

    @Override
    public String toString()
    {
        return String.format(
                "{%n    number: %s,%n    street: %s,%n    additional address: %s,%n    city: %s,%n    state: %s,%n    postal code: %s,%n    country: %s,%n    coordinates: %S%n}",
                hasHouseNumber() ? houseNumber : "",
                street,
                hasAdditionalAddress() ? additionalAddress : "",
                city,
                hasState() ? state : "",
                postalCode,
                country,
                hasCoordinates() ? coordinates.toString() : ""
        );
    }
}
