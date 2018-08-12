package hacklympics.utility;

import java.util.List;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import difflib.Delta;
import difflib.DiffUtils;
import difflib.Patch;
import difflib.PatchFailedException;

public class Test {
	
	public static void main(String[] args) {
		String content = "Hello World\nfoo bar\n123";
		
		List<String> original = Arrays.asList(content.split("\\n"));
        List<String> revised  = new ArrayList<>();
        
        revised.add("Hello World");
        revised.add("foo bar");
        revised.add("1234");

        // Compute diff. Get the Patch object. Patch is the container for computed deltas.
        Patch patch = DiffUtils.diff(original, revised);
        
        for (Delta d : patch.getDeltas()) {
        	System.out.println(d);
        }
        
        try {
        	System.out.println(patch.applyTo(original));
		} catch (PatchFailedException e) {
			e.printStackTrace();
		}
        
        String serialized = "";
        try {
			serialized = Utils.serialize(patch);
			System.out.println(serialized);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        try {
			Patch deserialized = (Patch) Utils.deserialize(serialized);
			System.out.println(deserialized.applyTo(original));
		} catch (ClassNotFoundException | IOException | PatchFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}