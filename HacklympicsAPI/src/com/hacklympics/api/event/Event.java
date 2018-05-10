package com.hacklympics.api.event;

import java.util.Map;
import java.util.HashMap;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.lang.reflect.InvocationTargetException;

public class Event {
    
    private final EventType eventType;
    private Map<String, Object> content;
    
    public Event(JsonObject json) {
        eventType = EventType.values()[json.get("eventType").getAsInt()];
        
        JsonObject rawContent = json.getAsJsonObject("content");
        content = new Gson().fromJson(rawContent, HashMap.class);
    }
    
    public Event(String raw) {
        this(new Gson().fromJson(raw, JsonObject.class));
    }
    
    
    public EventType getEventType() {
        return eventType;
    }
    
    public Map<String, Object> getContent() {
        return content;
    }
    
    
    public Event toCorrectForm() {
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
        }
        
        return event;
    }
    
    @Override
    public String toString() {
        JsonObject json = new JsonObject();
        
        json.addProperty("eventType", eventType.ordinal());
        json.add("content", new Gson().toJsonTree(content));
        
        return json.toString();
    }
    
}