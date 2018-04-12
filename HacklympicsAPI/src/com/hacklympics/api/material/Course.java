package com.hacklympics.api.material;

import java.util.Map;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hacklympics.api.communication.Response;
import com.hacklympics.api.utility.Utils;

public class Course {
    
    private final int courseID;
    private String name;
    private int semester;
    private String teacher;
    private List<String> students;
    
    public Course(int courseID) {
        this.courseID = courseID;
        
        initCourseData();
    }
    
    public void initCourseData() {
        String uri = String.format("course/%d", courseID);
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
    
    public static Response remove(int courseID) {
        String uri = String.format("course/remove");
        
        JsonObject json = new JsonObject();
        json.addProperty("id", courseID);
        
        return new Response(Utils.post(uri, json.toString()));
    }
    
    
    public int getCourseID() {
        return courseID;
    }
    
    public String getName() {
        return name;
    }
    
    public int getSemester() {
        return semester;
    }
    
    public String getTeacher() {
        return teacher;
    }
    
    public List<String> getStudents() {
        return students;
    }
    
    @Override
    public String toString() {
        return String.format("[%d] %d_%s - %s ", courseID, semester, name, teacher) + students;
    }
    
}
