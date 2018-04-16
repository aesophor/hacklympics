package com.hacklympics.api.user;

import java.util.Map;
import java.util.List;
import com.google.gson.Gson;
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
            
            this.profile = new Profile(
                    username,
                    json.get("fullname").toString(),
                    (int) Double.parseDouble(json.get("graduationYear").toString())
            );
        }
    }
    
    
    public static List<String> list() {
        String uri = String.format("user");
        Response list = new Response(Utils.get(uri));
        
        if (list.success()) {
            Gson gson = new Gson();
            
            String content = list.getContent().toString();
            JsonObject usernames = gson.fromJson(content, JsonObject.class);
            return gson.fromJson(usernames.get("users"), List.class);
        }
        
        return null;
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
    
    public static Response login(String username, String password) {
        String uri = "user/login";
        
        JsonObject json = new JsonObject();
        json.addProperty("username", username);
        json.addProperty("password", Utils.hash(password));
        json.addProperty("loginIP", Utils.getLocalAddress());
        
        return new Response(Utils.post(uri, json.toString()));
    }
    
    public static Response logout(String username) {
        String uri="user/logout";
        
        JsonObject json = new JsonObject();
        json.addProperty("username", username);
        
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
