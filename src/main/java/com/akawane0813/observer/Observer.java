package com.akawane0813.observer;

import java.util.List;

public class Observer implements IObserver{
    private List<String> updateMessages;
    public Observer() {

    }
    public void update(String message) {
        updateMessages.add(message);
    }

    public List<String> getUpdates(){
        return updateMessages;
    }
}
