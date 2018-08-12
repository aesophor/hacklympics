package hacklympics.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.Serializable;
import difflib.Patch;
import difflib.PatchFailedException;
import difflib.DiffUtils;

public class CodePatch implements Serializable {
	
	private final Patch patch; // java.io.NotSerializableException: difflib.Patch
	
	public CodePatch(String original, String revised) {
		List<String> originalLines = Arrays.asList(original.split("\\n"));
    	List<String> revisedLines = Arrays.asList(revised.split("\\n"));
    	
    	patch = DiffUtils.diff(originalLines, revisedLines);
	}
	
	
	public String applyTo(String text) {
		List<String> lines = Arrays.asList(text.split("\\n"));
		List<String> patchedLines = new ArrayList<>();
		
		try {
			patchedLines = (List<String>) patch.applyTo(lines);
		} catch (PatchFailedException e) {
			e.printStackTrace();
		}
		
		String patchedText = "";
		
		for (String line : patchedLines) {
			patchedText += (line + "\n");
		}
		
		return patchedText;
	}
	
	public Patch getPatch() {
		return patch;
	}
	
}