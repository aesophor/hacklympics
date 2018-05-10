package com.hacklympics.api.communication;

import java.util.Map;
import java.util.HashMap;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class Response {
    
    private final StatusCode statusCode;
    private Map<String, Object> content;
    
    public Response(JsonObject raw) {
        statusCode = StatusCode.values()[raw.get("statusCode").getAsInt()];
        
        if (statusCode == StatusCode.SUCCESS) {
            JsonObject rawContent = raw.getAsJsonObject("content");
            content = new Gson().fromJson(rawContent, HashMap.class);
        }
    }
    
    public Response(String raw) {
        this(new Gson().fromJson(raw, JsonObject.class));
    }
    
    public Response(StatusCode statusCode) {
        this.statusCode = statusCode;
    }
    
    
    public boolean success() {
        return (statusCode == StatusCode.SUCCESS);
    }
    
    public StatusCode getStatusCode() {
        return statusCode;
    }
    
    public Map<String, Object> getContent() {
        return content;
    }
    
    
    @Override
    public String toString() {
        JsonObject json = new JsonObject();
        json.addProperty("statusCode", statusCode.ordinal());
        json.add("content", new Gson().toJsonTree(content));
        return json.toString();
    }
    
}