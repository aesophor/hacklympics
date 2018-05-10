package com.hacklympics.api.message;

import com.google.gson.JsonObject;
import com.hacklympics.api.communication.Response;
import com.hacklympics.api.user.User;
import com.hacklympics.api.utility.Utils;

public class Message {
    
    private final MessageData data;
    
    public Message(User user, String content) {
        this.data = new MessageData(user, content);
    }
    
    
    public static Response create(String username, String content) {
        String uri = String.format("user/%s/message/create", username);
        
        JsonObject json = new JsonObject();
        json.addProperty("user", username);
        json.addProperty("content", content);
        
        return new Response(Utils.post(uri, json.toString()));
    }
    
    
    public MessageData getData() {
        return data;
    }
    
    public User getUser() {
        return data.getUser();
    }
    
    public String getContent() {
        return data.getContent();
    }
    
    
    @Override
    public String toString() {
        return data.toString();
    }
    
}