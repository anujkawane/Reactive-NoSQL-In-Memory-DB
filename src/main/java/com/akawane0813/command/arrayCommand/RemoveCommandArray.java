package com.akawane0813.command.arrayCommand;

import com.akawane0813.command.DatabaseCommands;
import com.akawane0813.database.Array;

public class RemoveCommandArray implements DatabaseCommands {
    private int index;
    private Array array;
    private Object removedValue;

    public RemoveCommandArray(int index) {
        this.index = index;
    }

    public Object execute(Object array) {
        this.array = (Array) array;
        this.removedValue =  this.array.remove(index);
        return this.removedValue;
    }

    public Object undoOperation() {
        return array.put(removedValue);
    }

    public String toString() {
        return operationToString("REMOVE", array, removedValue);
    }
}
