package com.easyinsight.datafeeds.batchbook2;

/**
 * User: jamesboe
 * Date: 7/31/12
 * Time: 4:28 PM
 */
public class Address {
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private String label;

    public Address(String address1, String address2, String city, String state, String postalCode, String country, String label) {
        this.address1 = address1;
        this.address2 = address2;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.country = country;
        this.label = label;
    }

    public String getAddress1() {
        return address1;
    }

    public String getAddress2() {
        return address2;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCountry() {
        return country;
    }

    public String getLabel() {
        return label;
    }
}
