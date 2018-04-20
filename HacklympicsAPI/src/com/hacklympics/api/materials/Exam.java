package com.hacklympics.api.materials;

import java.util.Map;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import com.google.gson.JsonObject;
import com.hacklympics.api.communication.Response;
import com.hacklympics.api.utility.Utils;

public class Exam {
    
    private ExamData data;
    
    public Exam(int courseID, int examID) {
        initExamData(courseID, examID);
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
    
    
    public static Response create(String title, String desc, int duration,
                                  int courseID) {
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
    
    public static Response remove(int courseID, int examID) {
        String uri = String.format("course/%d/exam/remove", courseID);
        
        JsonObject json = new JsonObject();
        json.addProperty("examID", examID);
        
        return new Response(Utils.post(uri, json.toString()));
    }
    
    
    public ExamData getData() {
        return data;
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
