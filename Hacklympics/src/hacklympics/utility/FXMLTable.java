package hacklympics.utility;

import java.util.Map;
import java.util.HashMap;

public class FXMLTable {
    
    private static FXMLTable fxmls;
    private final Map<String, String> table;
    
    private FXMLTable() {
        table = new HashMap<>();
        
        table.put("Login",              "/hacklympics/login/Login.fxml");
        table.put("Register",           "/hacklympics/login/Register.fxml");
        
        table.put("Student",            "/hacklympics/student/Student.fxml");
        table.put("Student/dashboard",  "/hacklympics/student/pages/Dashboard.fxml");
        table.put("Student/courses",    "/hacklympics/student/pages/Courses.fxml");
        table.put("Student/scoreboard", "/hacklympics/student/pages/Scoreboard.fxml");
        table.put("Student/messages",   "/hacklympics/student/pages/Messages.fxml");
        table.put("Student/code",       "/hacklympics/student/pages/Code.fxml");
        
        table.put("Teacher",            "/hacklympics/teacher/Teacher.fxml");
        table.put("Teacher/dashboard",  "/hacklympics/teacher/pages/Dashboard.fxml");
        table.put("Teacher/courses",    "/hacklympics/teacher/pages/Courses.fxml");
        table.put("Teacher/students",   "/hacklympics/teacher/pages/Students.fxml");
        table.put("Teacher/messages",   "/hacklympics/teacher/pages/Messages.fxml");
        table.put("Teacher/proctor",    "/hacklympics/teacher/pages/Proctor.fxml");
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
