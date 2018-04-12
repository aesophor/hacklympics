package com.hacklympics.api.material;

import java.util.Map;
import com.google.gson.JsonObject;
import com.hacklympics.api.communication.Response;
import com.hacklympics.api.utility.Utils;

public class Exam {
    
    private final int courseID;
    private final int examID;
    private String title;
    private String desc;
    private int duration;
    
    public Exam(int courseID, int examID) {
        this.courseID = courseID;
        this.examID = examID;
        
        initExamData();
    }
    
    private void initExamData() {
        String uri = String.format("course/%d/exam/%d", courseID, examID);
        Response response = new Response(Utils.get(uri));
        
        if (response.success()) {
            Map<String, Object> json = response.getContent();
            this.title = json.get("title").toString();
            this.desc = json.get("desc").toString();
            this.duration = (int) Double.parseDouble(json.get("duration").toString());
        }
    }
    
    
    public static Response create(String title, String desc, int duration,
                                  Course course) {
        String uri = String.format("course/%d/exam/create", course.getCourseID());
        
        JsonObject json = new JsonObject();
        json.addProperty("title", title);
        json.addProperty("desc", desc);
        json.addProperty("duration", Integer.toString(duration));
        
        return new Response(Utils.post(uri, json.toString()));
    }
    
    public static Response remove(Course course, int examID) {
        String uri = String.format("course/%d/exam/remove", course.getCourseID());
        
        JsonObject json = new JsonObject();
        json.addProperty("examID", examID);
        
        return new Response(Utils.post(uri, json.toString()));
    }
    
    
    public int getCourseID() {
        return courseID;
    }
    
    public int getExamID() {
        return examID;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getDesc() {
        return desc;
    }
    
    public int getDuration() {
        return duration;
    }
    
    @Override
    public String toString() {
        return String.format("");
    }
    
}
