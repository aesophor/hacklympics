package com.hacklympics.teacher.proctor;

import java.util.List;
import java.io.IOException;
import java.util.ArrayList;
import javafx.scene.text.Font;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.application.Platform;
import difflib.Patch;
import difflib.PatchFailedException;
import com.jfoenix.controls.JFXRadioButton;
import com.hacklympics.api.user.Student;
import com.hacklympics.utility.Utils;
import com.hacklympics.api.proctor.Keystroke;

public class KeystrokeBox extends StudentBox<Keystroke> {
    
    private static final int CODEAREA_WIDTH = 155;
    private static final int CODEAREA_HEIGHT = 93;
    private static final int CODEAREA_LAYOUT_X = 15;
    private static final int CODEAREA_LAYOUT_Y = 20;
    
    private static final int TIMELABEL_LAYOUT_X = 30;
    private static final int TIMELABEL_LAYOUT_Y = 115;
    private static final int FINISH_LABEL_LAYOUT_X = 65;
    
    private final List<String> patches;
    private int lastAppliedPatchIndex;
    
    private final JFXRadioButton radioBtn;
    private final TextArea codeArea;
    private final Label timestamp;

    public KeystrokeBox(Student student) {
        super(student);
        
        patches = new ArrayList<>();
        lastAppliedPatchIndex = -1;
        
        radioBtn = new JFXRadioButton(student.getFullname());
        
        codeArea = new TextArea();
        codeArea.setEditable(false);
        codeArea.setFont(Font.font("System", 8.0));
        codeArea.setPrefWidth(CODEAREA_WIDTH);
        codeArea.setPrefHeight(CODEAREA_HEIGHT);
        codeArea.setLayoutX(CODEAREA_LAYOUT_X);
        codeArea.setLayoutY(CODEAREA_LAYOUT_Y);
        
        timestamp = new Label("Waiting...");
        timestamp.setLayoutX(TIMELABEL_LAYOUT_X);
        timestamp.setLayoutY(TIMELABEL_LAYOUT_Y);
        
        getChildren().addAll(radioBtn, codeArea, timestamp);
    }
    
    
    @Override
    public void update(Keystroke keystroke) {
        patches.addAll(keystroke.getPatches());
        
        if (patches.size() > 0) {
            int lastIndex = patches.size() - 1;
            
            try {
            	for (int i = lastAppliedPatchIndex + 1; i <= lastIndex; i++) {
            		Patch patch = (Patch) Utils.deserialize(patches.get(i));
            		
            		Platform.runLater(() -> {
    					try {
							codeArea.setText(patch.applyTo(codeArea.getText()));
						} catch (PatchFailedException e) {
							e.printStackTrace();
						}
    					
    	                timestamp.setText(keystroke.getTimestamp());
    	            });
            	}
            	
            	// Update the last applied patch index.
            	lastAppliedPatchIndex = lastIndex;
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
        }
    }
    
    @Override
    public void markAsFinished() {
        Platform.runLater(() -> {
            timestamp.setText("Finished");
            timestamp.setLayoutX(FINISH_LABEL_LAYOUT_X);
            timestamp.getStyleClass().add("finish-label");
        });
    }
    
    public List<String> getPatches() {
        return patches;
    }
    
    public JFXRadioButton getRadioBtn() {
        return radioBtn;
    }
    
}