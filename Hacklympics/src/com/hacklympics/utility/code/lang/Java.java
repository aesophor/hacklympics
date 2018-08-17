package com.hacklympics.utility.code.lang;

public class Java {

	public static final String COLORSCHEME = "/resources/JavaKeywords.css";

    public static final String[] KEYWORDS = new String[]{
        "abstract", "assert", "boolean", "break", "byte",
        "case", "catch", "char", "class", "const",
        "continue", "default", "do", "double", "else",
        "enum", "extends", "final", "finally", "float",
        "for", "goto", "if", "implements", "import",
        "instanceof", "int", "interface", "long", "native",
        "new", "package", "private", "protected", "public",
        "return", "short", "static", "strictfp", "super",
        "switch", "synchronized", "this", "throw", "throws",
        "transient", "try", "void", "volatile", "while"
    };

    public static final String SAMPLE_CODE = String.join("\n", new String[]{
        "import java.util.*;",
        "",
        "public class Program {",
        "",
        "    public static void main(String[] args) {",
        "        // write your code right here.",
        "    }",
        "",
        "}"
    });
    
    
    private Java() {
    	
    }
	
}