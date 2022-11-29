package com.akawane0813.command.databaseCommands;

import com.akawane0813.command.IDatabaseCommands;
import com.akawane0813.database.Database;
import com.akawane0813.exception.KeyNotFoundException;

import java.io.Serializable;

public class DatabasePutCommand implements IDatabaseCommands, Serializable {

    private final Object object;
    private Database database;
    private String key;

    public DatabasePutCommand(String key, Object object) {
        this.key = key;
        this.object = object;
    }

    @Override
    public Object execute(Object database) {
        this.database = (Database) database;
        return this.database.put(key, object);
    }

    public Object undo() throws KeyNotFoundException {
        return database.remove(key);
    }
}
