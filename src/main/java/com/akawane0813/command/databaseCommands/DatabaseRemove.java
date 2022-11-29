package com.akawane0813.command.databaseCommands;

import com.akawane0813.command.IDatabaseCommands;
import com.akawane0813.database.Database;
import com.akawane0813.exception.KeyNotFoundException;

import java.io.Serializable;

public class DatabaseRemove implements IDatabaseCommands, Serializable {
    private Database db;
    private String key;
    private Object removedValue;

    public DatabaseRemove(String key) {
        this.key = key;
    }

    @Override
    public Object execute(Object db) throws KeyNotFoundException {
        this.db = (Database) db;
        removedValue = this.db.remove(key);
        return removedValue;
    }

    public Object undo() {
        return db.put(key, removedValue);
    }

}
