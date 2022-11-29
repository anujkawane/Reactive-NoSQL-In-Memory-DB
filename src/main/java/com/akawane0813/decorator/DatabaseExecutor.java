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
        String key = operations.get(1);
        String value = operations.get(2);

        if (db.contains(key)) {
            db.remove(key);
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
        return this.db.toString();
    }

    public boolean put(String key, Object value) throws Exception {
        IDatabaseOperation put = new PutOperationDatabase(key,value);

        put.execute(this.db);

        insertSuperKeyInEveryChild(key, value);
        if (stack == null) {
            executor.writeToFile( "INSERT " + key + " " + db.get(key).toString());
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

    public ICustomObject getObject(String key) throws Exception {
        return new DBObjectExecutor(db.getObject(key));
    }

    public Object remove(String key) throws KeyNotFoundException {
        IDatabaseOperation remove = new RemoveOperationDatabase(key);

        Object value = remove.execute(this.db);

        if (stack == null) {
            try {
                executor.writeToFile("INSERT " + key +  " " + db.get(key).toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
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
