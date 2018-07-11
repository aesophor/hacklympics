package com.hacklympics.api.proctor;

import com.google.gson.JsonObject;
import com.hacklympics.api.communication.Response;
import com.hacklympics.api.utility.Utils;

public class Snapshot {
    
    public Snapshot() {
        
    }
    
    
    public static Response create(int courseID, int examID, String student, String b64image) {
        String uri = String.format("course/%d/exam/%d/snapshot/create", courseID, examID);
        
        JsonObject json = new JsonObject();
        json.addProperty("student", student);
        json.addProperty("image", b64image);
        
        return new Response(Utils.post(uri, json.toString()));
    }
    
}
