package com.hacklympics.api.materials;

import java.util.Map;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import com.google.gson.JsonObject;
import com.hacklympics.api.communication.Response;
import com.hacklympics.api.utility.Utils;


public class Problem {
    
    private ProblemData problemData;

    public Problem(int courseID, int examID, int problemID) {
        initProblemData(courseID, examID, problemID);
    }
    
    public Problem(int courseID, int examID, int problemID,
                   String title, String desc, String input, String output) {
        this.problemData = new ProblemData(courseID, examID, problemID,
                                           title, desc, input, output);
    }
    
    private void initProblemData(int courseID, int examID, int problemID) {
        String uri = String.format("course/%d/exam/%d/problem/%d", 
                                   courseID, examID, problemID);
        Response get = new Response(Utils.get(uri));
        
        if (get.success()) {
            Map<String, Object> json = get.getContent();
            
            this.problemData = new ProblemData(
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
        String uri = String.format("course/%d/exam/%d/problem/create", 
                                   courseID, examID);
        
        JsonObject json = new JsonObject();
        json.addProperty("title", title);
        json.addProperty("desc", desc);
        json.addProperty("input", input);
        json.addProperty("output", output);
        
        return new Response(Utils.post(uri, json.toString()));
    }
    
    public Response update(String title, String desc, String input, String output) {
        String uri = String.format("course/%d/exam/%d/problem/update", 
                                   this.problemData.getCourseID(),
                                   this.problemData.getExamID());
        
        title = (title != null) ? title : this.problemData.getTitle();
        desc = (desc != null) ? desc : this.problemData.getDesc();
        
        JsonObject json = new JsonObject();
        json.addProperty("problemID", this.problemData.getProblemID());
        json.addProperty("title", title);
        json.addProperty("desc", desc);
        json.addProperty("input", input);
        json.addProperty("output", output);
        
        return new Response(Utils.post(uri, json.toString()));
    }
    
    public Response remove() {
        String uri = String.format("course/%d/exam/%d/problem/remove",
                                   problemData.getCourseID(), problemData.getExamID());
        
        JsonObject json = new JsonObject();
        json.addProperty("problemID", problemData.getProblemID());
        
        return new Response(Utils.post(uri, json.toString()));
    }
    
    
    public ProblemData getData() {
        return problemData;
    }
    
    public Integer getCourseID() {
        return problemData.getCourseID();
    }
    
    public Integer getExamID() {
        return problemData.getExamID();
    }
    
    public Integer getProblemID() {
        return problemData.getProblemID();
    }
    
    public String getTitle() {
        return problemData.getTitle();
    }
    
    public String getDesc() {
        return problemData.getDesc();
    }
    
    public String getInput() {
        return problemData.getInput();
    }
    
    public String getOutput() {
        return problemData.getOutput();
    }
    
    
    public SimpleIntegerProperty courseIDProperty() {
        return problemData.courseIDProperty();
    }
    
    public SimpleIntegerProperty examIDProperty() {
        return problemData.examIDProperty();
    }
    
    public SimpleIntegerProperty problemIDProperty() {
        return problemData.problemIDProperty();
    }
    
    public SimpleStringProperty titleProperty() {
        return problemData.titleProperty();
    }
    
    public SimpleStringProperty descProperty() {
        return problemData.descProperty();
    }
    
    public SimpleStringProperty inputProperty() {
        return problemData.inputProperty();
    }
    
    public SimpleStringProperty outputProperty() {
        return problemData.outputProperty();
    }
    
    @Override
    public String toString() {
        return problemData.toString();
    }
    
}
