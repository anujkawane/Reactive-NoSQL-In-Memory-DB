package com.akawane0813.command;

import com.akawane0813.database.Array;
import com.akawane0813.database.CustomObject;
import com.akawane0813.database.Database;
import com.akawane0813.exception.KeyNotFoundException;

public interface IDatabaseOperation {

    Object execute (Object object) throws KeyNotFoundException;

    Object undo() throws KeyNotFoundException;

    default String operationToString(String operation, Array array, Object value) {
        return convertParemetersToString("ARRAY", operation, value.toString());
    }

    default String operationToString(String operation, CustomObject customObject, Object value) {
        return convertParemetersToString("CustomObject", operation, value.toString());
    }

    default String operationToString(String operation, Database db, Object value) {
        return convertParemetersToString("DB", operation, value.toString());
    }

    default String convertParemetersToString(String operatesOn, String operation, Object value) {
        return ( operatesOn + "#"+operation+"#"+value + "#" );
    }

}
