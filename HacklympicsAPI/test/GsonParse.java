import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hacklympics.api.communication.Response;
import com.hacklympics.api.user.User;

public class GsonParse {
    
    public static void main(String[] args) {
        Response list = User.list();
        
        if (list.success()) {
            String json = list.getContent().get("users").toString();
            
            JsonArray users = new Gson().fromJson(json, JsonArray.class);
            
            for (JsonElement e: users) {
                System.out.println(e);
            }
        }
    }
    
}
