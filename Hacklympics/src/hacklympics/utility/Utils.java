package hacklympics.utility;

import java.util.List;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.hacklympics.api.communication.Response;
import com.hacklympics.api.users.User;
import com.hacklympics.api.users.Student;
import com.hacklympics.api.users.Teacher;

public class Utils {

    private static final Gson GSON = new Gson();
    
    private Utils() {
        
    }
 
    
    public static List<Student> getStudents() {
        List<Student> students = new ArrayList<>();
        Response list = User.list();
        
        if (list.success()) {
            String json = GSON.toJson(list.getContent().get("users"));
            JsonArray users = GSON.fromJson(json, JsonArray.class);
            
            for (JsonElement e: users) {
                String username = e.getAsJsonObject().get("username").getAsString();
                String fullname = e.getAsJsonObject().get("fullname").getAsString();
                int gradYear = e.getAsJsonObject().get("graduationYear").getAsInt();
                boolean isStudent = e.getAsJsonObject().get("isStudent").getAsBoolean();
                
                if (isStudent) {
                    students.add(new Student(username, fullname, gradYear));
                }
            }
        }
        
        return students;
    }
    
    public static List<Teacher> getTeachers() {
        List<Teacher> teachers = new ArrayList<>();
        Response list = User.list();
        
        if (list.success()) {
            String json = GSON.toJson(list.getContent().get("users"));
            JsonArray users = GSON.fromJson(json, JsonArray.class);
            
            for (JsonElement e: users) {
                String username = e.getAsJsonObject().get("username").getAsString();
                String fullname = e.getAsJsonObject().get("fullname").getAsString();
                int gradYear = e.getAsJsonObject().get("graduationYear").getAsInt();
                boolean isStudent = e.getAsJsonObject().get("isStudent").getAsBoolean();
                
                if (!isStudent) {
                    teachers.add(new Teacher(username, fullname, gradYear));
                }
            }
        }
        
        return teachers;
    }
}
