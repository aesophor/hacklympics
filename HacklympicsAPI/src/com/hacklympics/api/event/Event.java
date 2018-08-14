package com.hacklympics.api.event;

import java.util.Map;
import java.util.HashMap;
import com.google.gson.JsonObject;
import com.hacklympics.api.utility.NetworkUtils;
import java.lang.reflect.InvocationTargetException;

public class Event {
    
    private final EventType eventType;
    private Map<String, Object> content;
    
    public Event(JsonObject json) {
        eventType = EventType.values()[json.get("eventType").getAsInt()];
        
        JsonObject rawContent = json.getAsJsonObject("content");
        content = NetworkUtils.getGson().fromJson(rawContent, HashMap.class);
    }
    
    public Event(String rawJson) {
        this(NetworkUtils.getGson().fromJson(rawJson, JsonObject.class));
    }
    
    
    /**
     * Returns the EventType of this Event.
     * @return the event type.
     */
    public EventType getEventType() {
        return eventType;
    }
    
    /**
     * Returns the content of this Event.
     * @return the event.
     */
    public Map<String, Object> getContent() {
        return content;
    }
    
    
    /**
     * Converts this Event instance to its concrete event type.
     * @return the concrete (effective) event type of this Event.
     */
    public Event toConcreteEvent() {
        Event event = null;
        
        try {
            event = (Event) Class.forName(eventType.toString())
                                 .getConstructor(String.class)
                                 .newInstance(toString());
            
        } catch (InstantiationException
               | IllegalAccessException
               | IllegalArgumentException
               | InvocationTargetException
               | NoSuchMethodException
               | SecurityException
               | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        
        return event;
    }
    
    
    @Override
    public String toString() {
        JsonObject json = new JsonObject();
        json.addProperty("eventType", eventType.ordinal());
        json.add("content", NetworkUtils.getGson().toJsonTree(content));
        return json.toString();
    }
    
}