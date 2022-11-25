package com.akawane0813.database;

import com.akawane0813.exception.IncompatibleTypeException;
import com.akawane0813.exception.KeyNotFoundException;
import com.akawane0813.fileio.FileOperations;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Database implements Serializable, IDatabase {

    private Map<String, Object> database;
    private final String DATABASE_MEMENTO_FILEPATH = "src/main/resources/dbSnapshot.txt";
    private final String COMMANDS_FILEPATH = "src/main/resources/commands.txt";
    private int counterForBackUp = 0;
    private final int intervalForBackup = 5;

    @Override
    public String toString() {
        return "Database" +
                database +
                "";
    }

    public Database(){
        initializeDatabase();
    }

    //load BookList from memento if available
    private void initializeDatabase() {
        recover();
        if(database == null){
            database = new HashMap();
        }
    }

    private void backup() {
        counterForBackUp++;
        if(counterForBackUp == intervalForBackup) {
//            need to modify code for custom files given to snapshot
            snapshot();
            counterForBackUp = 0;
        }
    }

    public Database put(String key, Object value){
        database.put(key, value);
        backup();
        return this;
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

    public IArray getArray(String key) throws Exception {
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

        if(database.get(key) instanceof Array){
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
        backup();
        return removed;
    }


    public void snapshot(){

        FileOperations fileOperation = new FileOperations();
        fileOperation.writeObjectToFile(new File(DATABASE_MEMENTO_FILEPATH), this);

//        clears the commands from file after backup
        FileOperations.clearFile(new File(COMMANDS_FILEPATH));
    }

    public void snapshot(File commands, File dbSnapshot){
        FileOperations fileOperation = new FileOperations();
        fileOperation.writeObjectToFile(dbSnapshot, this);

//        clears the commands from file after backup
        FileOperations.clearFile(commands);
    }

    public void recover(){
        FileOperations fileOperation = new FileOperations();
        Database restoredDB =
                (Database) fileOperation.readObjectFromFile(new File(DATABASE_MEMENTO_FILEPATH));
        if(restoredDB != null) {
            database = restoredDB.database;
        }
        // CODE TO RE-EXECUTE COMMANDS FROM commands FILE INTO EXISTING OBJECT
    }

    public void clear(){
        FileOperations.clearFile(new File(DATABASE_MEMENTO_FILEPATH));
    }

    public void recover(File commands, File dbSnapshot){
        FileOperations fileOperation = new FileOperations();
        Database restoredDB =
                (Database) fileOperation.readObjectFromFile(dbSnapshot);
        if(restoredDB != null) {
            database = restoredDB.database;
        }
        // CODE TO RE-EXECUTE COMMANDS FROM commands FILE INTO EXISTING OBJECT
    }
}
