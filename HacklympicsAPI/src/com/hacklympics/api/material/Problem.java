package com.hacklympics.api.material;

import java.util.Map;
import com.google.gson.JsonObject;
import com.hacklympics.api.communication.Response;
import com.hacklympics.api.utility.Utils;

public class Problem {
    
    private final int courseID;
    private final int examID;
    private final int problemID;
    private String title;
    private String desc;

    public Problem(int courseID, int examID, int problemID) {
        this.courseID = courseID;
        this.examID = examID;
        this.problemID = problemID;
        
        initProblemData();
    }
    
    private void initProblemData() {
        String uri = String.format("course/%d/exam/%d/problem/%d", 
                                   courseID, examID, problemID);
        Response response = new Response(Utils.get(uri));
        
        if (response.success()) {
            Map<String, Object> json = response.getContent();
            this.title = json.get("title").toString();
            this.desc = json.get("desc").toString();
        }
    }
    
    
    // Shouldn't it return a Problem instance after creation (?)
    public static Response create(String title, String desc, 
                                  int courseID, int examID) {
        String uri = String.format("course/%d/exam/%d/problem/create", 
                                   courseID, examID);
        
        JsonObject json = new JsonObject();
        json.addProperty("title", title);
        json.addProperty("desc", desc);
        
        return new Response(Utils.post(uri, json.toString()));
    }
    
    public static Response remove(int courseID, int examID, int problemID) {
        String uri = String.format("course/%d/exam/%d/problem/remove",
                                   courseID, examID);
        
        JsonObject json = new JsonObject();
        json.addProperty("problemID", problemID);
        
        return new Response(Utils.post(uri, json.toString()));
    }
    
    
    public int getCourseID() {
        return courseID;
    }
    
    public int getExamID() {
        return examID;
    }
    
    public int getProblemID() {
        return problemID;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getDesc() {
        return desc;
    }
    
    @Override
    public String toString() {
        return String.format("");
    }
    
}
