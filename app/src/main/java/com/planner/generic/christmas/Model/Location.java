package com.planner.generic.christmas.Model;

import java.io.Serializable;

public class Location implements Serializable {
    private String postal;
    private String place;
    private String street;
    private String streetNumber;

    public static final String POSTAL = "target_location_postal";
    public static final String PLACE = "target_location_place";
    public static final String STREET = "target_location_street";
    public static final String STREETNUMBER = "target_location_street_number";

    public Location(String postal, String place, String street, String streetNumber) {
        this.postal = postal;
        this.place = place;
        this.street = street;
        this.streetNumber = streetNumber;
    }

    public String getPostal() {
        return postal;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public boolean equals(Object obj) {

        if(obj==null) return false;
        if (!(obj instanceof Task))
            return false;
        if (obj == this)
            return true;

        if(obj.hashCode() == this.hashCode())
            return true;

        return false;
    }

    public int hashCode(){
        int hash = 7;
        hash = 3 * hash + (this.postal == null ? 1 : this.postal.hashCode());
        hash = 3 * hash + (this.place == null ? 1 : this.place.hashCode());
        hash = 3 * hash + (this.street == null ? 1 : this.street.hashCode());
        hash = 3 * hash + (this.streetNumber == null ? 1 : this.streetNumber.hashCode());

        return hash;
    }
}
