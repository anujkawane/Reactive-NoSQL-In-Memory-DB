package com.akawane0813.database;

import com.akawane0813.cursor.Cursor;
import com.akawane0813.cursor.CursorTracker;
import com.akawane0813.exception.KeyNotFoundException;
import com.akawane0813.exception.IncompatibleTypeException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class CustomObject implements Serializable, ICustomObject {

    Map<String, Object> map;
    private String parent;

    private final CursorTracker cursorTracker = CursorTracker.getInstance();


    public CustomObject(){
        map = new HashMap();
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

        Cursor cursor = cursorTracker.getCursor(getParent());
        if(cursor != null ) {
            cursor.updateObserver();
        }

        map.put(key,object);
        return true;
    }


    public Object remove(String key) throws KeyNotFoundException {
        if(map.containsKey(key)){
            return map.remove(key);
        }
        Cursor cursor = cursorTracker.getCursor(getParent());
        if(cursor != null ) {
            cursor.updateObserver();
        }
        throw new KeyNotFoundException("No data present in given key "+key);
    }

    public String getString(String key) throws IncompatibleTypeException {
        if(map.get(key) instanceof String){
            return (String) map.get(key);
        }

        throw new IncompatibleTypeException("Object at "+key+" is not of type String");
    }

    public int getInt(String key) throws IncompatibleTypeException {
        if(map.get(key) instanceof Integer){
            return (int) map.get(key);
        }
        throw new IncompatibleTypeException("Object at key "+key+" is not of type Integer");
    }

    public Double getDouble(String key) throws IncompatibleTypeException {
        if(map.get(key) instanceof Double){
            return (Double) map.get(key);
        }
        throw new IncompatibleTypeException("Object at key "+key+" is not of type Double");
    }

    public CustomObject getObject(String key) throws IncompatibleTypeException {
        if(map.get(key) instanceof CustomObject){
            return (CustomObject) map.get(key);
        }
        throw new IncompatibleTypeException("Object at key "+key+" is not of type CustomObject");
    }

    public Array getArray(String key) throws IncompatibleTypeException {
        if(map.get(key) instanceof Array){
            return (Array) map.get(key);
        }
        throw new IncompatibleTypeException("Object at key "+key+" is not of type Array");
    }

    /**
     * Returns parent key of the current object to handle nested values
     * @return
     */
    public String getParent() {
        return parent;
    }

    public java.lang.Object get(String key) throws KeyNotFoundException {
        if(!map.containsKey(key)){
            throw new KeyNotFoundException("No data present in given key "+key);
        }
        return map.get(key);
    }

    /**
     * Sets parent key of the current value to handle the nested values
     * @param parent key in the DB
     */
    public void setParent(String parent) {
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

    public Object clone() {
        return fromString(toString());
    }

    /**
     * Converts String format into CustomObject of Objects by parsing using Jackson JSON library
     * @param stringValue String format of Custom Objects
     * @return CustomObject by converting String to specific type of objects
     * @throws JsonProcessingException if any difficulty parsing String
     */
    public CustomObject fromString(String stringValue) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> parsedJson;
        try {
            parsedJson = mapper.readValue(stringValue, HashMap.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        CustomObject newCustomObject = new CustomObject();

        for (Map.Entry<String, Object> entry : parsedJson.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            try {
                if (value instanceof ArrayList) {
                    String json = mapper.writeValueAsString(value);
                    newCustomObject.put(key, new Array().fromString(json));
                } else if (value instanceof Map) {
                    String json = mapper.writeValueAsString(value);
                    newCustomObject.put(key, this.fromString(json));
                } else {
                    newCustomObject.put(key, value);
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }

        return newCustomObject;
    }

    /**
     * Converts the current Custom object into String format so that it can be parsable using fromString
     * @return String format of CustomObjets current object
     */
    public String toString() {
        StringBuilder builder = new StringBuilder();
        final AtomicInteger indexInteger = new AtomicInteger();
        builder.append("{");
        int index = 0;
        for (Map.Entry<String, Object> entry : map.entrySet()){
        String key = entry.getKey();
        Object value = entry.getValue();
            builder.append("\"" +key + "\":");
            if(value instanceof Array || value instanceof CustomObject){
                String valueString = value.toString();
                builder.append(valueString);
            }else if(value instanceof String){
                builder.append("\""+ value + "\"");
            }else{
                builder.append(value);
            }

            if(index < map.size() - 1)
                builder.append(",");
            index++;
        }

        builder.append("}");
        return builder.toString();

    }

    public int size(){
        return map.size();
    }

    public Set<String> keySet() {
        return new HashSet<>(map.keySet());
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        CustomObject that = (CustomObject) object;
        return map.equals(that.map);
    }

    @Override
    public int hashCode() {
        return Objects.hash(map);
    }
}
