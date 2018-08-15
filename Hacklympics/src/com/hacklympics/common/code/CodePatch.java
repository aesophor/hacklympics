package com.hacklympics.common.code;

import java.util.List;
import java.util.Arrays;
import difflib.Patch;
import difflib.PatchFailedException;

public class CodePatch extends difflib.Patch<String> {
	
	public CodePatch(Patch<String> patch) {
		getDeltas().addAll(patch.getDeltas());
	}
	
	
	/**
     * Apply this patch to the given target
     * @param target
     * @return the patched text (String)
     * @throws PatchFailedException if can't apply patch
     */
    public String applyTo(String target) throws PatchFailedException {
    	List<String> targetLines = Arrays.asList(target.split("\\n"));
    	List<String> patchedLines = this.applyTo(targetLines);
    	
    	String patchedText = "";
    	
    	for (int i = 0; i < patchedLines.size(); i++) {
    		patchedText += patchedLines.get(i);
    		if (i < patchedLines.size() - 1) {
    			patchedText += "\n";
    		}
    	}
    	
    	return patchedText;
    }
    
}