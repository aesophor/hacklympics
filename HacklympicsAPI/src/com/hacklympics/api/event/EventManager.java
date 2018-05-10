package com.hacklympics.api.event;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

public class EventManager {
    
    public static EventManager eventManager;
    
    private final Map<EventType, List<EventListener<Event>>> listeners;
    
    private EventManager() {
        this.listeners = new HashMap<>();
        
        this.listeners.put(EventType.LOGIN, new ArrayList<>());
        this.listeners.put(EventType.LOGOUT, new ArrayList<>());
        this.listeners.put(EventType.NEW_MESSAGE, new ArrayList<>());
    }
    
    public static EventManager getInstance() {
        if (eventManager == null) {
            eventManager = new EventManager();
        }
        
        return eventManager;
    }
    
    
    /**
     * Add a EventListener that subscribes to a specific type of Event.
     * @param eventType the type of the Event to listen for.
     * @param listener the EventListener to be added.
     */
    public void addEventListener(EventType eventType, EventListener listener) {
        this.listeners.get(eventType).add(listener);
    }
    
    /**
     * Fire a Event to all of its EventListeners.
     * @param event the Event to be dispatched.
     */
    public void fireEvent(Event event) {
        EventType eventType = event.getEventType();
        
        for (EventListener<Event> listener : this.listeners.get(eventType)) {
            listener.handle(event);
        }
    }
    
}