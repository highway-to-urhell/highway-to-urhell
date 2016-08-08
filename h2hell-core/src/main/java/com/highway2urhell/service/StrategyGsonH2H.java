package com.highway2urhell.service;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class StrategyGsonH2H implements ExclusionStrategy {

    public boolean shouldSkipField(FieldAttributes f) {
        return checkFieldJDK(f);
    }

    public boolean shouldSkipClass(Class<?> clazz) {
        return false;
    }

    public Boolean checkFieldJDK(FieldAttributes f) {
        if(f.getDeclaredClass().getName().startsWith("java.io")
            || f.getDeclaredClass().getName().startsWith("javax.servlet")){
            return true;
        }
        return false;

    }
}
