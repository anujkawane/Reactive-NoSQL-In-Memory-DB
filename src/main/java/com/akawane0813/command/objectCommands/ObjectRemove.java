package com.akawane0813.command.objectCommands;

import com.akawane0813.command.IDatabaseCommands;
import com.akawane0813.database.CustomObject;
import com.akawane0813.exception.KeyNotFoundException;

import java.io.Serializable;

public class ObjectRemove implements IDatabaseCommands, Serializable {
    private CustomObject customObject;
    private String key;
    private Object removedValue;

    public ObjectRemove(String key) {
        this.key = key;
    }

    public Object execute(Object object) throws KeyNotFoundException {
        customObject = (CustomObject) object;
        return customObject.remove(key);
    }

    public Object undo() throws KeyNotFoundException {
        return customObject.put(key, removedValue);
    }


}
