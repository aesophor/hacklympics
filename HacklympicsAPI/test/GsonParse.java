import com.google.gson.Gson;
import com.google.gson.JsonArray;
import java.util.Map;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.hacklympics.api.communication.Response;
import com.hacklympics.api.materials.Course;
import com.hacklympics.api.users.User;

public class GsonParse {
    
    public static void main(String[] args) {
        
        
        Response list = User.list();
        
        if (list.success()) {
            String raw = new Gson().toJson(list.getContent().get("users"));
            JsonArray users = new Gson().fromJson(raw, JsonArray.class);
            
            for (JsonElement e: users) {
                System.out.println(e);
            }
        }
        
        
        /*
        Response list = Course.list();
        
        if (list.success()) {
            String raw = new Gson().toJson(list.getContent().get("courses"));
            System.out.println(raw);
            
            JsonArray courses = new Gson().fromJson(raw, JsonArray.class);
            System.out.println(courses);
            
            for (JsonElement e: courses) {
                System.out.println(e);
            }
        }
        */
    }
    
}
