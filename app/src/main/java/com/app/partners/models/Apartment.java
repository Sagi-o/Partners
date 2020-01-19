package com.app.partners.models;

import java.util.HashMap;

public class Apartment {
    public String name;
    public String address;
    public String id;
    public String landlordId;
//    public HashMap<String, Integer> list = new HashMap<>();
    public int sumOfShopping;

    public Apartment() {}

    public Apartment(String name, String address) {
        this.name = name;
        this.address = address;
        sumOfShopping = 0;
    }

}
