package com.hacklympics.api.event;

public interface EventHandler<T extends Event> {
    
    /**
     * Handles the specified Event. Use an anonymous class and override the
     * handle(T e) to specify exactly what you want to do upon the event's
     * arrival. Or alternatively, use lambda expression for shorter code.
     * @param e the Event to handle.
     */
    public void handle(T e);
    
}