package com.hacklympics.api.material;

import java.util.Map;
import javafx.beans.property.SimpleStringProperty;
import com.google.gson.JsonObject;
import com.hacklympics.api.communication.Response;
import com.hacklympics.api.utility.NetworkUtils;

public class Answer {
    
    private AnswerData data;
    
    public Answer(int courseID, int examID, int problemID, int answerID) {
        initAnswerData(courseID, examID, problemID, answerID);
    }
    
    private void initAnswerData(int courseID, int examID, int problemID, int answerID) {
        String uri = String.format("course/%d/exam/%d/problem/%d/answer/%d",
                courseID, examID, problemID, answerID);
        
        Response get = new Response(NetworkUtils.get(uri));
        
        if (get.success()) {
            Map<String, Object> json = get.getContent();
            
            this.data = new AnswerData(
                    courseID,
                    examID,
                    problemID,
                    answerID,
                    json.get("className").toString(),
                    json.get("sourceCode").toString(),
                    json.get("student").toString()
            );
        }
    }
    
    
    public static Response list(int courseID, int examID, int answerID) {
        String uri = String.format("course/%d/exam/%d/problem/%d/answer", 
                courseID, examID, answerID);
        
        return new Response(NetworkUtils.get(uri));
    }
    
    public static Response create(int courseID, int examID, int problemID, 
                                  String filename, String sourceCode, String student) {
        String uri = String.format("course/%d/exam/%d/problem/%d/answer/create",
                courseID, examID, problemID);
        
        JsonObject json = new JsonObject();
        json.addProperty("filename", filename);
        json.addProperty("sourceCode", sourceCode);
        json.addProperty("student", student);
        
        return new Response(NetworkUtils.post(uri, json.toString()));
    }
    
    public Response update(String filename, String sourceCode) {
        String uri = String.format("course/%d/exam/%d/problem/%d/answer/update", 
                getCourseID(), getExamID(), getProblemID());
        
        sourceCode = (sourceCode != null) ? sourceCode : this.data.getSourceCode();
        
        JsonObject json = new JsonObject();
        json.addProperty("answerID", this.data.getAnswerID());
        json.addProperty("filename", filename);
        json.addProperty("sourceCode", sourceCode);
        
        return new Response(NetworkUtils.post(uri, json.toString()));
    }
    
    public Response remove() {
        String uri = String.format("course/%d/exam/%d/problem/%d/answer/remove",
                getCourseID(), getExamID(), getProblemID());
        
        JsonObject json = new JsonObject();
        json.addProperty("answerID", data.getAnswerID());
        
        return new Response(NetworkUtils.post(uri, json.toString()));
    }
    
    public Response validate() {
        String uri = String.format("course/%d/exam/%d/problem/%d/answer/validate",
                getCourseID(), getExamID(), getProblemID());
        
        JsonObject json = new JsonObject();
        json.addProperty("answerID", data.getAnswerID());
        
        return new Response(NetworkUtils.post(uri, json.toString()));
    }
    
    
    public AnswerData getData() {
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
    
    public Integer getAnswerID() {
        return data.getAnswerID();
    }
    
    
    public String getClassName() {
        return data.getClassName();
    }
    
    public String getSourceCode() {
        return data.getSourceCode();
    }
    
    public String getStudent() {
        return data.getStudent();
    }
    
    
    public SimpleStringProperty classNameProperty() {
        return data.classNameProperty();
    }
    
    public SimpleStringProperty sourceCodeProperty() {
        return data.sourceCodeProperty();
    }
    
    public SimpleStringProperty studentProperty() {
        return data.studentProperty();
    }
    
    @Override
    public String toString() {
        return data.toString();
    }
    
}
