package com.hacklympics.api.material;

import java.util.Map;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hacklympics.api.communication.Response;
import com.hacklympics.api.utility.Utils;

public class Course {
    
    private final int id;
    private String name;
    private int semester;
    private String teacher;
    private List<String> students;
    
    public Course(int id) {
        this.id = id;
        initCourseData();
    }
    
    public void initCourseData() {
        String uri = String.format("course/%d", id);
        Response response = new Response(Utils.get(uri));
        
        if (response.success()) {
            Map<String, Object> json = response.getContent();
            this.name = json.get("name").toString();
            this.semester = (int) Double.parseDouble(json.get("semester").toString());
            this.teacher = json.get("teacher").toString();
            this.students = new Gson().fromJson(json.get("students").toString(), List.class);
        }
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
    
    @Override
    public String toString() {
        return String.format("[%d] %d_%s - %s ", id, semester, name, teacher) + students;
    }
    
}
