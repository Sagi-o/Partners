package com.app.partners.models;

public class User {

    public String firstName;
    public String lastName;
    public String email;
    public String phone;
    public boolean isLandLord;
    public int sumOfShopping = 0;
    public int debtToApartment = 0;

    public User() { }

    public User(String firstName, String lastName, String email, String phone, boolean isLandLord){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.isLandLord = isLandLord;
    }
}


