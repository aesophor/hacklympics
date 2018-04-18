package com.hacklympics.api.materials;

import java.util.Map;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hacklympics.api.communication.Response;
import com.hacklympics.api.utility.Utils;

public class Course {
    
    private CourseData data;
    
    public Course(int courseID) {
        initCourseData(courseID);
    }
    
    public void initCourseData(int courseID) {
        String uri = String.format("course/%d", courseID);
        Response response = new Response(Utils.get(uri));
        
        if (response.success()) {
            Map<String, Object> json = response.getContent();
            
            this.data = new CourseData(
                    courseID,
                    json.get("name").toString(),
                    (int) Double.parseDouble(json.get("semester").toString()),
                    json.get("teacher").toString(),
                    new Gson().fromJson(json.get("students").toString(), List.class)
            );
        }
    }
    
    
    public static Response list() {
        String uri = String.format("course");
        return new Response(Utils.get(uri));
    }
    
    public static Response create(String name, int semester, String teacher,
                                  List<String> students) {
        String uri = String.format("course/create");
        
        JsonArray s = new JsonArray();
        for (String student : students) {
            s.add(student);
        }
        
        JsonObject json = new JsonObject();
        json.addProperty("name", name);
        json.addProperty("semester", semester);
        json.addProperty("teacher", teacher);
        json.add("students", s);
        
        return new Response(Utils.post(uri, json.toString()));
    }
    
    public static Response remove(int courseID) {
        String uri = String.format("course/remove");
        
        JsonObject json = new JsonObject();
        json.addProperty("id", courseID);
        
        return new Response(Utils.post(uri, json.toString()));
    }
    
    
    public CourseData getData() {
        return data;
    }
    
    @Override
    public String toString() {
        return data.toString();
    }
    
}
