package com.akawane0813.decorator;

import com.akawane0813.command.IDatabaseOperation;
import com.akawane0813.command.customObjectOperation.PutOperationDBObject;
import com.akawane0813.command.customObjectOperation.RemoveOperationDBObject;
import com.akawane0813.database.*;
import com.akawane0813.exception.IncompatibleTypeException;
import com.akawane0813.exception.KeyNotFoundException;
import com.akawane0813.fileio.FileOperations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DBObjectExecutor implements ICustomObject {
    private CustomObject db;
    private FileOperations fileOperation;
    private String parent;
    private Executor executor;
    private Database database;
    public DBObjectExecutor(ICustomObject db) {
        this.db = (CustomObject) db;
        this.fileOperation = new FileOperations();
        executor = Executor.Executor();
        database= executor.getDatabase();
    }

    public boolean put(String key, Object value) throws KeyNotFoundException {
        IDatabaseOperation put = new PutOperationDBObject(key,value);

        Boolean res = (boolean)put.execute(this.db);

        String parentChain = "";
        try {
            parentChain = createParentChain(this.db.getParent(), value);
        } catch (Exception e) {
            e.printStackTrace();
        }

        executor.writeToFile(put.toString() + parentChain);
        return res;
    }

    public String createParentChain(String key , Object value) throws Exception {
        Object currentObject = database.get(key);

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

    public Object get(String key) throws KeyNotFoundException {
        return this.db.get(key);
    }

    public int getInt(String key) throws IncompatibleTypeException {
        return this.db.getInt(key);
    }

    public String toString(){
        return this.db.toString();
    }

    public IArray getArray(String key) throws IncompatibleTypeException {
        return new ArrayExecuter(this.db.getArray(key));
    }

    public ICustomObject getObject(String key) throws IncompatibleTypeException {
        return new DBObjectExecutor(this.db.getObject(key));
    }

    public Object remove(String key) throws KeyNotFoundException {
        IDatabaseOperation remove = new RemoveOperationDBObject(key);

        Object value = remove.execute(this.db);

        executor.writeToFile(remove.toString());
        return value;
    }

}
