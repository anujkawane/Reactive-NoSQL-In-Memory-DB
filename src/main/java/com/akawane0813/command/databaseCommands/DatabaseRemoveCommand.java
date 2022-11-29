package com.akawane0813.command.databaseCommands;

import com.akawane0813.command.IDatabaseCommands;
import com.akawane0813.database.Database;
import com.akawane0813.exception.KeyNotFoundException;

import java.io.Serializable;

public class DatabaseRemoveCommand implements IDatabaseCommands, Serializable {
    private Database database;
    private String key;
    private Object removedValue;

    public DatabaseRemoveCommand(String key) {
        this.key = key;
    }

    @Override
    public Object execute(Object db) throws KeyNotFoundException {
        this.database = (Database) db;
        removedValue = this.database.remove(key);
        return removedValue;
    }

    public Object undo() {
        return database.put(key, removedValue);
    }

}
