package com.akawane0813.database;

import com.akawane0813.exception.IncompatibleTypeException;
import com.akawane0813.exception.KeyNotFoundException;

public interface ICustomObject {
    Object get(String key) throws KeyNotFoundException;

    boolean put(String key, Object value) throws KeyNotFoundException;

    Object remove(String key) throws KeyNotFoundException;

    ICustomObject getObject(String key) throws IncompatibleTypeException;
}
