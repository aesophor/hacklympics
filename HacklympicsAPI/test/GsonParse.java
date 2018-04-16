import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hacklympics.api.communication.Response;
import com.hacklympics.api.user.User;

public class GsonParse {
    
    public static void main(String[] args) {
        /*
        Response list = User.list();
        
        if (list.success()) {
            String json = list.getContent().toString();
            
            JsonObject usernames = new Gson().fromJson(json, JsonObject.class);
            
            List<String> u = new Gson().fromJson(usernames.get("users"), List.class);
            System.out.println(u);
        }
        */
    }
    
}
