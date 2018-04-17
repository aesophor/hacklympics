package com.hacklympics.api.user;

import com.hacklympics.api.communication.Response;
import com.hacklympics.api.utility.Utils;

public class Student extends User {
    
    public Student(String username) {
        super(username);
    }
    
    
    public static Response list() {
        String uri = String.format("user/students");
        return new Response(Utils.get(uri));
    }
}
