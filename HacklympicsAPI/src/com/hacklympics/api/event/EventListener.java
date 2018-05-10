package com.hacklympics.api.event;

public interface EventListener<T extends Event> {
    
    public void handle(T e);
    
}