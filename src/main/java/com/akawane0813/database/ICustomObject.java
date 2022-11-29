package com.akawane0813.database;

import com.akawane0813.exception.IncompatibleTypeException;
import com.akawane0813.exception.KeyNotFoundException;

public interface ICustomObject {
    public Object get(String key) throws KeyNotFoundException;

    public boolean put(String key, Object value) throws KeyNotFoundException;

    public Object remove(String key) throws KeyNotFoundException;


    public ICustomObject getObject(String key) throws IncompatibleTypeException;
}
