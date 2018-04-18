package com.hacklympics.api.materials;

import java.util.Map;
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
        Response response = new Response(Utils.get(uri));
        
        if (response.success()) {
            Map<String, Object> json = response.getContent();
            
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
    
    public static Response remove(int courseID, int examID) {
        String uri = String.format("course/%d/exam/remove", courseID);
        
        JsonObject json = new JsonObject();
        json.addProperty("examID", examID);
        
        return new Response(Utils.post(uri, json.toString()));
    }
    
    
    public ExamData getData() {
        return data;
    }
    
    @Override
    public String toString() {
        return data.toString();
    }
    
}
