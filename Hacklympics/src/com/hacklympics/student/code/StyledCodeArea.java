package com.hacklympics.student.code;

import java.time.Duration;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

import com.hacklympics.utility.code.CodeUtils;
import com.hacklympics.utility.code.lang.Language;

public class StyledCodeArea extends CodeArea {
	
	private Language currentLanguage;
	
	public StyledCodeArea(Language language) {
		// This part needs to be reworked!
		currentLanguage = language;
		
		// Spice up the CodeArea with css.
		getStyleClass().add("code-area");
        getStylesheets().add(language.getCSSFilepath());
        
        // Enable line numbers.
        setParagraphGraphicFactory(LineNumberFactory.get(this));
        
        // Enable highlight computing.
        multiPlainChanges()
				.successionEnds(Duration.ofMillis(100))
				.subscribe(ignore -> setStyleSpans(0, CodeUtils.computeHighlighting(language, getText())));
	}
	
	
	
	public Language getCurrentLanguage() {
		return currentLanguage;
	}
	
	public void setCurrentLanguage(Language language) {
		currentLanguage = language;
	}
	
}