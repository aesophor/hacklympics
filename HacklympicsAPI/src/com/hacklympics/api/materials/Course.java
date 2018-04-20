package com.hacklympics.api.materials;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hacklympics.api.communication.Response;
import com.hacklympics.api.utility.Utils;

public class Course {
    
    private CourseData data;
    
    public Course(int courseID) {
        initCourseData(courseID);
    }
    
    public Course(int courseID, String name, int semester, String teacher,
                  List<String> students) {
        this.data = new CourseData(courseID, name, semester, teacher, students);
    }
    
    public void initCourseData(int courseID) {
        String uri = String.format("course/%d", courseID);
        Response get = new Response(Utils.get(uri));
        
        if (get.success()) {
            Map<String, Object> json = get.getContent();
            
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
    
    public Response update(String name, int semester, String teacher, 
                           List<String> students) {
        String uri = String.format("course/update");
        
        name = (name != null) ? name : this.data.getName();
        semester = (semester != 0) ? semester : this.data.getSemester();
        teacher = (teacher != null) ? teacher : this.data.getTeacher();
        students = (students != null) ? students : this.data.getStudents();
        
        JsonArray s = new JsonArray();
        for (String student : students) {
            s.add(student);
        }
        
        JsonObject json = new JsonObject();
        json.addProperty("id", this.data.getCourseID());
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
    
    
    public static List<Course> getCourses() {
        List<Course> courses = new ArrayList<>();
        Response list = Course.list();
        
        if (list.success()) {
            String raw = Utils.getGson().toJson(list.getContent().get("courses"));
            JsonArray json = Utils.getGson().fromJson(raw, JsonArray.class);
            
            for (JsonElement e: json) {
                int courseID = e.getAsJsonObject().get("id").getAsInt();
                String name = e.getAsJsonObject().get("name").getAsString();
                int semester = e.getAsJsonObject().get("semester").getAsInt();
                String teacher = e.getAsJsonObject().get("teacher").getAsString();
                List<String> students = Utils.getGson().fromJson(e.getAsJsonObject().get("students"), 
                                                                 List.class);
                
                courses.add(new Course(courseID, name, semester, teacher, students));
            }
        }
        
        return courses;
    }
    
    
    public CourseData getData() {
        return data;
    }
    
    public SimpleIntegerProperty courseIDProperty() {
        return data.courseIDProperty();
    }
    
    public SimpleStringProperty nameProperty() {
        return data.nameProperty();
    }
    
    public SimpleIntegerProperty semesterProperty() {
        return data.semesterProperty();
    }
    
    public SimpleStringProperty teacherProperty() {
        return data.teacherProperty();
    }
    
    @Override
    public String toString() {
        return data.toString();
    }
    
}
