package com.akawane0813.database;

import com.akawane0813.exception.IncompatibleTypeException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Array implements Serializable{

    List objectList;
    transient Gson gson;

    public Array(){
        objectList = new ArrayList();
        gson = new Gson();
    }

    public boolean put(java.lang.Object object){
        objectList.add(object);
        return true;
    }

    public String getString(int index) throws Exception {
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

    public Object getObject(int index) throws IncompatibleTypeException {
        if(index > objectList.size()){
            throw new IndexOutOfBoundsException();
        }
        if(objectList.get(index) instanceof Object){
            return (Object) objectList.get(index);
        }
        throw new IncompatibleTypeException("Object at index "+index+" is not of type Object");
    }

    public Array getArray(int index) throws Exception {
        if(index > objectList.size()){
            throw new IndexOutOfBoundsException();
        }
        if(objectList.get(index) instanceof Array){
            return (Array) objectList.get(index);
        }
        throw new IncompatibleTypeException("Object at index "+index+" is not of type Array");
    }

    public java.lang.Object get(int index){
        if(index > objectList.size()){
            throw new IndexOutOfBoundsException();
        }
        return objectList.get(index);
    }

    public int length(){
        return objectList.size();
    }
    public java.lang.Object remove(int index) {
        if(index < objectList.size()) {
            return objectList.remove(index);
        }
        throw new IndexOutOfBoundsException();
    }

    public Array fromString(String value) {
        Array newArray = new Array();
        Type expectedType = new TypeToken<ArrayList<java.lang.Object>>(){}.getType();
        ArrayList object = gson.fromJson(value, expectedType);

        object.forEach((v)
                -> {
            if (v instanceof ArrayList) {
                Array a = this.fromString(v.toString());
                newArray.put(a);
            }else if (v instanceof Object) {
                Object db = new Object();
                Object a = db.fromString(v.toString());
                newArray.put(a);
            } else {
                newArray.put(v);
            }
        });
        return this;
    }

    @Override
    public String toString() {
        return "" +
                  objectList +
                "";
    }
}
