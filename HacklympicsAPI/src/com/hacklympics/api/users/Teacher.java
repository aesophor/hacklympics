package com.hacklympics.api.users;

import com.hacklympics.api.communication.Response;
import com.hacklympics.api.utility.Utils;

public class Teacher extends User {
    
    public Teacher(String username) {
        super(username);
    }
    

    public static Response list() {
        String uri = String.format("user/teachers");
        return new Response(Utils.get(uri));
    }
}
