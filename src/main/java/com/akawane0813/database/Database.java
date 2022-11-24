package com.akawane0813.database;

public interface Database {
    String getString(String key) throws Exception;
    int getInt(String key) throws Exception;
    Array getArray(String key) throws Exception;
    Object getObject(String key) throws Exception;
    java.lang.Object get(String key) throws Exception;
    java.lang.Object remove(String key) throws Exception;
}
