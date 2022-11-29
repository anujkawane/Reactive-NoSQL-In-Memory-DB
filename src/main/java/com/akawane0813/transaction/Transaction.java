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

    private Database db;
    private DatabaseExecutor databaseExecutor;

    private Boolean isActive = true;
    Stack<IDatabaseOperation> operations = new Stack<>();
    public Transaction(Database db){
        this.db = db;
        System.out.println("Current db");
        System.out.println(this.db.toString());
        databaseExecutor = new DatabaseExecutor(this.db,operations);
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
        return this.db.getInt(key);
    }

    public String getString(String key) throws Exception {
        return this.db.getString(key);
    }

    public Array getArray(String key) throws Exception {
        return this.db.getArray(key);
    }

    public CustomObject getObject(String key) throws Exception {
        return this.db.getObject(key);
    }


    public Object get(String key) throws Exception {
        return this.db.get(key);
    }

    public Object remove(String key) throws KeyNotFoundException {
        return this.databaseExecutor.remove(key);
    }

    public boolean abort() throws KeyNotFoundException {
        this.operations = this.databaseExecutor.getCommands();
        System.out.println("Command lit");
        System.out.println(this.operations);
        while(!this.operations.isEmpty()) {
            IDatabaseOperation operation = this.operations.pop();
            System.out.println(operation);
            operation.undo();
        }
        this.isActive = false;
        System.out.println("Herre");

        System.out.println(databaseExecutor.toString());
        databaseExecutor.snapshot();
        return true;
    }

    public boolean commit() {
        databaseExecutor.commitCommands();
        this.isActive = false;
        return true;
    }


}
