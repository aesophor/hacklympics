package hacklympics.teacher.pages;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.application.Platform;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;
import javafx.event.ActionEvent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXProgressBar;
import hacklympics.teacher.TeacherController;
import hacklympics.utility.dialog.AlertDialog;
import hacklympics.utility.dialog.ConfirmDialog;
import hacklympics.utility.Utils;
import com.hacklympics.api.communication.Response;
import com.hacklympics.api.communication.StatusCode;
import com.hacklympics.api.event.EventHandler;
import com.hacklympics.api.event.EventManager;
import com.hacklympics.api.event.EventType;
import com.hacklympics.api.event.exam.AttendExamEvent;
import com.hacklympics.api.event.exam.HaltExamEvent;
import com.hacklympics.api.event.exam.LeaveExamEvent;
import com.hacklympics.api.event.proctor.NewKeystrokeEvent;
import com.hacklympics.api.event.proctor.NewSnapshotEvent;
import com.hacklympics.api.proctor.Keystroke;
import com.hacklympics.api.material.Exam;
import com.hacklympics.api.proctor.Snapshot;
import com.hacklympics.api.user.User;
import com.hacklympics.api.user.Student;
import com.hacklympics.api.session.Session;
import com.jfoenix.controls.JFXTextArea;
import hacklympics.utility.proctor.SnapshotManager;
import static hacklympics.utility.FileTab.computeHighlighting;
import hacklympics.utility.proctor.KeystrokeBox;
import hacklympics.utility.proctor.KeystrokeManager;
import hacklympics.utility.proctor.KeystrokeStudentsVBox;
import hacklympics.utility.proctor.SnapshotBox;
import hacklympics.utility.proctor.SnapshotGroup;
import hacklympics.utility.proctor.SnapshotGrpVBox;
import javafx.scene.layout.HBox;
import static javafx.scene.layout.Region.USE_PREF_SIZE;
import javafx.scene.layout.VBox;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

public class ProctorController implements Initializable {

    private Timeline timeline;
    private int remainingTime;
    
    private SnapshotGrpVBox snapshotGenGrpVBox;
    private SnapshotGrpVBox snapshotSpeGrpVBox;
    
    private KeystrokeStudentsVBox keystrokeStudentsVBox;
    
    @FXML
    private StackPane dialogPane;
    @FXML
    private Label examLabel;
    @FXML
    private JFXButton leaveOrHaltBtn;

    @FXML
    private ScrollPane snapshotGenGrpPane;
    @FXML
    private ScrollPane snapshotSpeGrpPane;
    @FXML
    private JFXComboBox groupBox;
    @FXML
    private JFXComboBox imgQualityBox;
    @FXML
    private JFXComboBox imgFrequencyBox;
    
    @FXML
    private ScrollPane keystrokeStudentsPane;
    @FXML
    private ScrollPane keystrokePlaybackPane;
    @FXML
    private JFXTextArea codeArea;
    @FXML
    private JFXProgressBar keystrokePlaybackBar;
    @FXML
    private JFXComboBox keyFrequencyBox;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // First we will cache to generic and special group VBox so that
        // we can write code with better readability later.
        this.snapshotGenGrpVBox = SnapshotGroup.GENERIC.getSnapshotGrpVBox();
        this.snapshotSpeGrpVBox = SnapshotGroup.SPECIAL.getSnapshotGrpVBox();
        this.keystrokeStudentsVBox = new KeystrokeStudentsVBox();
        
        // Initialize the generic and special group pane in LiveScreens Tab,
        // as well as keystroke students pane in Keystroke Tab.
        this.snapshotGenGrpPane.setContent(this.snapshotGenGrpVBox);
        this.snapshotSpeGrpPane.setContent(this.snapshotSpeGrpVBox);
        this.keystrokeStudentsPane.setContent(this.keystrokeStudentsVBox);
        
        
        // Add the generic and special group VBoxes to the groupBox.
        // This part is tricky: we cannot simply add a VBox to a combobox
        // as its content (since SnapshotGrpVBox extends VBox), 
        // toString method won't be fully functional! you may try it out
        // for yourself. So I created an enum to wrap the SnapshotGrpVBox.
        this.groupBox.getItems().add(SnapshotGroup.GENERIC);
        this.groupBox.getItems().add(SnapshotGroup.SPECIAL);

