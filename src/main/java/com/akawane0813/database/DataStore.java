package com.akawane0813.database;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class DataStore implements Serializable {
    private Map<String, Object> database;

    public DataStore(){
        database = new HashMap<>();
    }

    public boolean put(String key, Object value){
        database.put(key, value);
        return true;
    }

    public boolean containsKey(String key){
        return database.containsKey(key);
    }

    @Override
    public String toString() {
        return "Database"
                + database +
                "";
    }

    public Object remove(String key){
        return database.remove(key);
    }

    public Object get(String key){
        return database.get(key);
    }
}
