package com.hacklympics.api.event;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

public class EventManager {
    
    private static EventManager eventManager;
    
    private final Map<EventType, List<EventHandler>> handlers;
    
    private EventManager() {
        this.handlers = new HashMap<>();
        
        // List of handlers for events of user login/logout.
        this.handlers.put(EventType.LOGIN, new ArrayList<>());
        this.handlers.put(EventType.LOGOUT, new ArrayList<>());
        
        // List of handlers for events of new messages arrival.
        this.handlers.put(EventType.NEW_MESSAGE, new ArrayList<>());
        
        // List of handlers for exam-related events.
        this.handlers.put(EventType.LAUNCH_EXAM, new ArrayList<>());
        this.handlers.put(EventType.HALT_EXAM, new ArrayList<>());
        this.handlers.put(EventType.ATTEND_EXAM, new ArrayList<>());
        this.handlers.put(EventType.LEAVE_EXAM, new ArrayList<>());
    }
    
    public static EventManager getInstance() {
        if (eventManager == null) {
            eventManager = new EventManager();
        }
        
        return eventManager;
    }
    
    
    /**
     * Adds a EventHandler which subscribes to a specific type of Event.
     * @param eventType the type of the Event which the handler will handle.
     * @param handler the EventHandler to be added.
     */
    public void addEventHandler(EventType eventType, EventHandler handler) {
        this.handlers.get(eventType).add(handler);
    }
    
    /**
     * Fires the specified Events to all of its EventHandlers.
     * @param event the Event to fire.
     */
    public void fireEvent(Event event) {
        EventType eventType = event.getEventType();
        
        for (EventHandler handler : this.handlers.get(eventType)) {
            handler.handle(event);
        }
    }
    
}