package com.akawane0813.transaction;

import com.akawane0813.command.IDatabaseOperation;
import com.akawane0813.database.Array;
import com.akawane0813.database.CustomObject;
import com.akawane0813.database.Database;
import com.akawane0813.database.IDatabase;
import com.akawane0813.decorator.DatabaseExecutor;
import com.akawane0813.exception.KeyNotFoundException;

import java.util.Stack;

public class Transaction implements IDatabase {

    private Database database;
    private DatabaseExecutor databaseExecutor;

    private Boolean isActive = true;
    Stack<IDatabaseOperation> operations = new Stack<>();
    public Transaction(Database database){
        this.database = database;
        databaseExecutor = new DatabaseExecutor(this.database,operations);
    }
    public boolean put(String key, Object value) throws KeyNotFoundException {
        boolean response = false;
        try {
            response = databaseExecutor.put(key,value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public int getInt(String key) throws Exception {
        return database.getInt(key);
    }

    public Double getDouble(String key) throws Exception {
        return database.getDouble(key);
    }

    public String getString(String key) throws Exception {
        return database.getString(key);
    }

    public Array getArray(String key) throws Exception {
        return database.getArray(key);
    }

    public CustomObject getObject(String key) throws Exception {
        return database.getObject(key);
    }


    public Object get(String key) throws Exception {
        return database.get(key);
    }

    public Object remove(String key) throws KeyNotFoundException {
        return databaseExecutor.remove(key);
    }

    public boolean abort() throws KeyNotFoundException {
        operations = databaseExecutor.getCommands();
        while(!operations.isEmpty()) {
            IDatabaseOperation operation = operations.pop();
            operation.undo();
        }
        isActive = false;

        databaseExecutor.snapshot();
        return true;
    }

    public boolean commit() {
        databaseExecutor.commitCommands();
        isActive = false;
        return true;
    }


}
