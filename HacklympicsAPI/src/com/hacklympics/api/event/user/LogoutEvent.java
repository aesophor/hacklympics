package com.hacklympics.api.event.user;

import com.hacklympics.api.event.Event;
import java.util.Map;
import com.hacklympics.api.user.Student;
import com.hacklympics.api.user.Teacher;
import com.hacklympics.api.user.User;

public class LogoutEvent extends Event {
    
    private final User loggedOutUser;
    
    public LogoutEvent(String raw) {
        super(raw);
        
        Map<String, Object> loginEvent = this.getContent();
        
        String username = loginEvent.get("username").toString();
        String fullname = loginEvent.get("fullname").toString();
        int gradYear = (int) Double.parseDouble(loginEvent.get("graduationYear").toString());
        boolean isStudent = Boolean.parseBoolean(loginEvent.get("isStudent").toString());
        
        loggedOutUser = (isStudent) ? new Student(username, fullname, gradYear)
                                   : new Teacher(username, fullname, gradYear);
    }
    
    
    /**
     * Returns the user that just logged out.
     * @return the user that just logged out.
     */
    public User getLoggedOutUser() {
        return loggedOutUser;
    }
            
}