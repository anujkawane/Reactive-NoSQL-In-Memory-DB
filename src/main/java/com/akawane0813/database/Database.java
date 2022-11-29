package com.akawane0813.database;

import com.akawane0813.cursor.Cursor;
import com.akawane0813.cursor.CursorTracker;
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
    private final CursorTracker cursorTracker = CursorTracker.getInstance();

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

    /**
     * Check for snapshot after specific time
     */
    private void backup() {
        if(System.currentTimeMillis() > END_TIME){
            snapshot();
            END_TIME = System.currentTimeMillis() + BACKUP_INTERVAL_SECONDS * 1000;
        }
    }

    public boolean put(String key, Object value){
        database.put(key, value);

        backup();
        Cursor cursor = cursorTracker.getCursor(key);
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

    public Double getDouble(String key) throws Exception {
        if(!database.containsKey(key)) {
            throw new KeyNotFoundException("No such key as " + key);
        }

        if(database.get(key) instanceof Double){
            return (Double) database.get(key);
        }
        throw new IncompatibleTypeException("CustomObject at key "+key+" is not of type Double");
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

        Cursor cursor = cursorTracker.getCursor(key);
        if(cursor != null ) {
            cursor.updateObserver();
        }
        backup();
        return removed;
    }


    /**
     * Saves the state of the current object after specified time interval and clears the command file.
     */
    public void snapshot(){

        FileOperations fileOperation = new FileOperations();
        fileOperation.writeObjectToFile(new File(DATABASE_MEMENTO_FILEPATH), database);

        FileOperations.clearFile(new File(COMMANDS_FILEPATH));
    }

    /**
     * Saves the state of the current object after specified time interval and clears the command file.
     *
     * @param commands Commands file to clear after saving state
     * @param dbSnapshot DBSnapshot file to save the state
     */
    public void snapshot(File commands, File dbSnapshot){
        FileOperations fileOperation = new FileOperations();
        fileOperation.writeObjectToFile(dbSnapshot, database);

        FileOperations.clearFile(commands);
    }

    public Cursor getCursor(String key) {
        Cursor cursor = null;
        try {
            cursor = new Cursor(key, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cursor;
    }

    public Transaction transaction() {
        return new Transaction(this);
    }


    /**
     * Recovers previously saved state of the object by loading the snapshot
     * and then execute the saved commands on restored DB object
     */
    public void recover(){
        FileOperations fileOperation = new FileOperations();
        DataStore restoredDB =
                (DataStore) fileOperation.readObjectFromFile(new File(DATABASE_MEMENTO_FILEPATH));
        if(restoredDB != null) {
            database = restoredDB;
        }
        try {
            new DatabaseExecutor(this).executeSavedCommands(new File(COMMANDS_FILEPATH));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Recovers previously saved state of the object by loading the snapshot
     * and then execute the saved commands on restored DB object
     * @param commands Commands file to execute saved commands
     * @param dbSnapshot DBSnapshot file to recover state
     */
    public void recover(File commands, File dbSnapshot){
        FileOperations fileOperation = new FileOperations();
        DataStore restoredDB =
                (DataStore) fileOperation.readObjectFromFile(dbSnapshot);
        if(restoredDB != null) {
            database = restoredDB;
        }
        try {
            new DatabaseExecutor(this).executeSavedCommands(commands);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean containsKey(String key) {
        return database.containsKey(key);
    }
}
