package com.hacklympics.api.event;

public enum EventType {
    
    LOGIN("com.hacklympics.api.event.user.LoginEvent"),                    // 0
    LOGOUT("com.hacklympics.api.event.user.LogoutEvent"),                  // 1
    
    NEW_MESSAGE("com.hacklympics.api.event.message.NewMessageEvent"),      // 2
    
    LAUNCH_EXAM("com.hacklympics.api.event.exam.LaunchExamEvent"),         // 3
    HALT_EXAM("com.hacklympics.api.event.exam.HaltExamEvent"),             // 4 
    ATTEND_EXAM("com.hacklympics.api.event.exam.AttendExamEvent"),         // 5
    LEAVE_EXAM("com.hacklympics.api.event.exam.LeaveExamEvent"),           // 6
    
    NEW_SNAPSHOT("com.hacklympics.api.event.snapshot.NewSnapshotEvent");   // 7
    
    
    private final String classname;
    
    private EventType(String classname) {
        this.classname = classname;
    }
    
    @Override
    public String toString() {
        return classname;
    }
    
}