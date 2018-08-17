package com.hacklympics.utility.dialog;

import javafx.scene.text.Text;

public class WrappingText extends Text {
    
    public WrappingText(String text, double width) {
        super(text);
        setWrappingWidth(width);
    }
    
}
