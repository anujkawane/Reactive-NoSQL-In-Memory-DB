package com.akawane0813.command.customObjectOperation;

import com.akawane0813.command.IDatabaseOperation;
import com.akawane0813.database.CustomObject;
import com.akawane0813.exception.KeyNotFoundException;

import java.io.Serializable;

public class PutOperationDBObject implements IDatabaseOperation, Serializable {
    private Object value;
    private CustomObject customObject;
    private String key;

    public PutOperationDBObject(String key,Object value) {
        this.key = key;
        this.value = value;
    }

    public Object execute(Object object) throws KeyNotFoundException {
        customObject = (CustomObject) object;
        return customObject.put(key, value);
    }
    public Object undo() throws KeyNotFoundException {
        return customObject.remove(key);
    }

    public String toString() {
        return operationToString("PUT", customObject, value);
    }
}
