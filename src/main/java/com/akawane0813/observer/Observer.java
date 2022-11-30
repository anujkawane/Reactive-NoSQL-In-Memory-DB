package com.akawane0813.observer;

import java.util.ArrayList;
import java.util.List;

public class Observer implements IObserver{
    private List<String> logs= new ArrayList<>();
    public Observer() {

    }
    public void update(String message) {
        logs.add(message);
    }

    public List<String> getUpdates(){
        return logs;
    }
}
