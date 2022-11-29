package com.akawane0813.cursor;

import java.util.HashMap;

public class CursorTracker {
    private static CursorTracker cursorTracker;

    private HashMap<String,Cursor> map = new HashMap<>();

    private CursorTracker() {}

    public static CursorTracker getInstance() {
        if(cursorTracker != null) {
            return cursorTracker;
        }
        cursorTracker = new CursorTracker();
        return cursorTracker;
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

}
