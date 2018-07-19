package hacklympics.teacher.proctor;

import java.util.List;
import java.util.ArrayList;
import javafx.scene.text.Font;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import com.jfoenix.controls.JFXRadioButton;
import com.hacklympics.api.user.Student;
import com.hacklympics.api.proctor.Keystroke;
import javafx.application.Platform;

public class KeystrokeBox extends StudentBox<Keystroke> {
    
    private static final int CODEAREA_WIDTH = 155;
    private static final int CODEAREA_HEIGHT = 93;
    private static final int CODEAREA_LAYOUT_X = 15;
    private static final int CODEAREA_LAYOUT_Y = 20;
    
    private static final int TIMELABEL_LAYOUT_X = 30;
    private static final int TIMELABEL_LAYOUT_Y = 115;
    private static final int FINISH_LABEL_LAYOUT_X = 65;
    
    private List<String> keystrokeHistory;
    
    private final JFXRadioButton radioBtn;
    private final TextArea codeArea;
    private final Label timestamp;

    public KeystrokeBox(Student student) {
        super(student);
        
        this.keystrokeHistory = new ArrayList<>();
        
        this.radioBtn = new JFXRadioButton(student.getFullname());
        
        this.codeArea = new TextArea();
        this.codeArea.setEditable(false);
        this.codeArea.setFont(Font.font("System", 10.0));
        this.codeArea.setPrefWidth(CODEAREA_WIDTH);
        this.codeArea.setPrefHeight(CODEAREA_HEIGHT);
        this.codeArea.setLayoutX(CODEAREA_LAYOUT_X);
        this.codeArea.setLayoutY(CODEAREA_LAYOUT_Y);
        
        this.timestamp = new Label("Waiting...");
        this.timestamp.setLayoutX(TIMELABEL_LAYOUT_X);
        this.timestamp.setLayoutY(TIMELABEL_LAYOUT_Y);
        
        this.getChildren().addAll(this.radioBtn, this.codeArea, this.timestamp);
    }
    
    
    @Override
    public void update(Keystroke keystroke) {
        this.keystrokeHistory = keystroke.getHistory();
        
        if (this.keystrokeHistory.size() > 0) {
            int lastIndex = this.keystrokeHistory.size() - 1;
            
            Platform.runLater(() -> {
                this.codeArea.setText(this.keystrokeHistory.get(lastIndex));
                this.timestamp.setText(keystroke.getTimestamp());
            });
        }
    }
    
    @Override
    public void markAsFinished() {
        Platform.runLater(() -> {
            this.timestamp.setText("Finished");
            this.timestamp.setLayoutX(FINISH_LABEL_LAYOUT_X);
            this.timestamp.getStyleClass().add("finish-label");
        });
    }
    
    public List<String> getKeystrokeHistory() {
        return this.keystrokeHistory;
    }
    
    public JFXRadioButton getRadioBtn() {
        return this.radioBtn;
    }
    
}