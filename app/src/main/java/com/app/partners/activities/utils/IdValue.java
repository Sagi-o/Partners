package com.app.partners.activities.utils;

import android.util.Pair;

import java.util.Map;

public class IdValue implements Map.Entry<String, Integer> {

    String partnerId;
    int value;

    public IdValue(String uid, int value){
        this.partnerId = uid;
        this.value = value;
    }
    @Override
    public String getKey() {
        return partnerId;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public Integer setValue(Integer integer) {
        this.value = integer;
        return value;
    }
}
