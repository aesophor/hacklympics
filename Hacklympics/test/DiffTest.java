import java.util.List;
import java.util.ArrayList;
import difflib.DiffUtils;
import difflib.Delta;
import difflib.Patch;

public class DiffTest {

    public static void main(String[] args) {
        List<String> original = new ArrayList<>();
        List<String> revised = new ArrayList<>();
        
        original.add("Hello World.");
        revised.add("Hello Coding World.");
        
        original.add("Goodbye World.");
        revised.add("Hello World.");

        // Compute diff. Get the Patch object. Patch is the container for computed deltas.
        Patch patch = DiffUtils.diff(original, revised);

        for (Delta delta : patch.getDeltas()) {
            System.out.println(delta);
        }
    }
    
}
