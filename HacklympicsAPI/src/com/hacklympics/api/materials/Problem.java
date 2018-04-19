package com.hacklympics.api.materials;

import java.util.Map;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import com.google.gson.JsonObject;
import com.hacklympics.api.communication.Response;
import com.hacklympics.api.utility.Utils;

public class Problem {
    
    private ProblemData data;

    public Problem(int courseID, int examID, int problemID) {
        initProblemData(courseID, examID, problemID);
    }
    
    private void initProblemData(int courseID, int examID, int problemID) {
        String uri = String.format("course/%d/exam/%d/problem/%d", 
                                   courseID, examID, problemID);
        Response response = new Response(Utils.get(uri));
        
        if (response.success()) {
            Map<String, Object> json = response.getContent();
            
            this.data = new ProblemData(
                    courseID,
                    examID,
                    problemID,
                    json.get("title").toString(),
                    json.get("desc").toString()
            );
        }
    }
    
    
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
    
    
    public ProblemData getData() {
        return data;
    }
    
    public SimpleIntegerProperty courseIDProperty() {
        return data.courseIDProperty();
    }
    
    public SimpleIntegerProperty examIDProperty() {
        return data.examIDProperty();
    }
    
    public SimpleIntegerProperty problemIDProperty() {
        return data.problemIDProperty();
    }
    
    public SimpleStringProperty titleProperty() {
        return data.titleProperty();
    }
    
    public SimpleStringProperty descProperty() {
        return data.descProperty();
    }
    
    @Override
    public String toString() {
        return data.toString();
    }
    
}
