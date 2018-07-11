package com.hacklympics.api.message;

import com.google.gson.JsonObject;
import com.hacklympics.api.communication.Response;
import com.hacklympics.api.user.User;
import com.hacklympics.api.utility.Utils;

public class Message {
    
    private final User user;
    private final String body;
    
    public Message(User user, String content) {
        this.user = user;
        this.body = content;
    }
    
    
    public static Response create(String username, String content) {
        String uri = String.format("user/%s/message/create", username);
        
        JsonObject json = new JsonObject();
        json.addProperty("user", username);
        json.addProperty("content", content);
        
        return new Response(Utils.post(uri, json.toString()));
    }
    
    
    public User getUser() {
        return user;
    }
    
    public String getContent() {
        return body;
    }
    
    
    @Override
    public String toString() {
        return String.format("%s: %s", user.getFullname(), body);
    }
    
}