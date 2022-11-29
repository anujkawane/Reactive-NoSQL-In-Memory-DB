package com.akawane0813.database;

import com.akawane0813.exception.IncompatibleTypeException;
import com.akawane0813.exception.KeyNotFoundException;

public interface IArray {

    public boolean put(Object object) throws KeyNotFoundException;

    public Object get(int index);

    public String getString(int index) throws IncompatibleTypeException;

    public int getInt(int index) throws IncompatibleTypeException;

    public IArray getArray(int index) throws IncompatibleTypeException;

    public ICustomObject getObject(int index) throws IncompatibleTypeException;

    public Object remove(int index) throws Exception;
}