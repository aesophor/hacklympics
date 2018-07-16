package com.hacklympics.api.user;

import java.util.List;
import java.util.ArrayList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.hacklympics.api.communication.Response;
import com.hacklympics.api.material.Course;
import com.hacklympics.api.utility.NetworkUtils;

public class Student extends User {
    
    public Student(String username) {
        super(username);
    }
    
    public Student(String username, String fullname, int gradYear) {
        super(username, fullname, gradYear);
    }
    
    
    public static List<Student> getStudents() {
        List<Student> students = new ArrayList<>();
        Response list = User.list();
        
        if (list.success()) {
            String raw = NetworkUtils.getGson().toJson(list.getContent().get("users"));
            JsonArray json = NetworkUtils.getGson().fromJson(raw, JsonArray.class);
            
            for (JsonElement e: json) {
                String username = e.getAsJsonObject().get("username").getAsString();
                String fullname = e.getAsJsonObject().get("fullname").getAsString();
                int gradYear = e.getAsJsonObject().get("graduationYear").getAsInt();
                boolean isStudent = e.getAsJsonObject().get("isStudent").getAsBoolean();
                
                if (isStudent) {
                    students.add(new Student(username, fullname, gradYear));
                }
            }
        }
        
        return students;
    }
    
    public List<Course> getCourses() {
        List<Course> courses = new ArrayList<>();
        Response list = Course.list();
        
        if (list.success()) {
            String raw = NetworkUtils.getGson().toJson(list.getContent().get("courses"));
            JsonArray json = NetworkUtils.getGson().fromJson(raw, JsonArray.class);
            
            String username = getProfile().getUsername();
            
            for (JsonElement e: json) {
                int courseID = e.getAsJsonObject().get("id").getAsInt();
                String name = e.getAsJsonObject().get("name").getAsString();
                int semester = e.getAsJsonObject().get("semester").getAsInt();
                String teacher = e.getAsJsonObject().get("teacher").getAsString();
                List<String> students = NetworkUtils.getGson().fromJson(e.getAsJsonObject().get("students"), List.class);
                
                if (students.contains(username)) {
                    courses.add(new Course(courseID, name, semester, teacher, students));
                }
            }
        }
        
        return courses;
    }
    
}
