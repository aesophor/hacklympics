package com.hacklympics.api.user;

import java.util.Map;
import com.google.gson.JsonObject;
import com.hacklympics.api.communication.Response;
import com.hacklympics.api.utility.Utils;

public abstract class User {
    
    private Profile profile;
    
    public User(String username) {
        initProfile(username);
    }
    
    private void initProfile(String username) {
        String uri = String.format("user/%s", username);
        Response response = new Response(Utils.get(uri));
        
        if (response.success()) {
            Map<String, Object> json = response.getContent();
            this.profile = new Profile(username,
                                       json.get("fullname").toString(),
                                       json.get("graduationYear").toString());
        }
    }
    
    
    public static Response register(String username, String password,
                                    String fullname, int graduationYear) {
        String uri = String.format("user/register");
        
        JsonObject json = new JsonObject();
        json.addProperty("username", username);
        json.addProperty("password", password);
        json.addProperty("fullname", fullname);
        json.addProperty("graduationYear", graduationYear);
        json.addProperty("isStudent", true);
        
        return new Response(Utils.post(uri, json.toString()));
    }
    
    public Response login(String password) {
        String uri = "user/login";
        
        JsonObject json = new JsonObject();
        json.addProperty("username", this.profile.getUsername());
        json.addProperty("password", Utils.hash(password));
        json.addProperty("loginIP", Utils.getLocalAddress());
        
        return new Response(Utils.post(uri, json.toString()));
    }
    
    public Response logout() {
        String uri="user/logout";
        
        JsonObject json = new JsonObject();
        json.addProperty("username", this.profile.getUsername());
        
        return new Response(Utils.post(uri, json.toString()));
    }
    
    
    public Profile getProfile() {
        return profile;
    }
    
    @Override
    public String toString() {
        return profile.toString();
    }
    
}
