package com.hacklympics.api.users;

import java.util.Map;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hacklympics.api.communication.Response;
import com.hacklympics.api.utility.Utils;

public abstract class User {
    
    protected static final Gson GSON = new Gson();
    private UserProfile profile;
    
    public User(String username) {
        initProfile(username);
    }
    
    public User(String username, String fullname, int gradYear) {
        this.profile = new UserProfile(username, fullname, gradYear);
    }
    
    private void initProfile(String username) {
        String uri = String.format("user/%s", username);
        Response response = new Response(Utils.get(uri));
        
        if (response.success()) {
            Map<String, Object> json = response.getContent();
            
            this.profile = new UserProfile(
                    username,
                    json.get("fullname").toString(),
                    (int) Double.parseDouble(json.get("graduationYear").toString())
            );
        }
    }
    
    
    public static Response list() {
        String uri = String.format("user");
        return new Response(Utils.get(uri));
    }
    
    public static Response listOnline() {
        String uri = String.format("user/online");
        return new Response(Utils.get(uri));
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
        String uri = "user/logout";
        
        JsonObject json = new JsonObject();
        json.addProperty("username", username);
        
        return new Response(Utils.post(uri, json.toString()));
    }
    
    
    public UserProfile getProfile() {
        return profile;
    }
    
    public String getUsername() {
        return profile.getUsername();
    }
    
    public String getFullname() {
        return profile.getFullname();
    }
    
    public Integer getGradYear() {
        return profile.getGradYear();
    }
    
    public SimpleStringProperty usernameProperty() {
        return profile.usernameProperty();
    }
    
    public SimpleStringProperty fullnameProperty() {
        return profile.fullnameProperty();
    }
    
    public SimpleIntegerProperty gradYearProperty() {
        return profile.gradYearProperty();
    }
    
    @Override
    public String toString() {
        return String.format("%s (%s)", profile.getUsername(), profile.getFullname());
    }
    
}
