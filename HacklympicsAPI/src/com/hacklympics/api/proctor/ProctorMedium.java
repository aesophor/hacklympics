package com.hacklympics.api.proctor;

import java.util.List;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hacklympics.api.communication.Response;
import com.hacklympics.api.user.Student;
import com.hacklympics.api.utility.NetworkUtils;

public interface ProctorMedium {
    
    public static Response adjustParam(int courseID, int examID, double snapshotQuality, int syncFrequency, List<Student> students) {
        String uri = String.format("course/%d/exam/%d/proctor/adjust_params", courseID, examID);
        
        JsonArray studentsJsonArray = new JsonArray();
        for (Student student : students) {
            studentsJsonArray.add(student.getUsername());
        }
        
        JsonObject json = new JsonObject();
        json.addProperty("snapshotQuality", snapshotQuality);
        json.addProperty("syncFrequency", syncFrequency);
        json.add("students", studentsJsonArray);
        
        return new Response(NetworkUtils.post(uri, json.toString()));
    }

}