package com.hacklympics.api.user;

import java.util.Map;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.hacklympics.api.communication.Response;
import com.hacklympics.api.utility.Utils;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class User {
    
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
    
    public Response logout() {
        String uri = "user/logout";
        
        JsonObject json = new JsonObject();
        json.addProperty("username", getProfile().getUsername());
        
        return new Response(Utils.post(uri, json.toString()));
    }
    
    
    public static List<User> getOnlineUsers() {
        List<User> users = new ArrayList<>();
        Response list = User.listOnline();
        
        if (list.success()) {
            String raw = Utils.getGson().toJson(list.getContent().get("users"));
            JsonArray json = Utils.getGson().fromJson(raw, JsonArray.class);
            
            for (JsonElement e: json) {
                String username = e.getAsJsonObject().get("username").getAsString();
                String fullname = e.getAsJsonObject().get("fullname").getAsString();
                int gradYear = e.getAsJsonObject().get("graduationYear").getAsInt();
                boolean isStudent = e.getAsJsonObject().get("isStudent").getAsBoolean();
                
                if (isStudent) {
                    users.add(new Student(username, fullname, gradYear));
                } else {
                    users.add(new Teacher(username, fullname, gradYear));
                }
            }
        }
        
        return users;
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
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof User)) return false;
        
        User that = (User) o;
        return this.hashCode() == that.hashCode();
    }
    
    @Override
    public int hashCode() {
        int hash = 17;
        hash = 37*hash + Objects.hashCode(getUsername());
        hash = 37*hash + Objects.hashCode(getFullname());
        hash = 37*hash + Objects.hashCode(getGradYear());
        return hash;
    }
    
    @Override
    public String toString() {
        return profile.toString();
    }
    
}
