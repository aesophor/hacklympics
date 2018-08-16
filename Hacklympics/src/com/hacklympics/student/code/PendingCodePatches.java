package com.hacklympics.student.code;

import java.util.List;

import java.util.ArrayList;

public class PendingCodePatches {

	private static PendingCodePatches pendingCodePatches;
	private final List<String> patches;
	
	private PendingCodePatches() {
		patches = new ArrayList<>();
	}
	
	public static PendingCodePatches getInstance() {
		if (pendingCodePatches == null) {
			pendingCodePatches = new PendingCodePatches();
		}
		
		return pendingCodePatches;
	}
	
	
	public boolean isEmpty() {
		return patches.isEmpty();
	}
	
	public void add(String serializedPatch) {
		patches.add(serializedPatch);
	}
	
	public void clear() {
		patches.clear();
	}
	
	public List<String> getPatches() {
		return patches;
	}
	
}