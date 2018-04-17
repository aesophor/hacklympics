package com.hacklympics.api.user;

import java.util.List;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hacklympics.api.communication.Response;
import com.hacklympics.api.utility.Utils;

public class Teacher extends User {
    
    public Teacher(String username) {
        super(username);
    }
    
    
    public static List<String> list() {
        String uri = String.format("user/teachers");
        Response list = new Response(Utils.get(uri));
        
        if (list.success()) {
            Gson gson = new Gson();
            
            String content = list.getContent().toString();
            JsonObject usernames = gson.fromJson(content, JsonObject.class);
            return gson.fromJson(usernames.get("teachers"), List.class);
        }
        
        return null;
    }
}
