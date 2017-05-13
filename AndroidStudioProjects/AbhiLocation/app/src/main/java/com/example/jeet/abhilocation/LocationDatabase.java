package com.example.jeet.abhilocation;

/**
 * Created by jeet on 27/10/16.
 */
public class LocationDatabase {
    private String address;
    private String city;
    private String country;

    public LocationDatabase() {

    }
    public String getaddress() {
        return this.address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getCity() {
        return  this.city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getCountry() {
        return this.country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
}
