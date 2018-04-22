package com.hacklympics.api.materials;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.hacklympics.api.communication.Response;
import com.hacklympics.api.utility.Utils;


public class Problem {
    
    private ProblemData data;

    public Problem(int courseID, int examID, int problemID) {
        initProblemData(courseID, examID, problemID);
    }
    
    public Problem(int courseID, int examID, int problemID, String title, String desc) {
        this.data = new ProblemData(courseID, examID, problemID, title, desc);
    }
    
    private void initProblemData(int courseID, int examID, int problemID) {
        String uri = String.format("course/%d/exam/%d/problem/%d", 
                                   courseID, examID, problemID);
        Response get = new Response(Utils.get(uri));
        
        if (get.success()) {
            Map<String, Object> json = get.getContent();
            
            this.data = new ProblemData(
                    courseID,
                    examID,
                    problemID,
                    json.get("title").toString(),
                    json.get("desc").toString()
            );
        }
    }
    
    
    public static Response list(int courseID, int examID) {
        String uri = String.format("course/%d/exam/%d/problem", courseID, examID);
        return new Response(Utils.get(uri));
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
    
    public Response update(String title, String desc) {
        String uri = String.format("course/%d/exam/%d/problem/update", 
                                   this.data.getCourseID(),
                                   this.data.getExamID());
        
        title = (title != null) ? title : this.data.getTitle();
        desc = (desc != null) ? desc : this.data.getDesc();
        
        JsonObject json = new JsonObject();
        json.addProperty("problemID", this.data.getProblemID());
        json.addProperty("title", title);
        json.addProperty("desc", desc);
        
        return new Response(Utils.post(uri, json.toString()));
    }
    
    public Response remove() {
        String uri = String.format("course/%d/exam/%d/problem/remove",
                                   data.getCourseID(), data.getExamID());
        
        JsonObject json = new JsonObject();
        json.addProperty("problemID", data.getProblemID());
        
        return new Response(Utils.post(uri, json.toString()));
    }
    
    
    public static List<Problem> getProblems(Exam exam) {
        List<Problem> problems = new ArrayList<>();
        Response list = Problem.list(exam.getData().getCourseID(),
                                     exam.getData().getExamID());
        
        if (list.success()) {
            String raw = Utils.getGson().toJson(list.getContent().get("problems"));
            JsonArray json = Utils.getGson().fromJson(raw, JsonArray.class);
            
            for (JsonElement e: json) {
                int problemID = e.getAsJsonObject().get("id").getAsInt();
                String title = e.getAsJsonObject().get("title").getAsString();
                String desc = e.getAsJsonObject().get("desc").getAsString();
                
                problems.add(new Problem(exam.getData().getCourseID(),
                                         exam.getData().getExamID(), 
                                         problemID, 
                                         title,
                                         desc));
            }
        }
        
        return problems;
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
