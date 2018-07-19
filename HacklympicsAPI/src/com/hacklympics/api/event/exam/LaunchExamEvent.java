package com.hacklympics.api.event.exam;

import java.util.Map;
import com.google.gson.JsonObject;
import com.hacklympics.api.event.Event;
import com.hacklympics.api.material.Exam;
import com.hacklympics.api.user.Teacher;
import com.hacklympics.api.utility.NetworkUtils;

public class LaunchExamEvent extends Event {
    
    private final Exam exam;
    private final Teacher teacher;
    
    public LaunchExamEvent(String rawJson) {
        super(rawJson);
        
        Map<String, Object> content = this.getContent();
        
        
        // Extract exam from json content.
        String rawExamJson = NetworkUtils.getGson().toJson(content.get("exam"));
        JsonObject examJson = NetworkUtils.getGson().fromJson(rawExamJson, JsonObject.class);
        
        int courseID = examJson.get("courseID").getAsInt();
        int examID = examJson.get("examID").getAsInt();
        String title = examJson.get("title").getAsString();
        String desc = examJson.get("desc").getAsString();
        int duration = examJson.get("duration").getAsInt();
        
        this.exam = new Exam(courseID, examID, title, desc, duration);
        
        
        // Extract teacher from json content.
        String rawTeacherJson = NetworkUtils.getGson().toJson(content.get("teacher"));
        JsonObject teacherJson = NetworkUtils.getGson().fromJson(rawTeacherJson, JsonObject.class);
        
        String username = teacherJson.get("username").getAsString();
        String fullname = teacherJson.get("fullname").getAsString();
        int gradYear = teacherJson.get("graduationYear").getAsInt();
        
        this.teacher = new Teacher(username, fullname, gradYear);
    }
    
    
    /**
     * Returns the exam that has just been launched.
     * @return the exam.
     */
    public Exam getExam() {
        return exam;
    }
    
    /**
     * Returns the teacher to which the exam belongs.
     * @return the teacher.
     */
    public Teacher getTeacher() {
        return teacher;
    }
    
}
