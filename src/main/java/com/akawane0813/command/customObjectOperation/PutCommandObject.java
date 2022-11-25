package com.akawane0813.command.customObjectOperation;

import com.akawane0813.command.DatabaseCommands;
import com.akawane0813.database.CustomObject;
import com.akawane0813.exception.KeyNotFoundException;

public class PutCommandObject implements DatabaseCommands {
    private Object value;
    private CustomObject customObject;
    private String key;

    public PutCommandObject(String key,Object value) {
        this.key = key;
        this.value = value;
    }

    public Object execute(Object object) throws KeyNotFoundException {
        customObject = (CustomObject) object;
        return customObject.put(key, value);
    }
    public Object undoOperation() throws KeyNotFoundException {
        return customObject.remove(key);
    }

    public String toString() {
        return operationToString("PUT", this.customObject, this.value);
    }
}
