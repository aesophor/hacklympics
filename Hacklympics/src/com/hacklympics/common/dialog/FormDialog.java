package com.hacklympics.common.dialog;

import java.util.Map;
import java.util.HashMap;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

public class FormDialog extends Dialog {
    
    private static final double VBOX_SPACING = 30.0;
    
    private final VBox vbox;
    private final Map<String, Node> components;
    
    public FormDialog(String title) {
        super(title);
        
        components = new HashMap<>();
        vbox = new VBox(VBOX_SPACING);
        
        content.setBody(vbox);
    }
    
    
    public Node get(String identifier) {
        return components.get(identifier);
    }
    
    public Button getAsButton(String identifier) {
        return (Button) components.get(identifier);
    }
    
    
    public void add(String identifier, Node node) {
        components.put(identifier, node);
        
        vbox.getChildren().add(components.get(identifier));
    }
    
    public void addTextField(String identifier, String content) {
        JFXTextField textField = new JFXTextField();
        textField.setLabelFloat(true);
        textField.setPromptText(identifier);
        textField.setText(content);
        
        add(identifier, textField);
    }
    
    public void addTextArea(String identifier, String content) {
        JFXTextArea textArea = new JFXTextArea();
        textArea.setMinHeight(50);
        textArea.setMaxHeight(50);
        textArea.setLabelFloat(true);
        textArea.setPromptText(identifier);
        textArea.setText(content);
        
        add(identifier, textArea);
    }
    
    public void addDeleteBtn(String identifier) {
        Button deleteBtn = new Button("Delete");
        deleteBtn.getStyleClass().add("textBtn-delete");
        
        add(identifier, deleteBtn);
    }
    
}