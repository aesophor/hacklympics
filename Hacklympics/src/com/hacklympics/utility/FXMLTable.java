package com.hacklympics.utility;

import java.util.Map;
import java.util.HashMap;

public class FXMLTable {
    
    private static FXMLTable fxmls;
    private final Map<String, String> table;
    
    private FXMLTable() {
        table = new HashMap<>();
        
        table.put("Login",                "/com/hacklympics/login/Login.fxml");
        table.put("Register",             "/com/hacklympics/login/Register.fxml");
        table.put("About",                "/com/hacklympics/common/About.fxml");
        
        table.put("Student",              "/com/hacklympics/student/Student.fxml");
        table.put("Student/Dashboard",    "/com/hacklympics/student/dashboard/Dashboard.fxml");
        table.put("Student/Courses",      "/com/hacklympics/student/courses/Courses.fxml");
        table.put("Student/OngoingExams", "/com/hacklympics/student/exams/OngoingExams.fxml");
        table.put("Student/Messages",     "/com/hacklympics/student/messages/Messages.fxml");
        table.put("Student/Code",         "/com/hacklympics/student/code/Code.fxml");
        
        table.put("Teacher",              "/com/hacklympics/teacher/Teacher.fxml");
        table.put("Teacher/Dashboard",    "/com/hacklympics/teacher/dashboard/Dashboard.fxml");
        table.put("Teacher/Courses",      "/com/hacklympics/teacher/courses/Courses.fxml");
        table.put("Teacher/OngoingExams", "/com/hacklympics/teacher/exams/OngoingExams.fxml");
        table.put("Teacher/Messages",     "/com/hacklympics/teacher/messages/Messages.fxml");
        table.put("Teacher/Proctor",      "/com/hacklympics/teacher/proctor/Proctor.fxml");
    }
    
    public static FXMLTable getInstance() {
        if (fxmls == null) {
            fxmls = new FXMLTable();
        }
        
        return fxmls;
    }
    
    
    public String get(String pageName) {
        return table.get(pageName);
    }
    
}