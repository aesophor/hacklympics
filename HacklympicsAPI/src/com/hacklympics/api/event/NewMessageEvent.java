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
    
    public NewMessageEvent(String rawJson) {
        super(rawJson);
        
        Map<String, Object> newMessageEvent = this.getContent();
        String msgContent = newMessageEvent.get("content").toString();
        
        String rawUserJson = Utils.getGson().toJson(newMessageEvent.get("user"));
        JsonObject userJson = Utils.getGson().fromJson(rawUserJson, JsonObject.class);
            
        String username = userJson.get("username").getAsString();
        String fullname = userJson.get("fullname").getAsString();
        int gradYear = userJson.get("graduationYear").getAsInt();
        boolean isStudent = userJson.get("isStudent").getAsBoolean();
        
        User msgCreator = (isStudent) ? new Student(username, fullname, gradYear)
                                      : new Teacher(username, fullname, gradYear);
        
        this.message = new Message(msgCreator, msgContent);
    }
    
    
    /**
     * Returns the new message that has just been sent.
     * @return the message.
     */
    public Message getMessage() {
        return message;
    }
    
}