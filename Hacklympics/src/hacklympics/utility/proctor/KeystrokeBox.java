package hacklympics.utility.proctor;

import java.util.List;
import java.util.ArrayList;
import javafx.scene.control.TextArea;
import com.jfoenix.controls.JFXRadioButton;
import com.hacklympics.api.user.Student;
import com.hacklympics.api.proctor.Keystroke;

public class KeystrokeBox extends StudentBox<Keystroke> {
    
    private static final int CODEAREA_WIDTH = 155;
    private static final int CODEAREA_HEIGHT = 93;
    private static final int CODEAREA_LAYOUT_X = 15;
    private static final int CODEAREA_LAYOUT_Y = 20;
    
    private final List<String> keystrokeHistory;
    
    private final JFXRadioButton radioBtn;
    private final TextArea codeArea;

    public KeystrokeBox(Student student) {
        super(student);
        
        this.keystrokeHistory = new ArrayList<>();
        
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
    public void update(Keystroke keystroke) {
        this.codeArea.setText(keystroke.getContent());
    }
    
    public List<String> getKeystrokeHistory() {
        return this.keystrokeHistory;
    }
    
    public JFXRadioButton getRadioBtn() {
        return this.radioBtn;
    }
    
}