package com.akawane0813.decorator;

import com.akawane0813.command.IDatabaseOperation;
import com.akawane0813.command.databaseOperation.PutOperationDatabase;
import com.akawane0813.command.databaseOperation.RemoveOperationDatabase;
import com.akawane0813.cursor.Cursor;
import com.akawane0813.database.*;
import com.akawane0813.exception.KeyNotFoundException;
import com.akawane0813.transaction.Transaction;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class DatabaseExecutor implements IDatabase {

    private Database db;
    private Stack<IDatabaseOperation> stack ;

    private File commandFile;
    private  Executor executor;

    public DatabaseExecutor(Database db) {
        this.db = db;
        executor = Executor.Executor(db,new File("commands.txt"));

    }

    public DatabaseExecutor(Database db, File commandFile) {
        executor = Executor.Executor(db,commandFile);
        this.db = db;
        this.commandFile = commandFile;
    }

    public DatabaseExecutor(Database db, Stack<IDatabaseOperation> operations) {
        this.db = db;
        this.stack = operations;
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
        String operation = operations.get(1) + " " +operations.get(0);
        String value = operations.get(2);
        String key = operations.get(3);
        List<String> listOfKeys = getKeys(key);

        switch(operation) {
            case "PUT DB" :
                put(key,parseValue(value));
                break;
            case "REMOVE DB" :
                remove(key);
                break;
            case "PUT ARRAY":
            case "PUT CustomObject":
                insertValueIntoNestedKeys(listOfKeys,value);
                break;
            case "REMOVE ARRAY":
            case "REMOVE CustomObject":
                removeValueFromNestedKeys(listOfKeys);
                break;
        }
    }

    public List<String> getKeys(String key) {
        String[] keys = key.split("\\.");
        return Arrays.stream(keys).collect(Collectors.toList());
    }

    public void removeValueFromNestedKeys(List<String> listOfKeys) throws Exception {
        Object object = get(listOfKeys.get(0));
        listOfKeys.remove(0);
        int listSize = listOfKeys.size()-1;
        int currentIndex = 0;

        for (String key : listOfKeys) {
            if( key.contains("(")) {
                int index = Integer.parseInt(key.substring(1,key.length()-1));
                if (currentIndex == listSize) {
                    object = (new ArrayExecuter((Array) object)).remove(index);
                } else {
                    object = ((Array)(object)).get(index);
                }
            } else {
                if (currentIndex == listSize) {
                    object = (new DBObjectExecutor((CustomObject) object)).remove(key);
                } else {
                    object = ((CustomObject)object).get(key);

                }
            }
            currentIndex++;
        }

    }

    public void insertValueIntoNestedKeys(List<String> listOfKeys, String value) throws Exception {
        Object object = get(listOfKeys.get(0));
        listOfKeys.remove(0);
        int listSize = listOfKeys.size()-1;
        int currentIndex = 0;

        for (String key : listOfKeys) {
            if( key.contains("(")) {
                int index = Integer.parseInt(key.substring(1,key.length()-1));
                if (currentIndex == listSize) {
                    object = (new ArrayExecuter((Array) object)).put(parseValue(value));
                } else {
                    object = ((Array)(object)).get(index);
                }
            } else {
                if (currentIndex == listSize) {
                    object = (new DBObjectExecutor((CustomObject) object)).put(key,parseValue(value));
                } else {
                    object = ((CustomObject)object).get(key);

                }
            }
            currentIndex++;
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
        return this.db.toString();
    }

    public boolean put(String key, Object value) throws Exception {
        IDatabaseOperation put = new PutOperationDatabase(key,value);

        put.execute(this.db);
        insertSuperKeyInEveryChild(key, value);
        if (stack == null) {
            executor.writeToFile(put.toString() + key);
        } else {
            this.stack.add(put);

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
        return this.db.get(key);
    }

    public int getInt(String key) throws Exception {
        return this.db.getInt(key);
    }

    public String getString(String key) throws Exception {
        return this.db.getString(key);
    }

    public IArray getArray(String key) throws Exception {
        return new ArrayExecuter(this.db.getArray(key));
    }

    public CustomObject getObject(String key) throws Exception {
        return this.db.getObject(key);
    }

    public Object remove(String key) throws KeyNotFoundException {
        IDatabaseOperation remove = new RemoveOperationDatabase(key);

        Object value = remove.execute(this.db);

        if (stack == null) {
            executor.writeToFile(remove.toString() + key);
        } else {
            this.stack.add(remove);

        }
        return value;
    }

    public Cursor getCursor(String key) {
        return this.db.getCursor(key);
    }

    public Stack<IDatabaseOperation> getCommands() {
        return stack;
    }
    public boolean clearCommands() {
        this.stack = new Stack<>();
        return true;
    }

    public Transaction transaction() {
        return this.db.transaction();
    }

    public void commitCommands() {
        this.stack.forEach((operation) -> {
            executor.writeToFile(operation.toString());
        });
        snapshot();
    }

    public void snapshot() {
        db.snapshot();
    }

}
