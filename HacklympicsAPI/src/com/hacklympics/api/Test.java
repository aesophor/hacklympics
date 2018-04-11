package com.hacklympics.api;

import java.io.IOException;
import com.google.gson.JsonObject;
import com.hacklympics.api.user.Profile;
import com.hacklympics.api.user.Student;
import com.hacklympics.api.user.Teacher;
import com.hacklympics.api.user.User;
import com.hacklympics.api.utility.Utils;

public class Test {
    
    public static String createUser(Profile user) {
        
        JsonObject j = new JsonObject();
        
        j.addProperty("username", user.getUsername());
        //j.addProperty("password", user.getPassword());
        j.addProperty("fullname", user.getFullname());
        j.addProperty("graduation_year", user.getGradYear());
        j.addProperty("is_student", "False");
        
        return j.toString();
    }
    
    public static void main(String[] args) throws IOException {
        //Teacher max = new Teacher("max", "password", "Max");
        //String json = createUser(max);
        //String response = Utils.post("http://127.0.0.1:8000/user/register", json);
        //System.out.println(response);
        
        //Teacher max = new Teacher("max");
        //System.out.println(max);
        
        
        Student andrey = new Student("andrey");
        System.out.println(andrey);
        
        System.out.println("Logging in as andrey...");
        System.out.println(andrey.login("hello").success());
        
        //System.out.println("Registering Tim...");
        //System.out.println(User.register("tim", "timmy", "Tim", 108).success());
        
        //System.out.println("Logging out as andrey...");
        //System.out.println(andrey.logout().success());
    } 
    
}
