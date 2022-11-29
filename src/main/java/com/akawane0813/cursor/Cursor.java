package com.akawane0813.cursor;


import com.akawane0813.database.Array;
import com.akawane0813.database.CustomObject;
import com.akawane0813.database.Database;
import com.akawane0813.exception.IncompatibleTypeException;
import com.akawane0813.exception.KeyNotFoundException;
import com.akawane0813.observer.IObserver;
import com.akawane0813.observer.Observer;

import java.util.ArrayList;
import java.util.List;

public class Cursor {
    private String key;
    private Database db;
    private Object currentValue;

    public List<IObserver> observers = new ArrayList<>();

    private CursorMapper mapper = CursorMapper.CursorMapper();
    public Cursor(String key, Database db) throws Exception {

        this.db = db;
        this.key = key;
        this.currentValue = this.db.get(key);

        if (this.currentValue instanceof Array) {
            this.currentValue = ((Array)this.db.get(key)).clone();
        } else if(this.currentValue instanceof CustomObject) {
            this.currentValue = ((CustomObject)this.db.get(key)).clone();
        }

        mapper.put(key,this);
    }
    
    public boolean updateObserver() {
        String message = "";
        try {
            Object newValue = this.db.get(key);
            message =  key + " in the DB updated with "+newValue.toString();

        } catch(KeyNotFoundException e) {
            message = key + " removed from the DB";
        } catch (Exception e) {
            e.printStackTrace();
        }


        String finalMessage = message;
        observers.forEach((o)->o.update(finalMessage));
        return true;
    }

    public boolean addObserver(IObserver observer) {
        observers.add(observer);
        return true;
    }

    public boolean removeObserver(Observer observer) {
        observers.remove(observer);
        return true;
    }

    public Object get() {
        return this.currentValue;
    }

    public Integer getInt() throws IncompatibleTypeException {
        if(!(this.currentValue instanceof Integer)) {
            throw new IncompatibleTypeException("The current value is not an integer");
        }
        return (Integer) this.currentValue;
    }

    public String getString() throws IncompatibleTypeException {
        if(!(this.currentValue instanceof String)) {
            throw new IncompatibleTypeException("The current value is not a string");
        }
        return (String) this.currentValue;
    }

    public Array getArray() throws IncompatibleTypeException {
        if(!(this.currentValue instanceof Array)) {
            throw new IncompatibleTypeException("The current value is not an Array");
        }
        return (Array) this.currentValue;
    }

    public CustomObject getObject() throws IncompatibleTypeException {
        if(!(this.currentValue instanceof CustomObject)) {
            throw new IncompatibleTypeException("The current value is not a custom object");
        }
        return (CustomObject) this.currentValue;
    }
}
