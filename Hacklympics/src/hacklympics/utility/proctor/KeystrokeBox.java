package hacklympics.utility.proctor;

import java.io.IOException;
import javafx.application.Platform;
import javafx.scene.control.TextArea;
import com.jfoenix.controls.JFXRadioButton;
import com.hacklympics.api.snapshot.Snapshot;
import com.hacklympics.api.user.Student;

public class KeystrokeBox extends StudentBox {
    
    private static final int CODEAREA_WIDTH = 155;
    private static final int CODEAREA_HEIGHT = 93;
    private static final int CODEAREA_LAYOUT_X = 0;
    private static final int CODEAREA_LAYOUT_Y = 20;
    
    private final JFXRadioButton radioBtn;
    private final TextArea codeArea;

    public KeystrokeBox(Student student) {
        super(student);
        
        this.radioBtn = new JFXRadioButton(student.getFullname());
        
        this.codeArea = new TextArea();
        this.codeArea.setEditable(false);
        this.codeArea.setPrefWidth(CODEAREA_WIDTH);
        this.codeArea.setPrefHeight(CODEAREA_HEIGHT);
        this.codeArea.setLayoutX(CODEAREA_LAYOUT_X);
        this.codeArea.setLayoutY(CODEAREA_LAYOUT_Y);
        
        this.getChildren().addAll(this.radioBtn, this.codeArea);
    }
    
    
    @Override
    public void update(Snapshot snapshot) throws IOException {
        
        Platform.runLater(() -> {
            
        });
    }
    
    public JFXRadioButton getRadioBtn() {
        return this.radioBtn;
    }
    
    
}