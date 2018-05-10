package com.hacklympics.api.event;

import java.util.Map;
import com.google.gson.JsonObject;
import com.hacklympics.api.message.Message;
import com.hacklympics.api.user.User;
import com.hacklympics.api.user.Student;
import com.hacklympics.api.user.Teacher;
import com.hacklympics.api.utility.Utils;


public class NewMessageEvent extends Event {
    
    private final Message message;
    
    public NewMessageEvent(String raw) {
        super(raw);
        
        Map<String, Object> newMessageEvent = this.getContent();
        String msgContent = newMessageEvent.get("content").toString();
                
        String userJson = Utils.getGson().toJson(newMessageEvent.get("user"));
        JsonObject user = Utils.getGson().fromJson(userJson, JsonObject.class);
            
        String username = user.get("username").getAsString();
        String fullname = user.get("fullname").getAsString();
        int gradYear = user.get("graduationYear").getAsInt();
        boolean isStudent = user.get("isStudent").getAsBoolean();
        
        User msgCreator = (isStudent) ? new Student(username, fullname, gradYear)
                                      : new Teacher(username, fullname, gradYear);
        
        this.message = new Message(msgCreator, msgContent);
    }
    
    
    public Message getMessage() {
        return message;
    }
    
}