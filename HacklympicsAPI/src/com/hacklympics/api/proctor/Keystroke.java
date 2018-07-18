package com.hacklympics.api.proctor;

import com.google.gson.JsonObject;
import com.hacklympics.api.communication.Response;
import com.hacklympics.api.utility.NetworkUtils;

public class Keystroke implements ProctorMedium {
    
    private final int examID;
    private final String studentUsername;
    private final String content;
    
    public Keystroke(int examID, String studentUsername, String content) {
        this.examID = examID;
        this.studentUsername = studentUsername;
        this.content = content;
    }
    
    
    public static Response sync(int courseID, int examID, String student, String content) {
        String uri = String.format("course/%d/exam/%d/keystroke/sync", courseID, examID);
        
        JsonObject json = new JsonObject();
        json.addProperty("student", student);
        json.addProperty("content", content);
        
        return new Response(NetworkUtils.post(uri, json.toString()));
    }
    
    
    public int getExamID() {
        return this.examID;
    }
    
    public String getStudentUsername() {
        return this.studentUsername;
    }
    
    public String getContent() {
        return this.content;
    }
    
    
    @Override
    public String toString() {
        return String.format("[%s] %s", this.studentUsername, this.content);
    }
    
}