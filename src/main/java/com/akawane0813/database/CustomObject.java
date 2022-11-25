package com.akawane0813.database;

import com.akawane0813.exception.KeyNotFoundException;
import com.akawane0813.exception.IncompatibleTypeException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class CustomObject implements Serializable {

    Map map;
    transient Gson gson;
    public CustomObject(){
        map = new HashMap();
        gson = new Gson();
    }

    public boolean put(String key, java.lang.Object object){
       map.putIfAbsent(key, object);
        return true;
    }

    public String getString(String key) throws IncompatibleTypeException {
        if(map.get(key) instanceof String){
            return (String) map.get(key);
        }
        throw new IncompatibleTypeException("CustomObject at "+key+" is not of type String");
    }

    public int getInt(String key) throws IncompatibleTypeException {
        if(map.get(key) instanceof Integer){
            return (int) map.get(key);
        }
        throw new IncompatibleTypeException("CustomObject at key "+key+" is not of type Integer");
    }

    public CustomObject getObject(String key) throws IncompatibleTypeException {
        if(map.get(key) instanceof CustomObject){
            return (CustomObject) map.get(key);
        }
        throw new IncompatibleTypeException("CustomObject at key "+key+" is not of type CustomObject");
    }

    public Array getArray(String key) throws IncompatibleTypeException {
        if(map.get(key) instanceof Array){
            return (Array) map.get(key);
        }
        throw new IncompatibleTypeException("CustomObject at key "+key+" is not of type Array");
    }

    public java.lang.Object get(String key) throws KeyNotFoundException {
        if(!map.containsKey(key)){
            throw new KeyNotFoundException("No data present in given key "+key);
        }
        return map.get(key);
    }

    public int length(){
        return map.size();
    }

    public CustomObject fromString(String value) {
        Gson gson = new Gson();
        Type userListType = new TypeToken<HashMap<String, java.lang.Object>>(){}.getType();
        HashMap object = (HashMap)gson.fromJson(value, userListType);
        CustomObject newDBCustomObject = new CustomObject();
        this.map = object;
        object.forEach((k, v)
                -> {
            System.out.println(k);
            System.out.println(v instanceof Map);
            if (v instanceof ArrayList) {
                Array ab = new Array();
                Array a = ab.fromString(v.toString());
                newDBCustomObject.put(k.toString(),a);
                System.out.println(a);
            }else if (v instanceof Map) {
                CustomObject a = fromString(v.toString());
                newDBCustomObject.put(k.toString(),a);
                System.out.println(a);
            } else {
                newDBCustomObject.put(k.toString(),v);
            }
        });
        return newDBCustomObject;
    }

    @Override
    public String toString() {
        return  (String) map.keySet().stream()
                .map(key -> key + "=" + map.get(key))
                .collect(Collectors.joining(", ", "{", "}"));
    }
}
