package com.akawane0813.database;

import com.akawane0813.exception.IncompatibleTypeException;
import com.akawane0813.exception.KeyNotFoundException;

public interface IArray {

    boolean put(Object object) throws KeyNotFoundException;

    Object get(int index);

    String getString(int index) throws IncompatibleTypeException;

    int getInt(int index) throws IncompatibleTypeException;

    IArray getArray(int index) throws IncompatibleTypeException;

    ICustomObject getObject(int index) throws IncompatibleTypeException;

    Object remove(int index) throws Exception;
}