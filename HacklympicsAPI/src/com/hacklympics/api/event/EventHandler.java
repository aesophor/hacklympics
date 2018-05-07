package com.hacklympics.api.event;

import com.hacklympics.api.session.Session;

public class EventHandler {
    
    private EventHandler() {
        
    }
    
    
    public static void handle(Event e) {
        EventType eventType = e.getEventType();
        
        switch (eventType) {
            case LOGIN:
                //Session.getInstance().getMainController().updateOnlineUserList();
                break;
                
            case LOGOUT:
                break;
                
            case NEW_MESSAGE:
                break;
                
            default:
                break;
        }
    }
    
}
