package com.akawane0813.decorator;

import com.akawane0813.command.IDatabaseCommands;
import com.akawane0813.command.arrayCommands.ArrayPutCommand;
import com.akawane0813.command.arrayCommands.ArrayRemoveCommand;
import com.akawane0813.database.*;
import com.akawane0813.exception.IncompatibleTypeException;
import com.akawane0813.exception.KeyNotFoundException;

public class ArrayExecutor implements IArray {
    private Array array;
    private Database db;
    private String parent = "";
    private Executor executor;
    public ArrayExecutor(IArray array) {
        executor = Executor.Executor();
        this.array = (Array)array;
        if (executor != null) {
            db = executor.getDatabase();
        }
    }

    public boolean put(Object value) throws KeyNotFoundException {
        IDatabaseCommands put = new ArrayPutCommand(value);

        Boolean response = (boolean)put.execute(this.array);

        try {
            executor.writeToFile( "PUT->" + array.getParent() + "->" +db.get(array.getParent()).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public Object get(int index) {
        return this.array.get(index);
    }

    public int getInt(int index) throws IncompatibleTypeException {
        return this.array.getInt(index);
    }

    public Double getDouble(int index) throws IncompatibleTypeException {
        return this.array.getDouble(index);
    }

    public String getString(int index) throws IncompatibleTypeException {
        return this.array.getString(index);
    }

    public IArray getArray(int index) throws IncompatibleTypeException {
        return this.array.getArray(index);
    }

    public ICustomObject getObject(int index) throws IncompatibleTypeException {
        return new ObjectExecutor((CustomObject) this.array.getObject(index));
    }

    public String toString() {
        return this.array.toString();
    }

    /**
     * Removes object at specified index in array and returns it
     * @param index index of array
     * @return removed object
     * @throws Exception if index is out of bound
     */
    public Object remove(int index) throws Exception {
        IDatabaseCommands remove = new ArrayRemoveCommand(index);

        Object value = remove.execute(this.array);

        executor.writeToFile(  "PUT->" + array.getParent() + "->" + db.get(array.getParent()).toString());
        return value;
    }
}
