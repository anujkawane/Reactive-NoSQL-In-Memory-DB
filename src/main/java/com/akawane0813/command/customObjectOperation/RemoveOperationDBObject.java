package com.akawane0813.command.customObjectOperation;

import com.akawane0813.command.IDatabaseOperation;
import com.akawane0813.database.CustomObject;
import com.akawane0813.exception.KeyNotFoundException;

import java.io.Serializable;

public class RemoveOperationDBObject implements IDatabaseOperation, Serializable {
    private CustomObject customObject;
    private String key;
    private Object removedValue;

    public RemoveOperationDBObject(String key) {
        this.key = key;
    }

    public Object execute(Object object) throws KeyNotFoundException {
        customObject = (CustomObject) object;
        return customObject.remove(key);
    }

    public Object undo() throws KeyNotFoundException {
        return customObject.put(key, removedValue);
    }

    public String toString() {
        return this.operationToString("REMOVE", customObject, removedValue);
    }
}
