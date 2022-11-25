package com.akawane0813.command.customObjectOperation;

import com.akawane0813.command.DatabaseCommands;
import com.akawane0813.database.CustomObject;
import com.akawane0813.exception.KeyNotFoundException;

public class RemoveCommandObject implements DatabaseCommands {
    private CustomObject customObject;
    private String key;
    private Object removedValue;

    public RemoveCommandObject(String key) {
        this.key = key;
    }

    public Object execute(Object object) throws KeyNotFoundException {
        customObject = (CustomObject) object;
        return customObject.remove(key);
    }

    public Object undoOperation() throws KeyNotFoundException {
        return customObject.put(key, removedValue);
    }

    public String toString() {
        return operationToString("REMOVE", customObject, removedValue);
    }
}
