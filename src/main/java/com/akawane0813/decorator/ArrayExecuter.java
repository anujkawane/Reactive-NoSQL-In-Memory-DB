package com.akawane0813.decorator;


import com.akawane0813.command.IDatabaseOperation;
import com.akawane0813.command.arrayCommand.PutOperationArray;
import com.akawane0813.command.arrayCommand.RemoveOperationArray;
import com.akawane0813.database.*;
import com.akawane0813.exception.IncompatibleTypeException;
import com.akawane0813.exception.KeyNotFoundException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ArrayExecuter implements IArray {
    private Array array;
    private Database db;
    private String parent = "";
    private Executor executor;
    public ArrayExecuter(IArray array) {
        executor = Executor.Executor();
        this.array = (Array)array;
        if (executor != null) {
            db = executor.getDatabase();
        }
    }

    public boolean put(Object value) throws KeyNotFoundException {
        IDatabaseOperation put = new PutOperationArray(value);

        Boolean response = (boolean)put.execute(this.array);
        String parentChain = "";
        try {
            parentChain = createParentChain(array.getParent(),value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        executor.writeToFile(put.toString() + parentChain);

        return response;
    }

    public String createParentChain(String key , Object value) throws Exception {
        if(db == null) {
            return key;
        }
        Object currentObject = db.get(key);

        List<String> chain = new ArrayList<>();
        chain.add("");
        if (currentObject instanceof Array) {
            Array array = ((Array)currentObject);
            int length = array.length();

            for(int i = 0;i<length;i++) {
                Object currentElement = array.get(i);
                if(dfs(currentElement,value, chain)) {
                    List<String> chainArr = Arrays.stream(chain.get(0).split("\\.")).collect(Collectors.toList());
                    Collections.reverse(chainArr);
                    String parentChain = "";
                    for (String parent : chainArr) {
                        parentChain +=  "." + parent;
                    }
                    return key +".("+ i+")"  +parentChain;
                }
            }
        } else if (currentObject instanceof CustomObject) {
            CustomObject customObject = ((CustomObject)value);
            int length  = customObject.length();
            List<String> keys = customObject.keys();
            for(int i = 0; i< length; i ++) {
                if(dfs(customObject.get(keys.get(i)),value,chain)) {
                    List<String> chainArr = Arrays.stream(chain.get(0).split("\\.")).collect(Collectors.toList());
                    Collections.reverse(chainArr);
                    String parentChain = "";
                    for (String parent : chainArr) {
                        parentChain +=  "." + parent;

                    }
                    return key +"."+keys.get(i) + parentChain;
                }
            }
        }
        return key;
    }

    public Boolean dfs(Object value,Object toFind, List<String> chain) throws KeyNotFoundException {
        if(value == toFind) {
            return true;
        }
        if (value instanceof Array) {
            Array array = ((Array)value);
            int length = array.length();
            for(int i = 0; i < length; i++) {
                if(dfs(array.get(i),toFind,chain)) {
                    String parentChain = chain.get(0);
                    parentChain += "." +"(" + "I" + i + ")";
                    chain.add(0,parentChain);
                    return true;
                }
            }
        } else if (value instanceof CustomObject) {
            CustomObject customObject = ((CustomObject)value);
            int length  = customObject.length();
            List<String> keys = customObject.keys();
            for(int i = 0; i< length; i ++) {
                if(dfs(customObject.get(keys.get(i)),toFind,chain)) {
                    String parentChain = chain.get(0);
                    parentChain += "." + keys.get(i);
                    chain.add(0,parentChain);
                    return true;
                }
            }
        }
        return false;
    }

    public Object get(int index) {
        return this.array.get(index);
    }

    public int getInt(int index) throws IncompatibleTypeException {
        return this.array.getInt(index);
    }

    public String getString(int index) throws IncompatibleTypeException {
        return this.array.getString(index);
    }

    public IArray getArray(int index) throws IncompatibleTypeException {
        return this.array.getArray(index);
    }

    public ICustomObject getObject(int index) throws IncompatibleTypeException {
        return new DBObjectExecutor((CustomObject) this.array.getObject(index));
    }

    public String toString() {
        return this.array.toString();
    }

    public Object remove(int index) throws KeyNotFoundException {
        IDatabaseOperation remove = new RemoveOperationArray(index);

        Object value = remove.execute(this.array);

        executor.writeToFile(remove.toString());
        return value;
    }
}
