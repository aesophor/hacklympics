package com.hacklympics.api.communication;

import java.util.Map;
import java.util.HashMap;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class Response {
    
    private final StatusCode statusCode;
    private Map<String, Object> content;
    
    public Response(JsonObject json) {
        statusCode = StatusCode.values()[json.get("statusCode").getAsInt()];
        
        if (statusCode == StatusCode.SUCCESS) {
            // Maps the "content" part to a HashMap.
            JsonObject rawContent = json.getAsJsonObject("content");
            content = new Gson().fromJson(rawContent, HashMap.class);
        }
    }
    
    public Response(String rawJson) {
        this(new Gson().fromJson(rawJson, JsonObject.class));
    }
    
    public Response(StatusCode statusCode) {
        this.statusCode = statusCode;
    }
    
    /**
     * Check if the status code of this response is 'SUCCESS'.
     * @return true if the status code is 'SUCCESS'.
     */
    public boolean success() {
        return (statusCode == StatusCode.SUCCESS);
    }
    
    /**
     * Returns the status code of this response.
     * @return the status code.
     */
    public StatusCode getStatusCode() {
        return statusCode;
    }
    
    /**
     * Returns the content of this response.
     * @return the content.
     */
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