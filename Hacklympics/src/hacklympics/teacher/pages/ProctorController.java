package hacklympics.teacher.pages;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;
import javafx.event.ActionEvent;
import javafx.scene.control.Tab;
import javafx.scene.layout.StackPane;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import hacklympics.teacher.TeacherController;
import hacklympics.utility.dialog.AlertDialog;
import hacklympics.utility.dialog.ConfirmDialog;
import hacklympics.utility.Utils;
import com.hacklympics.api.communication.Response;
import com.hacklympics.api.event.EventHandler;
import com.hacklympics.api.event.EventManager;
import com.hacklympics.api.event.EventType;
import com.hacklympics.api.event.exam.AttendExamEvent;
import com.hacklympics.api.event.exam.HaltExamEvent;
import com.hacklympics.api.event.exam.LeaveExamEvent;
import com.hacklympics.api.event.snapshot.NewSnapshotEvent;
import com.hacklympics.api.material.Exam;
import com.hacklympics.api.proctor.Snapshot;
import com.hacklympics.api.user.User;
import com.hacklympics.api.user.Student;
import com.hacklympics.api.session.Session;
import hacklympics.utility.SnapshotBox;
import hacklympics.utility.SnapshotGrpVBox;
import java.io.IOException;
import javafx.application.Platform;
import javafx.scene.control.ScrollPane;

public class ProctorController implements Initializable {

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
    private ScrollPane genericGrpPane;
    @FXML
    private ScrollPane specialGrpPane;

    private SnapshotGrpVBox genericGrpBox;
    private SnapshotGrpVBox specialGrpBox;

    @FXML
    private JFXComboBox groupBox;
    @FXML
    private JFXComboBox imgQualityBox;
    @FXML
    private JFXComboBox imgFreqBox;
    @FXML
    private JFXButton leaveExamBtn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.genericGrpBox = new SnapshotGrpVBox();
        this.genericGrpPane.setContent(this.genericGrpBox);

        this.specialGrpBox = new SnapshotGrpVBox();
        this.specialGrpPane.setContent(this.specialGrpBox);

        // Clear all SnapshotBoxes of current exam and restore the
        // Proctor Page to its original state if current exam is halted.
        this.setOnHaltExam((HaltExamEvent event) -> {
            if (Session.getInstance().isInExam()) {
                int eventExamID = event.getExam().getExamID();
                int currentExamID = Session.getInstance().getCurrentExam().getExamID();

                if (eventExamID == currentExamID) {
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
            }
        });

        // Create a new SnapshotBox for the student who just arrived,
        // and add it to the generic group by default.
        this.setOnAttendExam((AttendExamEvent event) -> {
            if (Session.getInstance().isInExam()) {
                int eventExamID = event.getExam().getExamID();
                int currentExamID = Session.getInstance().getCurrentExam().getExamID();

                if (eventExamID == currentExamID) {
                    SnapshotBox box = new SnapshotBox((Student) event.getUser());
                    this.genericGrpBox.add(box);

                    Platform.runLater(() -> {
                        this.genericGrpBox.rearrange();
                    });
                }
            }
        });

        // Remove the SnapshotBox for the student who just left.
        this.setOnLeaveExam((LeaveExamEvent event) -> {
            if (Session.getInstance().isInExam()) {
                int eventExamID = event.getExam().getExamID();
                int currentExamID = Session.getInstance().getCurrentExam().getExamID();

                if (eventExamID == currentExamID) {
                    SnapshotBox box = this.genericGrpBox.get((Student) event.getUser());
                    this.genericGrpBox.remove(box);

                    Platform.runLater(() -> {
                        this.genericGrpBox.rearrange();
                    });
                }
            }
        });

        // Updates the SnapshotBox when a NewSnapshotEvent arrives.
        this.setOnNewSnapshot((NewSnapshotEvent event) -> {
            int eventExamID = event.getSnapshot().getExamID();
            int currentExamID = Session.getInstance().getCurrentExam().getExamID();

            if (eventExamID == currentExamID) {
                Snapshot snapshot = event.getSnapshot();
                SnapshotBox box = this.genericGrpBox.get(snapshot.getStudentUsername());

                try {
                    box.update(snapshot);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    @FXML
    public void moveToSpecialGrp(ActionEvent event) {

    }

    @FXML
    public void moveToGenericGrp(ActionEvent event) {

    }

    @FXML
    public void adjustLiveScreensParam(ActionEvent event) {

    }

    @FXML
    public void adjustKeystrokesParam(ActionEvent event) {

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
        ConfirmDialog confirmation = new ConfirmDialog(
                dialogPane,
                "Halt Exam",
                "Once the exam is halted, all students will no longer be able "
                + "to submit their code to the server.\n\n"
                + "Halt the exam now?"
        );

        confirmation.getConfirmBtn().setOnAction((ActionEvent e) -> {
            // Unlike leaving an exam, halting an exam will force all
            // all students and teachers to leave the exam immediately.
            currentExam.halt();
            confirmation.close();
        });

        confirmation.show();
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
        ConfirmDialog confirmation = new ConfirmDialog(
                dialogPane,
                "Leave Exam",
                "As long as the exam is still ongoing, you can come back later at anytime.\n\n"
                + "Leave the exam now?"
        );

        confirmation.getConfirmBtn().setOnAction((ActionEvent e) -> {
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

            confirmation.close();
        });

        confirmation.show();
    }

    /**
     * Resets the Code page to its original state.
     */
    private void reset() {
        this.genericGrpBox.clear();
        this.specialGrpBox.clear();
        this.genericGrpBox.rearrange();
        this.specialGrpBox.rearrange();

        this.disableLeaveBtn();
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
        this.leaveExamBtn.setDisable(false);
        this.leaveExamBtn.setText("Halt");
        this.leaveExamBtn.setOnAction((ActionEvent event) -> haltExam(event));
    }

    /**
     * Enables and renders the leave exam button as "Leave" Exam button.
     */
    public void enableLeaveExamBtn() {
        this.leaveExamBtn.setDisable(false);
        this.leaveExamBtn.setText("Leave");
        this.leaveExamBtn.setOnAction((ActionEvent event) -> leaveExam(event));
    }

    /**
     * Disables the ability to click on the leave exam button.
     */
    public void disableLeaveBtn() {
        this.leaveExamBtn.setDisable(true);
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

}
