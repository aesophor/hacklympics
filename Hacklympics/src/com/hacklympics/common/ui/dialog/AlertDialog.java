package com.hacklympics.common.ui.dialog;

import java.util.ArrayList;

public class AlertDialog extends Dialog {
    
    public AlertDialog(String title, String body) {
        super(title);
        
        content.setBody(new WrappingText(body, container.getWidth()));
        buttons.remove("confirmBtn");
        
        content.setActions(new ArrayList<>(buttons.values()));
    }
    
}