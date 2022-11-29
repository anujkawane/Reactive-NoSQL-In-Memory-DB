package com.akawane0813.decorator;

import com.akawane0813.command.IDatabaseOperation;
import com.akawane0813.command.customObjectOperation.ObjectPut;
import com.akawane0813.command.customObjectOperation.ObjectRemove;
import com.akawane0813.database.*;
import com.akawane0813.exception.IncompatibleTypeException;
import com.akawane0813.exception.KeyNotFoundException;
import com.akawane0813.fileio.FileOperations;


public class ObjectExecutor implements ICustomObject {
    private CustomObject customObject;
    private FileOperations fileOperation;
    private String parent;
    private Executor executor;
    private Database database;
    public ObjectExecutor(ICustomObject db) {
        this.customObject = (CustomObject) db;
        this.fileOperation = new FileOperations();
        executor = Executor.Executor();
        database= executor.getDatabase();
    }

    public boolean put(String key, Object value) throws KeyNotFoundException {
        IDatabaseOperation put = new ObjectPut(key,value);

        Boolean res = (boolean)put.execute(this.customObject);

        Object newValue;
        try {
            newValue = database.get(customObject.getParent());
        } catch (Exception e) {
            e.printStackTrace();
            newValue = null;
        }

        executor.writeToFile( "INSERT->" + customObject.getParent() + "->" +newValue.toString() );

        return res;
    }


    public Object get(String key) throws KeyNotFoundException {
        return this.customObject.get(key);
    }

    public int getInt(String key) throws IncompatibleTypeException {
        return this.customObject.getInt(key);
    }

    public String toString(){
        return this.customObject.toString();
    }

    public IArray getArray(String key) throws IncompatibleTypeException {
        return new ArrayExecutor(this.customObject.getArray(key));
    }

    public ICustomObject getObject(String key) throws IncompatibleTypeException {
        return new ObjectExecutor(this.customObject.getObject(key));
    }

    public Object remove(String key) throws KeyNotFoundException {
        IDatabaseOperation remove = new ObjectRemove(key);

        Object value = remove.execute(this.customObject);

        try {
            executor.writeToFile("INSERT" + "->" + customObject.getParent() + "->" + database.get(customObject.getParent()).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

}
