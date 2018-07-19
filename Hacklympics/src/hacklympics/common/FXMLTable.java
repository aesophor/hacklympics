package hacklympics.common;

import java.util.Map;
import java.util.HashMap;

public class FXMLTable {
    
    private static FXMLTable fxmls;
    private final Map<String, String> table;
    
    private FXMLTable() {
        table = new HashMap<>();
        
        table.put("Login",                "/hacklympics/login/Login.fxml");
        table.put("Register",             "/hacklympics/login/Register.fxml");
        
        table.put("Student",              "/hacklympics/student/Student.fxml");
        table.put("Student/Dashboard",    "/hacklympics/student/dashboard/Dashboard.fxml");
        table.put("Student/Courses",      "/hacklympics/student/courses/Courses.fxml");
        table.put("Student/OngoingExams", "/hacklympics/student/exams/OngoingExams.fxml");
        table.put("Student/Messages",     "/hacklympics/student/messages/Messages.fxml");
        table.put("Student/Code",         "/hacklympics/student/code/Code.fxml");
        
        table.put("Teacher",              "/hacklympics/teacher/Teacher.fxml");
        table.put("Teacher/Dashboard",    "/hacklympics/teacher/dashboard/Dashboard.fxml");
        table.put("Teacher/Courses",      "/hacklympics/teacher/courses/Courses.fxml");
        table.put("Teacher/OngoingExams", "/hacklympics/teacher/exams/OngoingExams.fxml");
        table.put("Teacher/Messages",     "/hacklympics/teacher/messages/Messages.fxml");
        table.put("Teacher/Proctor",      "/hacklympics/teacher/proctor/Proctor.fxml");
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
