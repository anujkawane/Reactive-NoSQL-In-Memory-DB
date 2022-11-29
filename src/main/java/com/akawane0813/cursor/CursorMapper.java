package com.akawane0813.cursor;

import java.util.HashMap;

public class CursorMapper {
    private static CursorMapper singleInstance = null;

    private HashMap<String,Cursor> map;

    private CursorMapper() {
        this.map = new HashMap<>();
    }

    public static CursorMapper CursorMapper() {
        if(singleInstance == null) {
            singleInstance = new CursorMapper();
        }
        return singleInstance;
    }

    public boolean put(String key, Cursor cursor) {
        if(this.map.containsKey(key)) {
            map.put(key,cursor);
        }
        map.put(key,cursor);
        return true;
    }

    public boolean remove(String key) {
        if(!this.map.containsKey(key)) {
            map.remove(key);
        }
        return true;
    }

    public Cursor getCursor(String key) {
        if (map.containsKey(key)) {
            return map.get(key);
        }
        return null;
    }

    public void notifyCursor(String key) {
        if (this.map.containsKey(key)) {
            Cursor cursor = this.map.get(key);
            cursor.updateObserver();
        }
    }
}
