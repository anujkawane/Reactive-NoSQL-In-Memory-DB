package com.akawane0813.database;

import com.akawane0813.exception.KeyNotFoundException;
import com.akawane0813.exception.IncompatibleTypeException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomObject implements Serializable, ICustomObject {

    Map map;
    transient Gson gson;
    private String parent;

    public CustomObject(){
        map = new HashMap();
        gson = new Gson();
    }

    public boolean put(String key, Object object) throws KeyNotFoundException {

        if (map.containsKey(key)) {
            throw new KeyNotFoundException("Key already present");
        }

        if (object instanceof Array) {
            ((Array)object).setParent(parent);
        } else if (object instanceof CustomObject) {
            ((CustomObject)object).setParent(parent);
        }

        map.put(key,object);
        return true;
    }


    public Object remove(String key) throws KeyNotFoundException {
        if(map.containsKey(key)){
            return map.remove(key);
        }
        throw new KeyNotFoundException("No data present in given key "+key);
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

    public String getParent() {
        return parent;
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

    public void setParent(String parent) {
        int index = 0;
        this.parent = parent;
        map.forEach((k,v)->{
            if(v instanceof Array) {
                ((Array)v).setParent(parent);
            } else if (v instanceof CustomObject) {
                ((CustomObject)v).setParent(parent
                );
            }
        });
    }

    public HashMap<String,Object> convertToHashMap(CustomObject object) {
        HashMap<String,Object> map = new HashMap<>();

        object.map.forEach((k,v) -> {
            if (v instanceof Array) {
                Array arr = new Array();
                ArrayList<Object> a = arr.convertToArrayList((Array)v);
                map.put((String) k,a);
            }else if (v instanceof CustomObject) {
                HashMap<String, Object> a = convertToHashMap((CustomObject) v);
                map.put((String) k,a);
            } else {
                map.put((String) k,v);
            }
        });
        return map;
    }

    public String toString(){
        HashMap<String, Object> map = convertToHashMap(this);
        if(gson == null) gson = new Gson();
        return gson.toJson(map);
    }

    public Object clone() throws CloneNotSupportedException
    {
        return fromString(toString());
    }

    public CustomObject fromString(String value) {
        Type userListType = new TypeToken<HashMap<String,Object>>(){}.getType();
        HashMap object = gson.fromJson(value, userListType);
        CustomObject newCustomObject = new CustomObject();
        this.map = object;

        object.forEach((k, v)
                -> {
            if (v instanceof ArrayList) {
                Array array = new Array().fromString(v.toString());
                try {
                    newCustomObject.put(k.toString(),array);
                } catch (KeyNotFoundException e) {
                    e.printStackTrace();
                }
            }else if (v instanceof Map) {
                CustomObject customObject = fromString(v.toString());
                try {
                    newCustomObject.put(k.toString(),customObject);
                } catch (KeyNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    newCustomObject.put(k.toString(),v);
                } catch (KeyNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        return newCustomObject;
    }

    public List<String> keys() {
        return new ArrayList<>(map.keySet());

    }
}
