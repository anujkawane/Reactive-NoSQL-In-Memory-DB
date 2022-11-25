package com.akawane0813.database;

import com.akawane0813.exception.IncompatibleTypeException;
import com.akawane0813.exception.KeyNotFoundException;
import com.akawane0813.fileio.FileOperations;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class DatabaseImpl implements Serializable, Database {

    private Map<String, java.lang.Object> database;
    private final String DATABASE_MEMENTO_FILEPATH = "src/main/resources/dbSnapshot.txt";
    private final String COMMANDS_FILEPATH = "src/main/resources/commands.txt";
    private int counterForBackUp = 0;
    private final int intervalForBackup = 5;

    @Override
    public String toString() {
        return "DatabaseImpl" +
                database +
                "";
    }

    public DatabaseImpl(){
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

    public DatabaseImpl put(String key, java.lang.Object value){
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
        throw new IncompatibleTypeException("Object at key "+key+" is not of type String");
    }

    public int getInt(String key) throws Exception {
        if(!database.containsKey(key)) {
            throw new KeyNotFoundException("No such key as " + key);
        }

        if(database.get(key) instanceof Integer){
            return (int) database.get(key);
        }
        throw new IncompatibleTypeException("Object at key "+key+" is not of type Integer");
    }

    public Array getArray(String key) throws Exception {
        if(!database.containsKey(key)) {
            throw new KeyNotFoundException("No such key as " + key);
        }

        if(database.get(key) instanceof Array){
            return (Array) database.get(key);
        }
        throw new IncompatibleTypeException("Object at key "+key+" is not of type Array");
    }

    public Object getObject(String key) throws Exception {
        if(!database.containsKey(key)) {
            throw new KeyNotFoundException("No such key as " + key);
        }

        if(database.get(key) instanceof Array){
            return (Object) database.get(key);
        }
        throw new IncompatibleTypeException("Object at key "+key+" is not of type Object");
    }

    public java.lang.Object get(String key) throws Exception {
        if(!database.containsKey(key)) {
            throw new KeyNotFoundException("No such key as " + key);
        }
        return database.get(key);
    }

    public java.lang.Object remove(String key) throws KeyNotFoundException {
        if(!database.containsKey(key)) {
            throw new KeyNotFoundException("No such key as " + key);
        }
        java.lang.Object removed =  database.remove(key);
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
        DatabaseImpl restoredDB =
                (DatabaseImpl) fileOperation.readObjectFromFile(new File(DATABASE_MEMENTO_FILEPATH));
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
        DatabaseImpl restoredDB =
                (DatabaseImpl) fileOperation.readObjectFromFile(dbSnapshot);
        if(restoredDB != null) {
            database = restoredDB.database;
        }
        // CODE TO RE-EXECUTE COMMANDS FROM commands FILE INTO EXISTING OBJECT
    }
}
