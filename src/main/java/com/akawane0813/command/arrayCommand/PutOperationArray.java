package com.akawane0813.command.arrayCommand;

import com.akawane0813.command.IDatabaseOperation;
import com.akawane0813.database.Array;

import java.io.Serializable;

public class PutOperationArray implements IDatabaseOperation, Serializable {
    private Object value;
    private Array array;
    private int index;

    public PutOperationArray(Object value) {
        this.value = value;
    }

    public Object execute(Object array) {
        this.array = (Array) array;
        index = this.array.length();
        return this.array.put(value);
    }

    public Object undo() {
        return array.remove(index);
    }

    public String toString() {
        return operationToString("PUT", array, value);
    }

}