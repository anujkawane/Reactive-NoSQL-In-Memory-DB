package com.akawane0813.command.arrayCommand;

import com.akawane0813.command.DatabaseCommands;
import com.akawane0813.database.Array;

public class PutCommandArray implements DatabaseCommands {
    private Object value;
    private Array array;
    private int index;

    public PutCommandArray(Object value) {
        this.value = value;
    }

    public Object execute(Object array) {
        this.array = (Array) array;
        this.index = this.array.length();
        return this.array.put(value);
    }

    public Object undoOperation() {
        return array.remove(index);
    }

    public String toString() {
        return operationToString("PUT", array, value);
    }
}
