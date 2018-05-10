package com.hacklympics.api.event;

import java.util.Map;
import java.util.HashMap;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class Event {
    
    private final EventType eventType;
    private Map<String, Object> content;
    
    public Event(JsonObject raw) {
        eventType = EventType.values()[raw.get("eventType").getAsInt()];
        
        JsonObject rawContent = raw.getAsJsonObject("content");
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
    
    
    @Override
    public String toString() {
        JsonObject json = new JsonObject();
        
        json.addProperty("eventType", eventType.ordinal());
        json.add("content", new Gson().toJsonTree(content));
        
        return json.toString();
    }
    
}