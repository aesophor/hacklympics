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
import hacklympics.teacher.TeacherController;
import hacklympics.utility.ConfirmDialog;
import hacklympics.utility.Utils;
import com.hacklympics.api.communication.Response;
import com.hacklympics.api.material.Exam;
import com.hacklympics.api.user.Student;
import com.hacklympics.api.session.Session;

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
     * Halts the specified exam.
     * Asks the user for confirmation for halting the exam prematurely.
     * If the user answers yes, the exam will end and he will be taken
     * back to the course/exam/problem page.
     */
    @FXML
    public void haltExam(ActionEvent event) {
        Exam currentExam = Session.getInstance().getCurrentExam();
        
        ConfirmDialog confirmation = new ConfirmDialog(
                dialogPane,
                "Halt Exam",
                "Once the exam is halted, all students will no longer be able "
              + "to submit their code to the server.\n\n"
              + "Halt the exam now?"
        );

        confirmation.getConfirmBtn().setOnAction((ActionEvent e) -> {
            Response halt = currentExam.halt();
            
            switch (halt.getStatusCode()) {
                case SUCCESS:
                    Session.getInstance().setCurrentExam(null);

                    TeacherController sc = (TeacherController) Session.getInstance().getMainController();
                    ProctorController cc = (ProctorController) sc.getControllers().get("Proctor");

                    // Reset the Proctor Page to its original state.
                    cc.stopExamLabelTimer();
                    cc.setExamLabel("No Exam Being Proctored");
                    
                    
                    // Take the user back to OngoingExams Page.
                    sc.showOngoingExams(event);
                    break;

                default:
                    break;
            }
            
            confirmation.close();
        });

        confirmation.show();
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
    
    /**
     * Stop the remaining time from updating.
     */
    public void stopExamLabelTimer() {
        this.timeline.stop();
    }
    
}