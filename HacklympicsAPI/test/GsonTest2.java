import java.util.List;
import java.util.ArrayList;
import com.google.gson.Gson;

public class GsonTest2 {
    
    public static Gson gson = new Gson();
    
    public static void main(String[] args) {
        /* Using String[] */
        String[] names = new String[] {"andrey", "tim", "max"};
        String json = gson.toJson(names);
        System.out.println(json);
        
        for (String name: names) {
            System.out.println(name);
        }
        
        
        /* Using List<String> */
        List<String> namesList = new ArrayList<>();
        namesList.add("andrey");
        namesList.add("tim");
        System.out.println(namesList);
        
        for (String name: namesList) {
            System.out.println(name);
        }
        
        String out = gson.toJson(namesList);
        System.out.println(out);
    }
    
}
