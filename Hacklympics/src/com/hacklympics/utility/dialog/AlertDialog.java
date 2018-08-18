package com.hacklympics.utility.dialog;

import java.util.ArrayList;

import javafx.scene.Node;

public class AlertDialog extends Dialog {
    
    public AlertDialog(String title, String body) {
        super(title);
        
        content.setBody(new WrappingText(body, container.getWidth()));
        buttons.remove("confirmBtn");
        
        content.setActions(new ArrayList<>(buttons.values()));
    }
    
    public AlertDialog(String title, Node body) {
        super(title);
        
        content.setBody(body);
        buttons.remove("confirmBtn");
        
        content.setActions(new ArrayList<>(buttons.values()));
    }
    
}