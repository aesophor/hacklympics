import java.io.IOException;
import com.google.gson.JsonObject;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Post {
    
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public OkHttpClient client = new OkHttpClient();
    
    public String post(String url, String json) throws IOException {
        
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder().url(url).post(body).build();
        
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
    
    public String createUser(User user) {
        
        JsonObject j = new JsonObject();
        
        j.addProperty("username", user.getUsername());
        j.addProperty("password", user.getPassword());
        j.addProperty("fullname", user.getFullname());
        j.addProperty("graduation_year", user.getGradYear());
        j.addProperty("is_student", new Boolean(user.isStudent()).toString());
        
        System.out.println(j.get("username").getAsString());
        
        return j.toString();
    }
    
    
    public static void main(String[] args) throws IOException {
        User andrey = new User("andrey", "lolcat", "Andrey", 108, true);
        
        Post example = new Post();
        String json = example.createUser(andrey);
        //System.out.println(json);
        //String response = example.post("http://127.0.0.1:8000/user/create", json);
        //System.out.println(response);
    }
    
}