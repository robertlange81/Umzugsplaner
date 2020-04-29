package com.planner.removal.removalplanner.Model;

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

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }
}
