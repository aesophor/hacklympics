package com.hacklympics.api.event;

public enum EventType {
    
    LOGIN("com.hacklympics.api.event.LoginEvent"),                // 0
    LOGOUT("com.hacklympics.api.event.LogoutEvent"),              // 1
    NEW_MESSAGE("com.hacklympics.api.event.NewMessageEvent");     // 2
    
    
    private final String classname;
    
    private EventType(String classname) {
        this.classname = classname;
    }
    
    @Override
    public String toString() {
        return classname;
    }
    
}