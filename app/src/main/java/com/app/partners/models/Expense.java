package com.app.partners.models;

public class Expense {
    public String description;
    public int value;
    public String payerId;
    public String imageUrl;
    public long timestamp;
    public String payerName;

    public Expense() {}

    public Expense(String description, int value, String payerId, String imageUrl, long timestamp, String payerName) {
        this.description = description;
        this.value = value;
        this.payerId = payerId;
        this.imageUrl = imageUrl;
        this.timestamp = timestamp;
        this.payerName = payerName;
    }
}
