package ca.uqac.sosdoit.data;

/**
 * Data structure for an address
 */

public class Address {

    private static String SEP = "@@";

    private String houseNumber;
    private String street;
    private String city;
    private String zip;
    private String state;

    public Address(String houseNumber, String street, String city, String zip, String state) {
        this.houseNumber = houseNumber;
        this.street = street;
        this.city = city;
        this.zip = zip;
        this.state = state;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


    @Override
    public String toString() {
        return houseNumber + SEP + street + SEP + city + SEP + zip + SEP + state;
    }

    /** Create an address from a parsed String
     *
     */
    public static Address createAdressFromString(String address) {
        String[] addressAtt = address.split(SEP);
        return new Address(
                addressAtt[0],
                addressAtt[1],
                addressAtt[2],
                addressAtt[3],
                addressAtt[4]
        );
    }
}
