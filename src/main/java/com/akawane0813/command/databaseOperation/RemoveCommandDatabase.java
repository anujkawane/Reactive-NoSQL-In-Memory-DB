package com.akawane0813.command.databaseOperation;

import com.akawane0813.command.DatabaseCommands;
import com.akawane0813.database.Database;
import com.akawane0813.exception.KeyNotFoundException;

public class RemoveCommandDatabase implements DatabaseCommands {
    private Database db;
    private String key;
    private Object removedValue;

    public RemoveCommandDatabase(String key) {
        this.key = key;
    }

    @Override
    public Object execute(Object db) throws KeyNotFoundException {
        this.db = (Database) db;
        removedValue = this.db.remove(key);
        return removedValue;
    }

    public Object undoOperation() {
        return db.put(key,removedValue);
    }

    public String toString() {
        return operationToString("REMOVE", db, removedValue, key);
    }
}
