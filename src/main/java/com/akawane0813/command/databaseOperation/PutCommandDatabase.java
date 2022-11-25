package com.akawane0813.command.databaseOperation;

import com.akawane0813.command.DatabaseCommands;
import com.akawane0813.database.Database;
import com.akawane0813.exception.KeyNotFoundException;

public class PutCommandDatabase implements DatabaseCommands {
    private final Object object;
    private Database db;
    private String key;

    public PutCommandDatabase(String key, Object object) {
        this.key = key;
        this.object = object;
    }

    @Override
    public Object execute(Object db) {
        this.db = (Database) db;
        return this.db.put(key, object);
    }

    public Object undoOperation() throws KeyNotFoundException {
        return db.remove(key);
    }

    public String toString() {
        return operationToString("PUT", db, object, key);
    }
}
