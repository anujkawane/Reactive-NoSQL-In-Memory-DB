package com.akawane0813.database;

import com.akawane0813.exception.IncompatibleTypeException;
import com.akawane0813.exception.KeyNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.*;

public class Array implements Serializable, IArray{

    List objectList;

    transient Gson gson;

    private String parent = "";

    public Array(){
        objectList = new ArrayList();
        gson = new Gson();
    }

    public void setParent(String parent) {
        int index = 0;
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

    public Double getDouble(int index) throws IncompatibleTypeException {
        if(index > objectList.size()){
            throw new IndexOutOfBoundsException();
        }
        if(objectList.get(index) instanceof Double){
            return (Double) objectList.get(index);
        }
        throw new IncompatibleTypeException("CustomObject at index "+index+" is not of type Double");
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
        ArrayList<Object> newArrayList = new ArrayList<>();

        array.objectList.forEach(v -> {
            if (v instanceof Array) {
                ArrayList<Object> a = this.convertToArrayList((Array)v);
                newArrayList.add(a);
            }else if (v instanceof CustomObject) {
                HashMap<String, Object> a = new CustomObject().convertToHashMap((CustomObject) v);
                newArrayList.add(a);
            } else {
                newArrayList.add(v);
            }
        });
        return newArrayList;
    }

    @Override
    public String toString() {
        ArrayList<Object> arr = convertToArrayList(this);
        if(gson == null) gson = new Gson() ;
        return gson.toJson(arr);
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

    public int size(){
        return objectList.size();
    }

    public Array fromString(String str) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<Object> list = mapper.readValue(str, ArrayList.class);
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

//    public Array fromString(String value) {
//        Array newArray = new Array();
//        Type expectedType = new TypeToken<ArrayList<Object>>(){}.getType();
//        ArrayList object = gson.fromJson(value, expectedType);
//        object.forEach((v)
//                -> {
//            if (v instanceof ArrayList) {
//                Array a = this.fromString(v.toString());
//                newArray.put(a);
//            }else if (v instanceof Map) {
//                CustomObject db = new CustomObject();
//                CustomObject a = db.fromString(v.toString());
//                newArray.put(a);
//            } else {
//                newArray.put(v);
//            }
//        });
//        return newArray;
//    }

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
