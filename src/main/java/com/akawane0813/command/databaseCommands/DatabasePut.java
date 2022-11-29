package com.akawane0813.command.databaseCommands;

import com.akawane0813.command.IDatabaseCommands;
import com.akawane0813.database.Database;
import com.akawane0813.exception.KeyNotFoundException;

import java.io.Serializable;

public class DatabasePut implements IDatabaseCommands, Serializable {

    private final Object object;
    private Database db;
    private String key;

    public DatabasePut(String key, Object object) {
        this.key = key;
        this.object = object;
    }

    @Override
    public Object execute(Object db) {
        this.db = (Database) db;
        return this.db.put(key, object);
    }

    public Object undo() throws KeyNotFoundException {
        return db.remove(key);
    }

}
