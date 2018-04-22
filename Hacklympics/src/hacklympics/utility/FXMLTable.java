package hacklympics.utility;

import java.util.Map;
import java.util.HashMap;

public class FXMLTable {
    
    private static FXMLTable fxmls;
    private final Map<String, String> table;
    
    private FXMLTable() {
        table = new HashMap<>();
        
        table.put("Login",             "/hacklympics/login/Login.fxml");
        
        table.put("Student",           "/hacklympics/student/Student.fxml");
        
        table.put("Teacher",           "/hacklympics/teacher/Teacher.fxml");
        table.put("Teacher/dashboard", "/hacklympics/teacher/pages/Dashboard.fxml");
        table.put("Teacher/students",  "/hacklympics/teacher/pages/Students.fxml");
        table.put("Teacher/courses",   "/hacklympics/teacher/pages/Courses.fxml");
        table.put("Teacher/exams",     "/hacklympics/teacher/pages/Exams.fxml");
        table.put("Teacher/problems",  "/hacklympics/teacher/pages/Problems.fxml");
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