        // Populate the imgQualityBox and imgFrequencyBox.
        this.imgQualityBox.getItems().addAll(SnapshotManager.QUALITY_OPTIONS);
        this.imgFrequencyBox.getItems().addAll(SnapshotManager.FREQUENCY_OPTIONS);
        
        // When user selects a certain group, display the corresponding parameters.
        this.groupBox.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    SnapshotGroup selectedGroup = (SnapshotGroup) this.groupBox.getSelectionModel().getSelectedItem();
                    SnapshotGrpVBox vbox = selectedGroup.getSnapshotGrpVBox();
                    
                    this.imgQualityBox.getSelectionModel().select((Double) vbox.getQuality());
                    this.imgFrequencyBox.getSelectionModel().select((Integer) vbox.getFrequency());
                }
        );
        
        
        // Populate the keyFrequencyBox.
        this.keyFrequencyBox.getItems().addAll(KeystrokeManager.FREQUENCY_OPTIONS);
        
        /************************************************************************
        // Place a CodeArea into keystrokePlaybackPane.
        codeArea = new CodeArea();
        codeArea.getStyleClass().add("code-area");
        //codeArea.setEditable(false);
        codeArea.setPrefHeight(USE_PREF_SIZE);
        codeArea.getStylesheets().add("/resources/JavaKeywords.css");

        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
        codeArea.multiPlainChanges()
                .successionEnds(java.time.Duration.ofMillis(1))
                .subscribe(ignore -> codeArea.setStyleSpans(0, computeHighlighting(codeArea.getText())));

        // Add the code area we just created into a VBox.
        HBox hbox = new HBox();
        hbox.getChildren().add(codeArea);
        
        VBox vbox = new VBox();
        vbox.setPrefWidth(353.0);
        vbox.setPrefHeight(451.0);
        //vbox.getStyleClass().add("code-vbox");
        vbox.getChildren().add(hbox);
        this.keystrokePlaybackPane.setContent(vbox);
        ************************************************************************/
        
        
        // Clear all SnapshotBoxes of current exam and restore the
        // Proctor Page to its original state if current exam is halted.
        this.setOnHaltExam((HaltExamEvent event) -> {
            if (Session.getInstance().isInExam() && event.isForCurrentExam()) {
                Session.getInstance().setCurrentExam(null);

                // Reset the Proctor Page to its original state.
                Platform.runLater(() -> {
                    this.reset();

                    AlertDialog alert = new AlertDialog(
                            dialogPane,
                            "Exam Halted",
                            "The current exam has been halted."
                    );

                    alert.show();
                });
            }
        });

        // Create a new SnapshotBox for the student who just arrived,
        // and add it to the generic group by default.
        this.setOnAttendExam((AttendExamEvent event) -> {
            if (Session.getInstance().isInExam() && event.isForCurrentExam()) {
                // Add a SnapshotBox to generic group VBox in LiveScreen Tab.
                SnapshotBox snapshotBox = new SnapshotBox((Student) event.getUser());
                this.snapshotGenGrpVBox.add(snapshotBox);
                
                // Add a KeystrokeBox to keystroke students VBox in Keystroke Tab.
                KeystrokeBox keystrokeBox = new KeystrokeBox((Student) event.getUser());
                this.keystrokeStudentsVBox.add(keystrokeBox);

                Platform.runLater(() -> {
                    this.snapshotGenGrpVBox.rearrange();
                    this.keystrokeStudentsVBox.rearrange();
                });
            }
        });

        // Remove the SnapshotBox for the student who just left.
        this.setOnLeaveExam((LeaveExamEvent event) -> {
            if (Session.getInstance().isInExam() && event.isForCurrentExam()) {
                // Remove the SnapshotBox of the event-specified student from LiveScreen Tab.
                SnapshotBox snapshotBox = this.snapshotGenGrpVBox.get((Student) event.getUser());
                this.snapshotGenGrpVBox.remove(snapshotBox);

                // Remove the KeystrokeBox from Keystroke Tab also.
                KeystrokeBox keystrokeBox = this.keystrokeStudentsVBox.get((Student) event.getUser());
                this.keystrokeStudentsVBox.remove(keystrokeBox);
                
                Platform.runLater(() -> {
                    this.snapshotGenGrpVBox.rearrange();
                    this.keystrokeStudentsVBox.rearrange();
                });
            }
        });

        // Updates the SnapshotBox when a NewSnapshotEvent arrives.
        // The target SnapshotBox could either be in generic or special group.
        this.setOnNewSnapshot((NewSnapshotEvent event) -> {
            if (Session.getInstance().isInExam() && event.isForCurrentExam()) {
                Snapshot snapshot = event.getSnapshot();
                
                try {
                    // Find out in which group the student belongs to,
                    // and perform an update on that SnapshotBox.
                    SnapshotGroup.getSnapshotBox(snapshot.getStudentUsername()).update(snapshot);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        
        // Updates the KeystrokeBox when a NewKeystrokeEvent arrives.
        this.setOnNewKeystroke((NewKeystrokeEvent event) -> {
            if (Session.getInstance().isInExam() && event.isForCurrentExam()) {
                Keystroke keystroke = event.getKeystroke();
                KeystrokeBox box = this.keystrokeStudentsVBox.get(keystroke.getStudentUsername());
                
                box.update(keystroke);
            }
        });
    }

    @FXML
    public void moveToSpecialGrp(ActionEvent event) {
        // Get the selected SnapshotBoxes from the generic group.
        List<SnapshotBox> genGrpSelectedBoxes = this.snapshotGenGrpVBox.getSelectedItems();

        if (genGrpSelectedBoxes == null) {
            AlertDialog alert = new AlertDialog(
                    dialogPane,
                    "Alert",
                    "You have not selected any students to move."
            );

            alert.show();
            return;
        }
        
        // Move them from generic group to special group.
        this.snapshotGenGrpVBox.removeAll(genGrpSelectedBoxes);
        this.snapshotSpeGrpVBox.addAll(genGrpSelectedBoxes);

        this.snapshotGenGrpVBox.rearrange();
        this.snapshotSpeGrpVBox.rearrange();

        
        // Apply the special group snapshot parameters to the SnapshotBoxes
        // we just moved.
        List<Student> students = new ArrayList<>();
        for (SnapshotBox box : genGrpSelectedBoxes) {
            students.add(box.getStudent());
        }

        Snapshot.adjustParam(
                Session.getInstance().getCurrentExam().getCourseID(),
                Session.getInstance().getCurrentExam().getExamID(),
                students,
                this.snapshotSpeGrpVBox.getQuality(),
                this.snapshotSpeGrpVBox.getFrequency()
        );
    }

    @FXML
    public void moveToGenericGrp(ActionEvent event) {
        // Get the selected SnapshotBoxes from the special group.
        List<SnapshotBox> speGrpSelectedBoxes = this.snapshotSpeGrpVBox.getSelectedItems();

        if (speGrpSelectedBoxes == null) {
            AlertDialog alert = new AlertDialog(
                    dialogPane,
                    "Alert",
                    "You have not selected any students to move."
            );

            alert.show();
            return;
        }
        
        // Move them from generic group to special group.
        this.snapshotSpeGrpVBox.removeAll(speGrpSelectedBoxes);
        this.snapshotGenGrpVBox.addAll(speGrpSelectedBoxes);

        this.snapshotSpeGrpVBox.rearrange();
        this.snapshotGenGrpVBox.rearrange();

        
        // Apply the generic group snapshot parameters to the SnapshotBoxes
        // we just moved.
        List<Student> students = new ArrayList<>();
        for (SnapshotBox box : speGrpSelectedBoxes) {
            students.add(box.getStudent());
        }

        Snapshot.adjustParam(
                Session.getInstance().getCurrentExam().getCourseID(),
                Session.getInstance().getCurrentExam().getExamID(),
                students,
                this.snapshotGenGrpVBox.getQuality(),
                this.snapshotGenGrpVBox.getFrequency()
        );
    }

    @FXML
    public void adjustLiveScreensParam(ActionEvent event) {
        // If the user tries to broadcast the command to adjust snapshot
        // parameters to all students in the exam, but the user is currently
        // not in any exam, block this attempt and alert the user.
        if (!Session.getInstance().isInExam()) {
            AlertDialog alert = new AlertDialog(
                    dialogPane,
                    "Alert",
                    "You can only adjust parameters while in exam."
            );

            alert.show();
            return;
        }
        
        // If everything is fine, then we will proceed.
        // First, check which group the user is trying to adjust.
        SnapshotGroup selectedGroup = (SnapshotGroup) this.groupBox.getSelectionModel().getSelectedItem();
        SnapshotGrpVBox vbox = selectedGroup.getSnapshotGrpVBox();

        // Get the parameters from the ComboBoxes.
        Double selectedQuality = (Double) this.imgQualityBox.getSelectionModel().getSelectedItem();
        Integer selectedFrequency = (Integer) this.imgFrequencyBox.getSelectionModel().getSelectedItem();

        // Broadcast the command to adjust snapshot parameters
        // to the students within the user-selected group.
        Response adjustParam = Snapshot.adjustParam(
                Session.getInstance().getCurrentExam().getCourseID(),
                Session.getInstance().getCurrentExam().getExamID(),
                vbox.getStudents(),
                selectedQuality,
                selectedFrequency
        );

        if (adjustParam.getStatusCode() == StatusCode.SUCCESS) {
            vbox.setQuality(selectedQuality);
            vbox.setFrequency(selectedFrequency);

            AlertDialog alert = new AlertDialog(
                    dialogPane,
                    "Parameters Adjusted",
                    "The snapshot parameters has been adjusted."
            );

            alert.show();
        } else {
            AlertDialog alert = new AlertDialog(
                    dialogPane,
                    "Alert",
                    "Failed to adjust the snapshot parameters."
            );

            alert.show();
        }
    }

    
    @FXML
    public void playKeystroke(ActionEvent event) throws InterruptedException {
        // If the user tries to play the keystroke history of a student,
        // but the user is currently not in any exam, block this attempt
        // and alert the user.
        if (!Session.getInstance().isInExam()) {
            AlertDialog alert = new AlertDialog(
                    dialogPane,
                    "Alert",
                    "Keystroke playback is only available during exam."
            );

            alert.show();
            return;
        }
        
        // If the user has not selected any student for playback,
        // block this attempt and alert the user.
        KeystrokeBox selectedBox = this.keystrokeStudentsVBox.getSelectedItem();
        
        if (selectedBox == null) {
            AlertDialog alert = new AlertDialog(
                    dialogPane,
                    "Alert",
                    "You haven't selected any student yet."
            );

            alert.show();
            return;
        }
        
        // If everything is fine, then we will proceed.
        // Execute a new thread for keystroke playback, since we will
        // need to call Thread.sleep() during playback.
        // If we call Thread.sleep() on JavaFX thread, the UI will hang.
        List<String> keystrokeHistory = selectedBox.getKeystrokeHistory();
        
        new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < keystrokeHistory.size(); i++) {
                    double currentProgress = ((double) i) / keystrokeHistory.size();
                    int currentIndex = i;
                    
                    Platform.runLater(() -> {
                        codeArea.setText(keystrokeHistory.get(currentIndex));
                        keystrokePlaybackBar.setProgress(currentProgress);
                    });
                
                    try {
                        // playback speed * 1000.
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }.start();
    }
    
    @FXML
    public void adjustKeystrokesParam(ActionEvent event) {
        // If the user tries to broadcast the command to adjust snapshot
        // parameters to all students in the exam, but the user is currently
        // not in any exam, block this attempt and alert the user.
        if (!Session.getInstance().isInExam()) {
            AlertDialog alert = new AlertDialog(
                    dialogPane,
                    "Alert",
                    "You can only adjust parameters while in exam."
            );

            alert.show();
            return;
        }
        
        // If everything is fine, then we will proceed.
        // Get the parameters from the ComboBoxes.
        Integer selectedFrequency = (Integer) this.keyFrequencyBox.getSelectionModel().getSelectedItem();

        // Broadcast the command to adjust snapshot parameters
        // to the students within the user-selected group.
        Response adjustParam = Keystroke.adjustParam(
                Session.getInstance().getCurrentExam().getCourseID(),
                Session.getInstance().getCurrentExam().getExamID(),
                this.keystrokeStudentsVBox.getStudents(),
                selectedFrequency
        );

        if (adjustParam.getStatusCode() == StatusCode.SUCCESS) {
            this.keystrokeStudentsVBox.setFrequency(selectedFrequency);

            AlertDialog alert = new AlertDialog(
                    dialogPane,
                    "Parameters Adjusted",
                    "The keystroke parameters has been adjusted."
            );

            alert.show();
        } else {
            AlertDialog alert = new AlertDialog(
                    dialogPane,
                    "Alert",
                    "Failed to adjust the keystroke parameters."
            );

            alert.show();
        }
    }
    

    /**
     * Halts the specified exam (for the teacher who launches the exam). Asks
     * the user for confirmation for halting the exam prematurely. If the user
     * answers yes, the exam will end and he will be taken back to the
     * course/exam/problem page.
     */
    public void haltExam(ActionEvent event) {
        // If the user is trying to halt an exam, but the user hasn't
        // attended to any exam yet, block this attempt and alert the user.
        // This section of code should never get executed, since
        // the halt exam button is disabled by default.
        Exam currentExam = Session.getInstance().getCurrentExam();

        if (currentExam == null) {
            AlertDialog alert = new AlertDialog(
                    dialogPane,
                    "Alert",
                    "You haven't attended to any exam yet.\n\n"
                    + "You can launch your exam in My Courses & Exam, or "
                    + "attend to exams of other teachers in Ongoing Exams."
            );

            alert.show();
            return;
        }

        // If everything alright, then ask the user for confirmation.
        // If yes, then we will proceed.
        ConfirmDialog dialog = new ConfirmDialog(
                dialogPane,
                "Halt Exam",
                "Once the exam is halted, all students will no longer be able "
              + "to submit their code to the server.\n\n"
              + "Halt the exam now?"
        );

        dialog.getConfirmBtn().setOnAction((ActionEvent e) -> {
            // Unlike leaving an exam, halting an exam will force all
            // all students and teachers to leave the exam immediately.
            currentExam.halt();
            dialog.close();
        });

        dialog.show();
    }

    /**
     * Leaves the specified exam (for all teachers except the one who launches
     * the exam). Asks the user for confirmation for leaving the exam. If the
     * user answers yes, the exam will end and he will be taken back to the
     * OngoingExams page.
     */
    public void leaveExam(ActionEvent event) {
        Exam currentExam = Session.getInstance().getCurrentExam();
        User currentUser = Session.getInstance().getCurrentUser();

        // If the user is trying to leave an exam, but the user hasn't
        // attended to any exam yet, block this attempt and alert the user.
        if (currentExam == null) {
            AlertDialog alert = new AlertDialog(
                    dialogPane,
                    "Alert",
                    "You haven't attended to any exam yet.\n\n"
                  + "You can attend to your exam by selecting any exam in Ongoing Exams."
            );
        
            alert.show();
            return;
        }

        // If everything alright, then ask the user for confirmation.
        // If yes, then we will proceed.
        ConfirmDialog dialog = new ConfirmDialog(
                dialogPane,
                "Leave Exam",
                "As long as the exam is still ongoing, you can come back later at anytime.\n\n"
              + "Leave the exam now?"
        );

        dialog.getConfirmBtn().setOnAction((ActionEvent e) -> {
            Response leave = currentUser.leave(currentExam);

            switch (leave.getStatusCode()) {
                case SUCCESS:
                    // Clear session data and reset the Code Page to
                    // its original state.
                    Session.getInstance().setCurrentExam(null);
                    this.reset();

                    // Take the user back to OngoingExams Page.
                    TeacherController tc = (TeacherController) Session.getInstance().getMainController();
                    tc.showOngoingExams(event);
                    break;

                default:
                    break;
            }

            dialog.close();
        });

        dialog.show();
    }

    /**
     * Resets the Code page to its original state.
     */
    private void reset() {
        this.snapshotGenGrpVBox.clear();
        this.snapshotSpeGrpVBox.clear();
        this.snapshotGenGrpVBox.rearrange();
        this.snapshotSpeGrpVBox.rearrange();
        
        this.keystrokeStudentsVBox.clear();
        this.keystrokeStudentsVBox.rearrange();
        
        this.codeArea.clear();

        this.disableLeaveOrHaltBtn();
        this.stopExamLabelTimer();
        this.setExamLabel("No Exam Being Proctored");
    }

    /**
     * Sets the exam label which shows the title of currently ongoing exam.
     *
     * @param examTitle title of exam.
     */
    public void setExamLabel(String examTitle) {
        this.examLabel.setText(examTitle);
    }

    /**
     * Sets the exam label which shows the title of currently ongoing exam, and
     * shows the remaining time of the exam as well.
     *
     * @param examTitle name of exam.
     * @param remainingTime remaining time of exam (in seconds).
     */
    public void setExamLabel(String examTitle, int remainingTime) {
        this.examLabel.setText(String.format("%s (%s)", examTitle, Utils.formatTime(remainingTime)));

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

        this.examLabel.setText(String.format("%s (%s)", examTitle, Utils.formatTime(this.remainingTime)));
    }

    /**
     * Stop the remaining time from updating.
     */
    public void stopExamLabelTimer() {
        this.timeline.stop();
    }

    /**
     * Enables and renders the leave exam button as "Halt" Exam button.
     */
    public void enableHaltExamBtn() {
        this.leaveOrHaltBtn.setDisable(false);
        this.leaveOrHaltBtn.setText("Halt");
        this.leaveOrHaltBtn.setOnAction((ActionEvent event) -> haltExam(event));
    }

    /**
     * Enables and renders the leave exam button as "Leave" Exam button.
     */
    public void enableLeaveExamBtn() {
        this.leaveOrHaltBtn.setDisable(false);
        this.leaveOrHaltBtn.setText("Leave");
        this.leaveOrHaltBtn.setOnAction((ActionEvent event) -> leaveExam(event));
    }

    /**
     * Disables the ability to click on the leave exam button.
     */
    public void disableLeaveOrHaltBtn() {
        this.leaveOrHaltBtn.setDisable(true);
    }

    private void setOnHaltExam(EventHandler<HaltExamEvent> listener) {
        EventManager.getInstance().addEventHandler(EventType.HALT_EXAM, listener);
    }

    private void setOnAttendExam(EventHandler<AttendExamEvent> listener) {
        EventManager.getInstance().addEventHandler(EventType.ATTEND_EXAM, listener);
    }

    private void setOnLeaveExam(EventHandler<LeaveExamEvent> listener) {
        EventManager.getInstance().addEventHandler(EventType.LEAVE_EXAM, listener);
    }

    private void setOnNewSnapshot(EventHandler<NewSnapshotEvent> listener) {
        EventManager.getInstance().addEventHandler(EventType.NEW_SNAPSHOT, listener);
    }
    
    private void setOnNewKeystroke(EventHandler<NewKeystrokeEvent> listener) {
        EventManager.getInstance().addEventHandler(EventType.NEW_KEYSTROKE, listener);
    }
    
}