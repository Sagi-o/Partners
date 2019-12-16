package com.app.partners.models;

public class Apartment {
    public String name;
    public String address;
    public String id;
    public String landlordId;

    public Apartment() {}

    public Apartment(String name, String address) {
        this.name = name;
        this.address = address;
    }
}
