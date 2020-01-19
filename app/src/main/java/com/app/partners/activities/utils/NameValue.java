package com.app.partners.activities.utils;

import android.util.Pair;

import java.util.Map;

public class NameValue {

    public String partnerName;
    public int value;

    public NameValue() {}

    public NameValue(String uid, int value){
        this.partnerName = uid;
        this.value = value;
    }
}
