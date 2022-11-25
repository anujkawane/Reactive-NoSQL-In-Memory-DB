package com.akawane0813.database;

import com.akawane0813.exception.IncompatibleTypeException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Array implements Serializable, IArray{

    List objectList;
    transient Gson gson;
    private String parent = "";

    public Array(){
        objectList = new ArrayList();
        gson = new Gson();
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public void setParentForNestedValue(String parent) {
        int index = 0;
        setParent(parent);
        objectList.forEach((v)->{
            if(v instanceof Array) {
                ((Array)v).setParentForNestedValue(parent+"."+"*index*" + index);
            } else if (v instanceof CustomObject) {
                ((CustomObject)v).setParentForNestedValue(parent+"."+"*index*"+ index
                );
            }
        });
    }

    public boolean put(Object object){
        if (object instanceof Array) {
            ((Array)object).setParentForNestedValue(getParent()+".*index*"+((Array) object).length());
        } else if (object instanceof CustomObject) {
            ((CustomObject)object).setParentForNestedValue(getParent()+".*index*"+((CustomObject)object).length());
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
        throw new IncompatibleTypeException("CustomObject at index "+index+" is not of type String");
    }



    public int getInt(int index) throws IncompatibleTypeException {
        if(index > objectList.size()){
            throw new IndexOutOfBoundsException();
        }
        if(objectList.get(index) instanceof Integer){
            return (int) objectList.get(index);
        }
        throw new IncompatibleTypeException("CustomObject at index "+index+" is not of type Integer");
    }

    public ICustomObject getObject(int index) throws IncompatibleTypeException {
        if(index > objectList.size()){
            throw new IndexOutOfBoundsException();
        }
        if(objectList.get(index) instanceof CustomObject){
            return (CustomObject) objectList.get(index);
        }
        throw new IncompatibleTypeException("CustomObject at index "+index+" is not of type CustomObject");
    }

    public IArray getArray(int index) throws IncompatibleTypeException {
        if(index > objectList.size()){
            throw new IndexOutOfBoundsException();
        }
        if(objectList.get(index) instanceof Array){
            return (Array) objectList.get(index);
        }
        throw new IncompatibleTypeException("CustomObject at index "+index+" is not of type Array");
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
        throw new IndexOutOfBoundsException();
    }

    public ArrayList<Object> convertToArrayList(Array array) {
        ArrayList newArrayList = new ArrayList<>();

        array.objectList.forEach(v -> {
            if (v instanceof Array) {
                ArrayList a = this.convertToArrayList((Array)v);
                newArrayList.add(a);
            }else if (v instanceof CustomObject) {
                HashMap a = new CustomObject().convertToHashMap((CustomObject) v);
                newArrayList.add(a);
            } else {
                newArrayList.add(v);
            }
        });
        return newArrayList;
    }

    @Override
    public String toString() {
        ArrayList arr = convertToArrayList(this);
        if(gson == null) gson = new Gson() ;
        return gson.toJson(arr);
    }


    public Array fromString(String value) {
        Array newArray = new Array();
        Type expectedType = new TypeToken<ArrayList<Object>>(){}.getType();
        ArrayList object = gson.fromJson(value, expectedType);
        object.forEach((v)
                -> {
            if (v instanceof ArrayList) {
                Array a = this.fromString(v.toString());
                newArray.put(a);
            }else if (v instanceof Map) {
                CustomObject db = new CustomObject();
                CustomObject a = db.fromString(v.toString());
                newArray.put(a);
            } else {
                newArray.put(v);
            }
        });
        return newArray;
    }
}
