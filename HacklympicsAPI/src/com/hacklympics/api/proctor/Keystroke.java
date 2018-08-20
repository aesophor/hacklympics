package com.hacklympics.api.proctor;

import java.util.List;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hacklympics.api.communication.Response;
import com.hacklympics.api.utility.NetworkUtils;

public class Keystroke implements ProctorMedium {
    
    private final int examID;
    private final String studentUsername;
    private final List<String> patches;
    private final String timestamp;
    
    public Keystroke(int examID, String studentUsername, List<String> patches, String timestamp) {
        this.examID = examID;
        this.studentUsername = studentUsername;
        this.patches = patches;
        this.timestamp = timestamp;
    }
    
    
    public static Response sync(int courseID, int examID, String student, List<String> patches) {
        String uri = String.format("course/%d/exam/%d/proctor/sync_keystrokes", courseID, examID);
        
        JsonArray patchJsonArray = new JsonArray();
        for (String patch : patches) {
            patchJsonArray.add(patch);
        }
        
        JsonObject json = new JsonObject();
        json.addProperty("student", student);
        json.add("patches", patchJsonArray);
        
        return new Response(NetworkUtils.post(uri, json.toString()));
    }
    
    
    public int getExamID() {
        return this.examID;
    }
    
    public String getStudentUsername() {
        return this.studentUsername;
    }
    
    public List<String> getPatches() {
        return this.patches;
    }
    
    public String getTimestamp() {
        return this.timestamp;
    }
    
}