package hacklympics.teacher.pages;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.List;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;
import javafx.event.ActionEvent;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import com.jfoenix.controls.JFXComboBox;
import com.hacklympics.api.user.Student;
import hacklympics.utility.Utils;


public class ProctorController implements Initializable {
    
    private List<Student> attendedStudents;
    private ObservableList<Student> normalGroup;
    private ObservableList<Student> specialGroup;
    
    private Timeline timeline;
    private int remainingTime;
    
    @FXML
    private Tab liveScreensTab;
    @FXML
    private Tab keystrokesTab;
    @FXML
    private StackPane dialogPane;
    @FXML
    private Label examLabel;
    @FXML
    private AnchorPane normalGrpPane;
    @FXML
    private AnchorPane specialGrpPane;
    @FXML
    private JFXComboBox groupBox;
    @FXML
    private JFXComboBox imgQualityBox;
    @FXML
    private JFXComboBox imgFreqBox;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        normalGroup = FXCollections.observableArrayList();
        normalGroup.setAll();
        
        specialGroup = FXCollections.observableArrayList();
        specialGroup.setAll();
    }

    
    @FXML
    public void moveToSpecialGrp(ActionEvent event) {
        
    }
    
    @FXML
    public void moveToNormalGrp(ActionEvent event) {
        
    }

    @FXML
    public void adjustLiveScreensParam(ActionEvent event) {
        
    }
    
    @FXML
    public void adjustKeystrokesParam(ActionEvent event) {
        
    }
    
    
    /**
     * Sets the exam label which shows the title of currently ongoing exam.
     * @param examTitle title of exam.
     */
    public void setExamLabel(String examTitle) {
        examLabel.setText(examTitle);
    }
    
    /**
     * Sets the exam label which shows the title of currently ongoing exam,
     * and shows the remaining time of the exam as well.
     * @param examTitle name of exam.
     * @param remainingTime remaining time of exam.
     */
    public void setExamLabel(String examTitle, int remainingTime) {
        examLabel.setText(String.format("%s (%s)", examTitle, Utils.formatTime(remainingTime)));
        
        this.remainingTime = remainingTime;
        this.timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateExamLabelTimer(examTitle)));
        this.timeline.setCycleCount(Timeline.INDEFINITE);
        this.timeline.playFromStart();
    }
    
    /**
     * Updates the remaining time in the exam label.
     */
    private void updateExamLabelTimer(String examTitle) {
        if (this.remainingTime > 0) {
            this.remainingTime--;
        }
        
        examLabel.setText(String.format("%s (%s)", examTitle, Utils.formatTime(this.remainingTime)));
    }
    
}