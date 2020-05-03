package com.planner.removal.removalplanner.Model;

import android.util.Log;

import java.lang.reflect.Field;

public class Location {
    private String postal;
    private String place;
    private String street;
    private String houseNumber;

    public Location(String postal, String place, String street, String houseNumber) {
        this.postal = postal;
        this.place = place;
        this.street = street;
        this.houseNumber = houseNumber;
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
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
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
        hash = 3 * hash + (this.houseNumber == null ? 1 : this.houseNumber.hashCode());

        return hash;
    }
}
