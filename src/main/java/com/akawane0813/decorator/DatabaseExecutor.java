package com.akawane0813.decorator;

import com.akawane0813.command.IDatabaseOperation;
import com.akawane0813.command.databaseOperation.DatabasePut;
import com.akawane0813.command.databaseOperation.DatabaseRemove;
import com.akawane0813.cursor.Cursor;
import com.akawane0813.database.*;
import com.akawane0813.exception.KeyNotFoundException;
import com.akawane0813.transaction.Transaction;

import java.io.File;
import java.util.List;
import java.util.Stack;

public class DatabaseExecutor implements IDatabase {

    private Database database;
    private Stack<IDatabaseOperation> stack ;

    private File commandFile;
    private  Executor executor;
    private List<String> commandStrings;

    public DatabaseExecutor(Database db) {
        this.database = db;
        executor = Executor.Executor(db,new File("commands.txt"));

    }

    public DatabaseExecutor(Database db, File commandFile) {
        executor = Executor.Executor(db,commandFile);
        this.database = db;
        this.commandFile = commandFile;
    }

    public DatabaseExecutor(Database db, Stack<IDatabaseOperation> operations) {
        this.database = db;
        this.stack = operations;
        commandStrings = new Stack<>();
    }



    public void executeSavedOperations(File commandFile) throws Exception {
        List<List<String>> operations;
        if (commandFile == null) {
            operations = executor.retrieveOperations(new File("src/main/resources/commands.txt"));
        } else {
            operations = executor.retrieveOperations(new File("src/main/resources/commands.txt"));
        }
        for (List<String> operation : operations) {
            executeOperation(operation);
        }
    }

    public void executeOperation(List<String> operations) throws Exception {
        String key = operations.get(1);
        String value = operations.get(2);

        if (database.contains(key)) {
            database.remove(key);
            put(key,parseValue(value));
        } else {
            put(key,parseValue(value));
        }
    }

    public Object parseValue(String value) {

        if (value.charAt(0) == '[') {
            return new Array().fromString(value);
        } else if (value.charAt(0) == '{') {
            return new CustomObject().fromString(value);
        } else if(value.charAt(0) == '('){
            String intValue = value.substring(1,value.length()-1);
            return Integer.parseInt(intValue);
        } else {
            return value;
        }

    }

    public String toString() {
        return this.database.toString();
    }

    public boolean put(String key, Object value) throws Exception {
        IDatabaseOperation put = new DatabasePut(key,value);

        put.execute(this.database);

        insertSuperKeyInEveryChild(key, value);
        String commandString = "INSERT->" + key + "->" + database.get(key).toString();
        if (stack == null) {
            executor.writeToFile(commandString);
        } else {
            this.stack.add(put);
            commandStrings.add(commandString);
        }

        return true;
    }

    public void insertSuperKeyInEveryChild(String key,Object value) {
        if(value instanceof Array) {
            ((Array)value).setParent(key);
        } else if (value instanceof CustomObject) {
            ((CustomObject)value).setParent(key
            );
        }
    }


    public Object get(String key) throws Exception {
        return this.database.get(key);
    }

    public int getInt(String key) throws Exception {
        return this.database.getInt(key);
    }

    public Double getDouble(String key) throws Exception {
        return this.database.getDouble(key);
    }

    public String getString(String key) throws Exception {
        return this.database.getString(key);
    }

    public IArray getArray(String key) throws Exception {
        return new ArrayExecutor(this.database.getArray(key));
    }

    public ICustomObject getObject(String key) throws Exception {
        return new ObjectExecutor(database.getObject(key));
    }

    public Object remove(String key) throws KeyNotFoundException {
        IDatabaseOperation remove = new DatabaseRemove(key);

        Object value = remove.execute(this.database);
        String commandString = "INSERT->" + key +  "->" + "NO-VALUE";

        if (stack == null) {
            executor.writeToFile(commandString);
        } else {
            this.stack.add(remove);
            commandStrings.add(commandString);

        }
        return value;
    }

    public Cursor getCursor(String key) {
        return this.database.getCursor(key);
    }

    public Stack<IDatabaseOperation> getCommands() {
        return stack;
    }
    public boolean clearCommands() {
        this.stack = new Stack<>();
        return true;
    }

    public Transaction transaction() {
        return this.database.transaction();
    }

    public void commitCommands() {
        this.commandStrings.forEach((operation) -> {
            executor.writeToFile(operation);
        });
        snapshot();
    }

    public void snapshot() {
        database.snapshot();
    }

}
