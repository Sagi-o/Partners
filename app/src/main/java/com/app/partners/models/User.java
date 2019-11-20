package com.app.partners.models;

public class User {

    private String first_name;
    private String last_name;
    private String email;
    private String phone;
//    private String password;
    private boolean isLandLord;
    private boolean isSettle;


    public String getFirst_name() {
        return first_name;
    }
    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }
    public String getLast_name() { return last_name; }
    public void setLast_name(String last_name) { this.last_name = last_name; }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
//    public String getPassword() {
//        return password;
//    }
//    public void setPassword(String password) { this.password = password; }
    public boolean isLandLord() { return isLandLord; }
    public void setLandLord(boolean landLord){ this.isLandLord = landLord; }
    public boolean isSettle() { return isSettle; }
    public void setSettle(boolean settle) { isSettle = settle; }

    public User(String first_name, String last_name, String email, String phone, boolean isLandLord){
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.phone = phone;
//        this.password = password;
        this.isLandLord = isLandLord;
    }
}


