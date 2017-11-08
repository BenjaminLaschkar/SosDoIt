package ca.uqac.sosdoit.data;

/**
 * Data structure for an address
 */

public class Address {

    private String houseNumber;
    private String street;
    private String city;
    private String zip;
    private String country;

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


    @Override
    public String toString() {
        return (houseNumber != null ? houseNumber + " " : "") + street + ", " + city + (zip != null ? "," + zip : "") + " " + country;
    }
}
