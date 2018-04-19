package com.hacklympics.api.users;

import com.hacklympics.api.communication.Response;
import com.hacklympics.api.utility.Utils;

public class Student extends User {
    
    public Student(String username) {
        super(username);
    }
    
    public Student(String username, String fullname, int gradYear) {
        super(username, fullname, gradYear);
    }
    
}
