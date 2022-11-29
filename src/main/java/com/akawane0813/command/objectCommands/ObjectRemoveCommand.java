package com.akawane0813.command.objectCommands;

import com.akawane0813.command.IDatabaseCommands;
import com.akawane0813.database.CustomObject;
import com.akawane0813.exception.KeyNotFoundException;

import java.io.Serializable;

public class ObjectRemoveCommand implements IDatabaseCommands, Serializable {
    private CustomObject customObject;
    private String key;
    private Object removedValue;

    public ObjectRemoveCommand(String key) {
        this.key = key;
    }

    public Object execute(Object object) throws KeyNotFoundException {
        customObject = (CustomObject) object;
        removedValue = customObject.remove(key);
        return removedValue;
    }

    public Object undo() throws KeyNotFoundException {
        return customObject.put(key, removedValue);
    }


}
