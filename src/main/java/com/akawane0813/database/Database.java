package com.akawane0813.database;

import com.akawane0813.cursor.Cursor;
import com.akawane0813.cursor.CursorMapper;
import com.akawane0813.decorator.DatabaseExecutor;
import com.akawane0813.exception.IncompatibleTypeException;
import com.akawane0813.exception.KeyNotFoundException;
import com.akawane0813.fileio.FileOperations;
import com.akawane0813.transaction.Transaction;

import java.io.File;
import java.io.Serializable;

public class Database implements Serializable, IDatabase {

    private DataStore database;
    private final String DATABASE_MEMENTO_FILEPATH = "src/main/resources/dbSnapshot.txt";
    private final String COMMANDS_FILEPATH = "src/main/resources/commands.txt";
    private final int BACKUP_INTERVAL_SECONDS = 5;

    long END_TIME = System.currentTimeMillis() + BACKUP_INTERVAL_SECONDS * 1000;
    private final CursorMapper cursorMapper = CursorMapper.CursorMapper();

    @Override
    public String toString() {
        return "" +
                database +
                "";
    }

    public Database(){
        initializeDatabase();
    }

    //load BookList from memento if available
    private void initializeDatabase() {
        database = new DataStore();
        recover();

    }

    private void backup() {
        if(System.currentTimeMillis() > END_TIME){
            snapshot();
            END_TIME = System.currentTimeMillis() + BACKUP_INTERVAL_SECONDS * 1000;
        }
    }

    public boolean put(String key, Object value){
        database.put(key, value);

        backup();
        Cursor cursor = cursorMapper.getCursor(key);
        if(cursor != null ) {
            cursor.updateObserver();
        }
        return true;
    }

    public String getString(String key) throws Exception {
        if(!database.containsKey(key)) {
            throw new KeyNotFoundException("No such key as " + key);
        }

        if(database.get(key) instanceof String){
           return (String) database.get(key);
        }
        throw new IncompatibleTypeException("CustomObject at key "+key+" is not of type String");
    }

    public int getInt(String key) throws Exception {
        if(!database.containsKey(key)) {
            throw new KeyNotFoundException("No such key as " + key);
        }

        if(database.get(key) instanceof Integer){
            return (int) database.get(key);
        }
        throw new IncompatibleTypeException("CustomObject at key "+key+" is not of type Integer");
    }

    public Array getArray(String key) throws Exception {
        if(!database.containsKey(key)) {
            throw new KeyNotFoundException("No such key as " + key);
        }

        if(database.get(key) instanceof Array){
            return (Array) database.get(key);
        }
        throw new IncompatibleTypeException("CustomObject at key "+key+" is not of type Array");
    }

    public CustomObject getObject(String key) throws Exception {
        if(!database.containsKey(key)) {
            throw new KeyNotFoundException("No such key as " + key);
        }

        if(database.get(key) instanceof CustomObject){
            return (CustomObject) database.get(key);
        }
        throw new IncompatibleTypeException("CustomObject at key "+key+" is not of type CustomObject");
    }

    public Object get(String key) throws Exception {
        if(!database.containsKey(key)) {
            throw new KeyNotFoundException("No such key as " + key);
        }

        return database.get(key);
    }

    public Object remove(String key) throws KeyNotFoundException {
        if(!database.containsKey(key)) {
            throw new KeyNotFoundException("No such key as " + key);
        }
        Object removed =  database.remove(key);

        Cursor cursor = cursorMapper.getCursor(key);
        if(cursor != null ) {
            cursor.updateObserver();
        }
        backup();
        return removed;
    }


    public void snapshot(){

        FileOperations fileOperation = new FileOperations();
        fileOperation.writeObjectToFile(new File(DATABASE_MEMENTO_FILEPATH), database);

//        clears the commands from file after backup
        FileOperations.clearFile(new File(COMMANDS_FILEPATH));
    }

    public void snapshot(File commands, File dbSnapshot){
        FileOperations fileOperation = new FileOperations();
        fileOperation.writeObjectToFile(dbSnapshot, database);

//        clears the commands from file after backup
        FileOperations.clearFile(commands);
    }

    public Cursor getCursor(String key) {
        Cursor cursor = null;
        try {
            cursor = new Cursor(key, this);
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cursor;
    }

    public Transaction transaction() {
        return new Transaction(this);
    }


    public void recover(){
        FileOperations fileOperation = new FileOperations();
        DataStore restoredDB =
                (DataStore) fileOperation.readObjectFromFile(new File(DATABASE_MEMENTO_FILEPATH));
        if(restoredDB != null) {
            database = restoredDB;
        }
        try {
            new DatabaseExecutor(this).executeSavedOperations(new File("src/main/resources/commands.txt"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clear(){
        FileOperations.clearFile(new File(DATABASE_MEMENTO_FILEPATH));
    }

    public void recover(File commands, File dbSnapshot){
        FileOperations fileOperation = new FileOperations();
        DataStore restoredDB =
                (DataStore) fileOperation.readObjectFromFile(dbSnapshot);
        if(restoredDB != null) {
            database = restoredDB;
        }
        try {
            new DatabaseExecutor(this).executeSavedOperations(commands);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean contains(String key) {
        return database.containsKey(key);
    }
}
