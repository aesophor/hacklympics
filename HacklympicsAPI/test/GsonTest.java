import java.util.Map;
import java.util.HashMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;


class Sentence {
    private int size;
    private Word[] words;
    
    public Sentence(Word[] words) {
        this.words = words;
        this.size = words.length;
    }
}

class Word {
    private int size;
    private String word;
    
    public Word(String word) {
        this.word = word;
        this.size = word.length();
    }
}


public class GsonTest {    
    
    public static void main(String[] args) {
        
        Sentence s = new Sentence(new Word[] {
            new Word("I"),
            new Word("am"),
            new Word("Marco")
        });
        
        Gson gson = new Gson();
        //Map<String, Object> map = string2Map(gson, gson.toJson(s));
        //Map<String, Object> map = object2Map(s);
        
        String json = gson.toJson(s);
        System.out.println(json);
        
        JsonObject j = gson.fromJson(json, JsonObject.class);
        System.out.println(j.toString());
        
        //Map<String, Object> map;
        //map = gson.fromJson(json, HashMap.class);
        //System.out.println(map.toString());
        
        //map.put("lolcat", "John Nash");
        //System.out.println(map.toString());
        
        //String newJson = new Gson().toJson(map, HashMap.class);
        //System.out.println(newJson);
        
        //System.out.println( ((Map)((List) map.get("words")).get(1)).get("word") );
        // Looks intimidating... lol
    }
    
    /*
    public static HashMap<String, Object> string2Map(Gson gson, String json) {
        return gson.fromJson(json, HashMap.class);
    }
    */
    
    public static HashMap<String, Object> object2Map(Object o) {
        Gson gson = new Gson();
        return gson.fromJson(gson.toJson(o), HashMap.class);
    }
    
}
