package com.hacklympics.api.message;

import com.hacklympics.api.user.User;

public class MessageData {
    
    private final User user;
    private final String content;
    
    public MessageData(User user, String content) {
        this.user = user;
        this.content = content;
    }
    
    
    public User getUser() {
        return user;
    }
    
    public String getContent() {
        return content;
    }
    
    
    @Override
    public String toString() {
        return String.format("%s: %s", user.getFullname(), content);
    }
    
}