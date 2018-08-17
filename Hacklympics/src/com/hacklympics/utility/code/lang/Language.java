package com.hacklympics.utility.code.lang;

import java.util.regex.Pattern;

public enum Language {

	JAVA(Java.COLORSCHEME, Java.SAMPLE_CODE, Java.KEYWORDS);
	
	
	private final String colorschemeCSS;
	private final String sampleCode;
	
	private final String keywordPattern;
	private final String parenPattern;
	private final String bracePattern;
	private final String bracketPattern;
	private final String semicolonPattern;
	private final String stringPattern;
	private final String commentPattern;
    private final Pattern pattern;
	
	private Language(String colorschemeCSS, String sampleCode, String[] keywords) {
		this.colorschemeCSS = colorschemeCSS;
		this.sampleCode = sampleCode;
		
		keywordPattern = "\\b(" + String.join("|", keywords) + ")\\b";
    	parenPattern = "\\(|\\)";
    	bracePattern = "\\{|\\}";
    	bracketPattern = "\\[|\\]";
    	semicolonPattern = "\\;";
    	stringPattern = "\"([^\"\\\\]|\\\\.)*\"";
    	commentPattern = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/";
    	
    	pattern = Pattern.compile(
    			"(?<KEYWORD>" + keywordPattern + ")"
    		  + "|(?<PAREN>" + parenPattern + ")"
              + "|(?<BRACE>" + bracePattern + ")"
              + "|(?<BRACKET>" + bracketPattern + ")"
              + "|(?<SEMICOLON>" + semicolonPattern + ")"
              + "|(?<STRING>" + stringPattern + ")"
              + "|(?<COMMENT>" + commentPattern + ")"
        );
	}
	
	
	public String getCSSFilepath() {
		return colorschemeCSS;
	}
	
	public String getSampleCode() {
		return sampleCode;
	}
	
	public Pattern getPattern() {
		return pattern;
	}
	
}