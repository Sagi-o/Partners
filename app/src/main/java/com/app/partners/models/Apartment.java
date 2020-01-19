package com.app.partners.models;

import java.util.HashMap;

public class Apartment {
    public String name;
    public String address;
    public String id;
    public String landlord;
    public String creatorPhoneNumber;
    public String contactName;

    public Apartment() {}

    public Apartment(String name, String address, String creatorPhoneNumber, String contactName) {
        this.name = name;
        this.address = address;
        this.creatorPhoneNumber = creatorPhoneNumber;
        this.contactName = contactName;
    }

}
