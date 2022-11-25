package com.akawane0813.command;

import com.akawane0813.database.Array;
import com.akawane0813.database.CustomObject;
import com.akawane0813.database.Database;
import com.akawane0813.exception.KeyNotFoundException;

public interface DatabaseCommands {

    Object execute (Object object) throws KeyNotFoundException;

    default String operationToString(String operation, Array array, Object value) {
        return convertParemetersToString("ARRAY", operation, value.toString(), array.getParent()+".*index*" + array.length());
    }

    default String operationToString(String operation, CustomObject customObject, Object value) {
        return convertParemetersToString("CustomObject", operation, value.toString(), customObject.getParent());
    }

    default String operationToString(String operation, Database db, Object value, String key) {
        return convertParemetersToString("DB", operation, value.toString(), key);
    }

    default String convertParemetersToString(String operatesOn, String operation, Object value, String parent) {
        return ( operatesOn + "#"+operation+"#"+value + "#" + parent);
    }
}
