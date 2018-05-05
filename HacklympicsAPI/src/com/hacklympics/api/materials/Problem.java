package com.hacklympics.api.materials;

import java.util.Map;
import javafx.beans.property.SimpleStringProperty;
import com.google.gson.JsonObject;
import com.hacklympics.api.communication.Response;
import com.hacklympics.api.utility.Utils;

public class Problem {
    
    private ProblemData data;

    public Problem(int courseID, int examID, int problemID) {
        initProblemData(courseID, examID, problemID);
    }
    
    public Problem(int courseID, int examID, int problemID, 
            String title, String desc, String input, String output) {
        
        this.data = new ProblemData(courseID, examID, problemID, title, desc, input, output);
    }
    
    private void initProblemData(int courseID, int examID, int problemID) {
        String uri = String.format("course/%d/exam/%d/problem/%d", courseID, examID, problemID);
        
        Response get = new Response(Utils.get(uri));
        
        if (get.success()) {
            Map<String, Object> json = get.getContent();
            
            this.data = new ProblemData(
                    courseID,
                    examID,
                    problemID,
                    json.get("title").toString(),
                    json.get("desc").toString(),
                    json.get("input").toString(),
                    json.get("output").toString()
            );
        }
    }
    
    
    public static Response list(int courseID, int examID) {
        String uri = String.format("course/%d/exam/%d/problem", courseID, examID);
        return new Response(Utils.get(uri));
    }
    
    public static Response create(int courseID, int examID, 
            String title, String desc, String input, String output) {
        
        String uri = String.format("course/%d/exam/%d/problem/create", courseID, examID);
        
        JsonObject json = new JsonObject();
        json.addProperty("title", title);
        json.addProperty("desc", desc);
        json.addProperty("input", input);
        json.addProperty("output", output);
        
        return new Response(Utils.post(uri, json.toString()));
    }
    
    public Response update(String title, String desc, String input, String output) {
        String uri = String.format("course/%d/exam/%d/problem/update", getCourseID(), getExamID());
        
        title = (title != null) ? title : this.data.getTitle();
        desc = (desc != null) ? desc : this.data.getDesc();
        
        JsonObject json = new JsonObject();
        json.addProperty("problemID", this.data.getProblemID());
        json.addProperty("title", title);
        json.addProperty("desc", desc);
        json.addProperty("input", input);
        json.addProperty("output", output);
        
        return new Response(Utils.post(uri, json.toString()));
    }
    
    public Response remove() {
        String uri = String.format("course/%d/exam/%d/problem/remove", getCourseID(), getExamID());
        
        JsonObject json = new JsonObject();
        json.addProperty("problemID", data.getProblemID());
        
        return new Response(Utils.post(uri, json.toString()));
    }
    
    
    public ProblemData getData() {
        return data;
    }
    
    public Integer getCourseID() {
        return data.getCourseID();
    }
    
    public Integer getExamID() {
        return data.getExamID();
    }
    
    public Integer getProblemID() {
        return data.getProblemID();
    }
    
    public String getTitle() {
        return data.getTitle();
    }
    
    public String getDesc() {
        return data.getDesc();
    }
    
    public String getInput() {
        return data.getInput();
    }
    
    public String getOutput() {
        return data.getOutput();
    }
    
    
    public SimpleStringProperty titleProperty() {
        return data.titleProperty();
    }
    
    public SimpleStringProperty descProperty() {
        return data.descProperty();
    }
    
    public SimpleStringProperty inputProperty() {
        return data.inputProperty();
    }
    
    public SimpleStringProperty outputProperty() {
        return data.outputProperty();
    }
    
    @Override
    public String toString() {
        return data.toString();
    }
    
}
