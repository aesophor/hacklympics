package com.hacklympics.api.material;

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

public class Exam {
    
    private ExamData data;
    
    public Exam(int courseID, int examID) {
        initExamData(courseID, examID);
    }
    
    public Exam(int courseID, int examID, String title, String desc, int duration) {
        this.data = new ExamData(courseID, examID, title, desc, duration);
    }
    
    private void initExamData(int courseID, int examID) {
        String uri = String.format("course/%d/exam/%d", courseID, examID);
        Response get = new Response(Utils.get(uri));
        
        if (get.success()) {
            Map<String, Object> json = get.getContent();
            
            this.data = new ExamData(
                    courseID,
                    examID,
                    json.get("title").toString(),
                    json.get("desc").toString(),
                    (int) Double.parseDouble(json.get("duration").toString())
            );
        }
    }
    
    
    public static Response list(int courseID) {
        String uri = String.format("course/%d/exam", courseID);
        return new Response(Utils.get(uri));
    }
    
    public static Response create(int courseID, String title, String desc, int duration) {
        String uri = String.format("course/%d/exam/create", courseID);
        
        JsonObject json = new JsonObject();
        json.addProperty("title", title);
        json.addProperty("desc", desc);
        json.addProperty("duration", Integer.toString(duration));
        
        return new Response(Utils.post(uri, json.toString()));
    }
    
    public Response update(String title, String desc, int duration) {
        String uri = String.format("course/%d/exam/update", this.data.getCourseID());
        
        title = (title != null) ? title : this.data.getTitle();
        desc = (desc != null) ? desc : this.data.getDesc();
        duration = (duration != 0) ? duration : this.data.getDuration();
        
        JsonObject json = new JsonObject();
        json.addProperty("examID", this.data.getExamID());
        json.addProperty("title", title);
        json.addProperty("desc", desc);
        json.addProperty("duration", duration);
        
        return new Response(Utils.post(uri, json.toString()));
    }
    
    public Response remove() {
        String uri = String.format("course/%d/exam/remove", data.getCourseID());
        
        JsonObject json = new JsonObject();
        json.addProperty("examID", data.getExamID());
        
        return new Response(Utils.post(uri, json.toString()));
    }
    
    
    /**
     * Returns all Problems under this Exam.
     * @return a list of Problems.
     */
    public List<Problem> getProblems() {
        List<Problem> problems = new ArrayList<>();
        Response list = Problem.list(getData().getCourseID(), getData().getExamID());
        
        if (list.success()) {
            String raw = Utils.getGson().toJson(list.getContent().get("problems"));
            JsonArray json = Utils.getGson().fromJson(raw, JsonArray.class);
            
            for (JsonElement e: json) {
                int problemID = e.getAsJsonObject().get("id").getAsInt();
                String title = e.getAsJsonObject().get("title").getAsString();
                String desc = e.getAsJsonObject().get("desc").getAsString();
                String input = e.getAsJsonObject().get("input").getAsString();
                String output = e.getAsJsonObject().get("output").getAsString();
                
                problems.add(new Problem(getCourseID(), getExamID(), problemID, title, desc, input, output));
            }
        }
        
        return problems;
    }
    
    
    public ExamData getData() {
        return data;
    }
    
    public Integer getCourseID() {
        return data.getCourseID();
    }
    
    public Integer getExamID() {
        return data.getExamID();
    }
    
    public String getTitle() {
        return data.getTitle();
    }
    
    public String getDesc() {
        return data.getDesc();
    }
    
    public Integer getDuration() {
        return data.getDuration();
    }
    
    
    public SimpleIntegerProperty courseIDProperty() {
        return data.courseIDProperty();
    }
    
    public SimpleIntegerProperty examIDProperty() {
        return data.examIDProperty();
    }
    
    public SimpleStringProperty titleProperty() {
        return data.titleProperty();
    }
    
    public SimpleStringProperty descProperty() {
        return data.descProperty();
    }
    
    public SimpleIntegerProperty durationProperty() {
        return data.durationProperty();
    }
    
    @Override
    public String toString() {
        return data.toString();
    }
    
}
