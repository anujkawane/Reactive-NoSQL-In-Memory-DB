package com.akawane0813.observer;

public class Observer implements IObserver{
    public Observer() {

    }

    public void update(String message) {
        System.out.println(message);
    }
}
