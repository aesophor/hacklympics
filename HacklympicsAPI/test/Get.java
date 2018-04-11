import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Get {
    
    OkHttpClient client = new OkHttpClient();
    
    public String run(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
    
    public static void main(String[] args) throws IOException {
        Get e = new Get();
        String response = e.run("http://127.0.0.1:8000/user/max");
        System.out.println(response);
    }
}