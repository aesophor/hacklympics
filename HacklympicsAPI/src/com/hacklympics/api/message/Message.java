package com.hacklympics.api.message;

import com.google.gson.JsonObject;
import com.hacklympics.api.communication.Response;
import com.hacklympics.api.user.User;
import com.hacklympics.api.utility.Utils;

public class Message {
    
    private final User user;
    private final int examID;
    private final String content;
    
    public Message(User user, int examID, String content) {
        this.examID = examID;
        this.user = user;
        this.content = content;
    }
    
    
    public static Response create(int courseID, int examID, String username, String content) {
        String uri = String.format("course/%d/exam/%d/snapshot/create", courseID, examID);
        
        JsonObject json = new JsonObject();
        json.addProperty("username", username);
        json.addProperty("content", content);
        
        return new Response(Utils.post(uri, json.toString()));
    }
    
    
    public User getUser() {
        return user;
    }
    
    public int getExamID() {
        return this.examID;
    }
    
    public String getContent() {
        return content;
    }
    
    
    @Override
    public String toString() {
        return String.format("%s: %s", user.getFullname(), content);
    }
    
}