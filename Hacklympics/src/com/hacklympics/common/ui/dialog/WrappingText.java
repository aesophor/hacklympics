package com.hacklympics.common.ui.dialog;

import javafx.scene.text.Text;

public class WrappingText extends Text {
    
    public WrappingText(String text, double width) {
        super(text);
        setWrappingWidth(width);
    }
    
}
