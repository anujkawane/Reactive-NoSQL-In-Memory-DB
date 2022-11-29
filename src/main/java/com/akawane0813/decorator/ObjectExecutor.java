package com.akawane0813.decorator;

import com.akawane0813.command.IDatabaseCommands;
import com.akawane0813.command.objectCommands.ObjectPutCommand;
import com.akawane0813.command.objectCommands.ObjectRemoveCommand;
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
        IDatabaseCommands put = new ObjectPutCommand(key,value);

        Boolean res = (boolean)put.execute(this.customObject);

        Object newValue;
        try {
            newValue = database.get(customObject.getParent());
        } catch (Exception e) {
            e.printStackTrace();
            newValue = null;
        }

        executor.writeToFile( "PUT->" + customObject.getParent() + "->" +newValue.toString() );

        return res;
    }


    public Object get(String key) throws KeyNotFoundException {
        return this.customObject.get(key);
    }

    public String getString(String key) throws Exception {
        return customObject.getString(key);
    }

    public int getInt(String key) throws IncompatibleTypeException {
        return customObject.getInt(key);
    }

    public Double getDouble(String key) throws Exception {
        return customObject.getDouble(key);
    }

    public String toString(){
        return customObject.toString();
    }

    public IArray getArray(String key) throws IncompatibleTypeException {
        return new ArrayExecutor(this.customObject.getArray(key));
    }

    public ICustomObject getObject(String key) throws IncompatibleTypeException {
        return new ObjectExecutor(this.customObject.getObject(key));
    }

    /**
     * Removes object at given key
     * @param key to remove object
     * @return removed object
     * @throws KeyNotFoundException if key is not present
     */
    public Object remove(String key) throws Exception {
        IDatabaseCommands remove = new ObjectRemoveCommand(key);

        Object value = remove.execute(this.customObject);
        executor.writeToFile("PUT" + "->" + customObject.getParent() + "->" +
                database.get(customObject.getParent()).toString());

        return value;
    }
}
