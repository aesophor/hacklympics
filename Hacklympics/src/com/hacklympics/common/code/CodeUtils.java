package com.hacklympics.common.code;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

import com.hacklympics.common.code.lang.Language;

import difflib.DiffUtils;

public class CodeUtils {
	
	private CodeUtils() {
		
	}
	
	
	public static StyleSpans<Collection<String>> computeHighlighting(Language lang, String text) {
        Matcher matcher = lang.getPattern().matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();

        while (matcher.find()) {
            String styleClass
                    = matcher.group("KEYWORD") != null ? "keyword"
                    : matcher.group("PAREN") != null ? "paren"
                    : matcher.group("BRACE") != null ? "brace"
                    : matcher.group("BRACKET") != null ? "bracket"
                    : matcher.group("SEMICOLON") != null ? "semicolon"
                    : matcher.group("STRING") != null ? "string"
                    : matcher.group("COMMENT") != null ? "comment"
                    : null;
            assert styleClass != null;
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }

        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }
	
	
	/**
     * Compute the difference between the original and revised texts with default diff algorithm
     * 
     * @param original the original text (String)
     * @param revised the revised text (String)
     * @return the code patch, which is a wrapper of the patch describing the difference between
     * 		   the original and revised texts
     */
    public static CodePatch diff(String original, String revised) {
    	List<String> originalLines = Arrays.asList(original.split("\\n"));
    	List<String> revisedLines = Arrays.asList(revised.split("\\n"));
    	
    	return new CodePatch(DiffUtils.diff(originalLines, revisedLines));
    }
}