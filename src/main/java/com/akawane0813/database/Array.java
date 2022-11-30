package com.akawane0813.database;

import com.akawane0813.cursor.Cursor;
import com.akawane0813.cursor.CursorTracker;
import com.akawane0813.exception.IncompatibleTypeException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.Serializable;
import java.util.*;

public class Array implements Serializable, IArray{

    List objectList;

    private String parent = "";

    private final CursorTracker cursorTracker = CursorTracker.getInstance();


    public Array(){
        objectList = new ArrayList();
    }

    public void setParent(String parent) {
        this.parent = parent;
        objectList.forEach((v)->{
            if(v instanceof Array) {
                ((Array)v).setParent(parent);
            } else if (v instanceof CustomObject) {
                ((CustomObject)v).setParent(parent
                );
            }
        });
    }

    public boolean put(Object object){
        if (object instanceof Array) {
            ((Array)object).setParent(parent);
        } else if (object instanceof CustomObject) {
            ((CustomObject)object).setParent(parent);
        }
        Cursor cursor = cursorTracker.getCursor(getParent());
        if(cursor != null ) {
            cursor.updateObserver();
        }
        objectList.add(object);
        return true;
    }

    public String getParent() {
        return parent;
    }


    public String getString(int index) throws IncompatibleTypeException {
        if(index > objectList.size()){
            throw new IndexOutOfBoundsException();
        }
        if(objectList.get(index) instanceof String){
            return (String) objectList.get(index);
        }
        throw new IncompatibleTypeException("Object at index "+index+" is not of type String");
    }



    public int getInt(int index) throws IncompatibleTypeException {
        if(index > objectList.size()){
            throw new IndexOutOfBoundsException();
        }
        if(objectList.get(index) instanceof Integer){
            return (int) objectList.get(index);
        }
        throw new IncompatibleTypeException("Object at index "+index+" is not of type Integer");
    }

    public Double getDouble(int index) throws IncompatibleTypeException {
        if(index > objectList.size()){
            throw new IndexOutOfBoundsException();
        }
        if(objectList.get(index) instanceof Double){
            return (Double) objectList.get(index);
        }
        throw new IncompatibleTypeException("Object at index "+index+" is not of type Double");
    }

    public ICustomObject getObject(int index) throws IncompatibleTypeException {
        if(index > objectList.size()){
            throw new IndexOutOfBoundsException();
        }
        if(objectList.get(index) instanceof CustomObject){
            return (CustomObject) objectList.get(index);
        }
        throw new IncompatibleTypeException("Object at index "+index+" is not of type CustomObject");
    }

    public IArray getArray(int index) throws IncompatibleTypeException {
        if(index > objectList.size()){
            throw new IndexOutOfBoundsException();
        }
        if(objectList.get(index) instanceof Array){
            return (Array) objectList.get(index);
        }
        throw new IncompatibleTypeException("Object at index "+index+" is not of type Array");
    }

    public Object get(int index){
        if(index > objectList.size()){
            throw new IndexOutOfBoundsException();
        }
        return objectList.get(index);
    }

    public int length(){
        return objectList.size();
    }

    public Object remove(int index) {
        if(index < objectList.size()) {
            return objectList.remove(index);
        }
        Cursor cursor = cursorTracker.getCursor(getParent());
        if(cursor != null ) {
            cursor.updateObserver();
        }
        throw new IndexOutOfBoundsException();
    }

    /**
     * Converts the current Array object into String format
     * @return String format of Array current object
     */
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");

        for(int i = 0; i < objectList.size(); i++){
            Object value = objectList.get(i);
            if(value instanceof Array || value instanceof CustomObject){
                String valueString = value.toString();
                builder.append(valueString);
            } else if(value instanceof String){
                builder.append("\""+ value + "\"");
            }else{
                builder.append(value);
            }
            if(i < this.length() - 1){
                builder.append(",");
            }
        }
        builder.append("]");
        return builder.toString();
    }

    public Object clone()
    {
        try {
            return fromString(toString());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Converts String format into Array of Objects by parsing using Jackson JSON library
     * @param value String format of Array Objects
     * @return Array Object by converting String to specific type of objects
     * @throws JsonProcessingException if any difficulty parsing String
     */
    public Array fromString(String value) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<Object> list = mapper.readValue(value, ArrayList.class);
        Array arrayObjects = new Array();

        for (Object obj : list) {
            if(obj instanceof ArrayList){
                String jsonArrString = mapper.writeValueAsString(obj);
                Array currArrayObj = this.fromString(jsonArrString);
                arrayObjects.put(currArrayObj);
            } else if (obj instanceof HashMap) {
                String jsonObjString = mapper.writeValueAsString(obj);
                arrayObjects.put(new CustomObject().fromString(jsonObjString));
            }else{
                arrayObjects.put(obj);
            }
        }
        return arrayObjects;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Array array = (Array) object;
        return objectList.equals(array.objectList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(objectList);
    }
}
