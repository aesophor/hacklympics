package com.hacklympics.api.event;

import java.util.Map;
import com.hacklympics.api.user.User;
import com.hacklympics.api.user.Student;
import com.hacklympics.api.user.Teacher;
import com.hacklympics.api.session.Session;

public class EventHandler {
    
    private EventHandler() {
        
    }
    
    
    public static void handle(Event event) {
        EventType eventType = event.getEventType();
        
        switch (eventType) {
            case LOGIN:
                Map<String, Object> loginEvent = event.getContent();
                Boolean isStudent = Boolean.parseBoolean(loginEvent.get("isStudent").toString());
                
                User loginUser;
                
                if (isStudent) {
                    loginUser = new Student(
                            loginEvent.get("username").toString(),
                            loginEvent.get("fullname").toString(),
                            (int) Double.parseDouble(loginEvent.get("graduationYear").toString())
                    );
                } else {
                    loginUser = new Teacher(
                            loginEvent.get("username").toString(),
                            loginEvent.get("fullname").toString(),
                            (int) Double.parseDouble(loginEvent.get("graduationYear").toString())
                    );
                }
                
                Session.getInstance().getOnlineUsers().add(loginUser);
                break;
                
            case LOGOUT:
                Map<String, Object> logoutEvent = event.getContent();
                Boolean wit = Boolean.parseBoolean(logoutEvent.get("isStudent").toString());
                
                User logoutUser;
                
                if (wit) {
                    logoutUser = new Student(
                            logoutEvent.get("username").toString(),
                            logoutEvent.get("fullname").toString(),
                            (int) Double.parseDouble(logoutEvent.get("graduationYear").toString())
                    );
                } else {
                    logoutUser = new Teacher(
                            logoutEvent.get("username").toString(),
                            logoutEvent.get("fullname").toString(),
                            (int) Double.parseDouble(logoutEvent.get("graduationYear").toString())
                    );
                }
                
                Session.getInstance().getOnlineUsers().remove(logoutUser);
                break;
                
            case NEW_MESSAGE:
                break;
                
            default:
                break;
        }
        
    }
    
}
