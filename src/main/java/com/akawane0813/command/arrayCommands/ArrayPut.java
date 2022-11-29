package com.akawane0813.command.arrayCommands;

import com.akawane0813.command.IDatabaseCommands;
import com.akawane0813.database.Array;

import java.io.Serializable;

public class ArrayPut implements IDatabaseCommands, Serializable {
    private Object value;
    private Array array;
    private int index;

    public ArrayPut(Object value) {
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

}
