package com.hacklympics.api.event.message;

import java.util.Map;
import com.google.gson.JsonObject;
import com.hacklympics.api.event.Event;
import com.hacklympics.api.message.Message;
import com.hacklympics.api.session.Session;
import com.hacklympics.api.user.User;
import com.hacklympics.api.user.Student;
import com.hacklympics.api.user.Teacher;
import com.hacklympics.api.utility.NetworkUtils;
import com.hacklympics.api.event.ExamRelated;

public class NewMessageEvent extends Event implements ExamRelated {
    
    private final Message message;
    
    public NewMessageEvent(String rawJson) {
        super(rawJson);
        
        Map<String, Object> content = this.getContent();
        
        
        // Extract the message content (message body).
        String msgContent = content.get("content").toString();
        
        
        // Extract examID from json content.
        int examID = (int) Double.parseDouble(content.get("examID").toString());
        
        
        // Extract user from json content.
        String rawUserJson = NetworkUtils.getGson().toJson(content.get("user"));
        JsonObject userJson = NetworkUtils.getGson().fromJson(rawUserJson, JsonObject.class);
            
        String username = userJson.get("username").getAsString();
        String fullname = userJson.get("fullname").getAsString();
        int gradYear = userJson.get("graduationYear").getAsInt();
        boolean isStudent = userJson.get("isStudent").getAsBoolean();
        
        User msgCreator = (isStudent) ? new Student(username, fullname, gradYear)
                                      : new Teacher(username, fullname, gradYear);
        
        
        // Create a new instance of Message.
        this.message = new Message(msgCreator, examID, msgContent);
    }
    
    
    /**
     * Returns the new message that has just been sent.
     * @return the message.
     */
    public Message getMessage() {
        return message;
    }

    @Override
    public boolean isForCurrentExam() {
        int eventExamID = this.getMessage().getExamID();
        int currentExamID = Session.getInstance().getCurrentExam().getExamID();
        
        return eventExamID == currentExamID;
    }
    
}