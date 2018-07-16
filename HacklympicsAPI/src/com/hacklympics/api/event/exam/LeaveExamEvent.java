package com.hacklympics.api.event.exam;

import java.util.Map;
import com.google.gson.JsonObject;
import com.hacklympics.api.event.Event;
import com.hacklympics.api.event.ExamDependent;
import com.hacklympics.api.material.Exam;
import com.hacklympics.api.session.Session;
import com.hacklympics.api.user.User;
import com.hacklympics.api.user.Student;
import com.hacklympics.api.user.Teacher;
import com.hacklympics.api.utility.NetworkUtils;

public class LeaveExamEvent extends Event implements ExamDependent {
    
    private final Exam exam;
    private final User user;
    
    public LeaveExamEvent(String rawJson) {
        super(rawJson);
        
        Map<String, Object> content = this.getContent();
        
        String rawTeacherJson = NetworkUtils.getGson().toJson(content.get("user"));
        JsonObject userJson = NetworkUtils.getGson().fromJson(rawTeacherJson, JsonObject.class);
        
        String username = userJson.get("username").getAsString();
        String fullname = userJson.get("fullname").getAsString();
        int gradYear = userJson.get("graduationYear").getAsInt();
        boolean isStudent = userJson.get("isStudent").getAsBoolean();
        
        this.user = (isStudent) ? new Student(username, fullname, gradYear)
                                : new Teacher(username, fullname, gradYear);
        
        
        String rawExamJson = NetworkUtils.getGson().toJson(content.get("exam"));
        JsonObject examJson = NetworkUtils.getGson().fromJson(rawExamJson, JsonObject.class);
        
        int courseID = examJson.get("courseID").getAsInt();
        int examID = examJson.get("examID").getAsInt();
        String title = examJson.get("title").getAsString();
        String desc = examJson.get("desc").getAsString();
        int duration = examJson.get("duration").getAsInt();
        
        this.exam = new Exam(courseID, examID, title, desc, duration);
    }
    
    
    /**
     * Returns the exam from which the user just left.
     * @return the exam.
     */
    public Exam getExam() {
        return exam;
    }
    
    /**
     * Returns the user that just left the exam.
     * @return the user.
     */
    public User getUser() {
        return user;
    }
    
    
    @Override
    public boolean isForCurrentExam() {
        int eventExamID = this.getExam().getExamID();
        int currentExamID = Session.getInstance().getCurrentExam().getExamID();
        
        return eventExamID == currentExamID;
    }
    
}
