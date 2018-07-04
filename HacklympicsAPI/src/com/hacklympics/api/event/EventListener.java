package com.hacklympics.api.event;

public interface EventListener<T extends Event> {
    
    /**
     * Fires the specified Event.
     * @param e the Event to fire.
     */
    public void fireEvent(T e);
    
}