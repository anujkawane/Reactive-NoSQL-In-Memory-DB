package com.akawane0813.decorator;


import com.akawane0813.command.IDatabaseOperation;
import com.akawane0813.command.arrayCommand.PutOperationArray;
import com.akawane0813.command.arrayCommand.RemoveOperationArray;
import com.akawane0813.database.*;
import com.akawane0813.exception.IncompatibleTypeException;
import com.akawane0813.exception.KeyNotFoundException;


public class ArrayExecuter implements IArray {
    private Array array;
    private Database db;
    private String parent = "";
    private Executor executor;
    public ArrayExecuter(IArray array) {
        executor = Executor.Executor();
        this.array = (Array)array;
        if (executor != null) {
            db = executor.getDatabase();
        }
    }

    public boolean put(Object value) throws KeyNotFoundException {
        IDatabaseOperation put = new PutOperationArray(value);

        Boolean response = (boolean)put.execute(this.array);

        try {
            executor.writeToFile( "INSERT " + array.getParent() + " " +db.get(array.getParent()).toString() );
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

    public String getString(int index) throws IncompatibleTypeException {
        return this.array.getString(index);
    }

    public IArray getArray(int index) throws IncompatibleTypeException {
        return this.array.getArray(index);
    }

    public ICustomObject getObject(int index) throws IncompatibleTypeException {
        return new DBObjectExecutor((CustomObject) this.array.getObject(index));
    }

    public String toString() {
        return this.array.toString();
    }

    public Object remove(int index) throws Exception {
        IDatabaseOperation remove = new RemoveOperationArray(index);

        Object value = remove.execute(this.array);

        executor.writeToFile(  "REMOVE " + array.getParent() + " " + db.get(array.getParent()).toString() );
        return value;
    }
}
