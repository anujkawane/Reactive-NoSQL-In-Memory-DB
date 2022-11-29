package com.akawane0813.command.arrayCommand;

import com.akawane0813.command.IDatabaseOperation;
import com.akawane0813.database.Array;


import java.io.Serializable;

public class ArrayRemove implements IDatabaseOperation, Serializable {
    private int index;
    private Array array;
    private Object removedValue;

    public ArrayRemove(int index) {
        this.index = index;
    }

    public Object execute(Object array) {
        this.array = (Array) array;
        removedValue =  this.array.remove(this.index);
        return removedValue;
    }

    public Object undo() {
        return array.put(removedValue);
    }

}
