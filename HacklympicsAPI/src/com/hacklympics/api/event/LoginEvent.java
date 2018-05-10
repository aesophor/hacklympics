package com.hacklympics.api.event;

import java.util.Map;
import com.hacklympics.api.user.Student;
import com.hacklympics.api.user.Teacher;
import com.hacklympics.api.user.User;

public class LoginEvent extends Event {
    
    private final User loggedInUser;
    
    public LoginEvent(String raw) {
        super(raw);
        
        Map<String, Object> loginEvent = this.getContent();
        
        String username = loginEvent.get("username").toString();
        String fullname = loginEvent.get("fullname").toString();
        int gradYear = (int) Double.parseDouble(loginEvent.get("graduationYear").toString());
        boolean isStudent = Boolean.parseBoolean(loginEvent.get("isStudent").toString());
        
        loggedInUser = (isStudent) ? new Student(username, fullname, gradYear)
                                   : new Teacher(username, fullname, gradYear);
    }
    
    
    public User getLoggedInUser() {
        return loggedInUser;
    }
    
}