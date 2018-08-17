package com.hacklympics.student.code;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.List;
import java.io.File;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.util.Duration;
import javafx.event.Event;
import javafx.event.ActionEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import com.jfoenix.controls.JFXComboBox;
import com.kodedu.terminalfx.TerminalTab;
import com.kodedu.terminalfx.TerminalBuilder;
import com.kodedu.terminalfx.config.TerminalConfig;
import com.hacklympics.api.communication.Response;
import com.hacklympics.api.event.EventHandler;
import com.hacklympics.api.event.EventManager;
import com.hacklympics.api.event.EventType;
import com.hacklympics.api.event.exam.HaltExamEvent;
import com.hacklympics.api.event.proctor.AdjustKeystrokeParamEvent;
import com.hacklympics.api.event.proctor.AdjustSnapshotParamEvent;
import com.hacklympics.api.material.Answer;
import com.hacklympics.api.material.Exam;
import com.hacklympics.api.material.Problem;
import com.hacklympics.api.session.Session;
import com.hacklympics.api.user.Student;
import com.hacklympics.api.user.User;
import com.hacklympics.common.code.CodePatch;
import com.hacklympics.common.code.CodeUtils;
import com.hacklympics.common.dialog.AlertDialog;
import com.hacklympics.common.dialog.ConfirmDialog;
import com.hacklympics.student.StudentController;
import com.hacklympics.student.logging.KeystrokeLogger;
import com.hacklympics.student.logging.ScreenRecorder;
import com.hacklympics.utility.Utils;

public class CodeController implements Initializable {
	
    private TerminalConfig terminalConfig;
    private TerminalBuilder terminalBuilder;
    private TerminalTab terminal;

    private Timeline timeline;
    private int remainingTime;

    @FXML
    private TabPane fileTabPane;
    @FXML
    private TabPane terminalTabPane;
    @FXML
    private StackPane dialogPane;

    @FXML
    private Label filepathLabel;
    @FXML
    private Label examLabel;
    @FXML
    private JFXComboBox problemBox;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        terminalConfig = new TerminalConfig();
        terminalConfig.setBackgroundColor("#eff1f5");

        terminalBuilder = new TerminalBuilder(terminalConfig);
        terminal = terminalBuilder.newTerminal();
        terminal.getStyleClass().add("minimal-tab");
        terminalTabPane.getTabs().add(terminal);

        // Whenever the student switch to a new tab, compute the diff between
        // the texts in oldtab and newtab, and then update the filepath label.
        fileTabPane.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldTab, newTab) -> {
                	// Compute diff between the texts in the old and new tabs.
                	if (oldTab != null && newTab != null) {
                		String oldTabText = ((FileTab) oldTab).getStyledCodeArea().getText();
                    	String newTabText = ((FileTab) newTab).getStyledCodeArea().getText();
                    	CodePatch patch = CodeUtils.diff(oldTabText, newTabText);
                    	
                    	try {
                    		synchronized (PendingCodePatches.getInstance()) {
                    			PendingCodePatches.getInstance().add(Utils.serialize(patch));
                    		}
    					} catch (IOException e) {
    						e.printStackTrace();
    					}
                	}
                	
                    updateFilepathLabel();
        });

        // Close the terminal whenever user clicks on the code area.
        fileTabPane.setOnMouseClicked((Event event) -> {
            closeTerminal();
        });

        // Reset the Code Page to its original state if current exam is halted.
        setOnHaltExam((HaltExamEvent event) -> {
            if (Session.getInstance().isInExam() && event.isForCurrentExam()) {
                // Shutdown the snapshot and keystroke logging thread.
                ScreenRecorder.getInstance().shutdown();
                KeystrokeLogger.getInstance().shutdown();
                    
                // Reset the Proctor Page to its original state.
                Session.getInstance().setCurrentExam(null);

                Platform.runLater(() -> {
                    reset();

                    AlertDialog alert = new AlertDialog(
                            "Exam Halted",
                            "The current exam has been halted."
                    );

                    alert.show();
                });
            }
        });
        
        // Adjust the snapshot parameters and restart the snapshot thread
        // upon receiving an AdjustSnapshotParamEvent.
        setOnAdjustSnapshotParam((AdjustSnapshotParamEvent event) -> {
            if (Session.getInstance().isInExam() && event.isForCurrentExam()) {
                // Set the parameters.
                ScreenRecorder.getInstance().setQuality(event.getQuality());
                ScreenRecorder.getInstance().setFrequency(event.getFrequency());
            }
        });
        
        // Adjust the keystroke parameters and restart the keystroke logging
        // thread upon receiving an AdjustKeystrokeParamEvent.
        setOnAdjustKeystrokeParam((AdjustKeystrokeParamEvent event) -> {
            if (Session.getInstance().isInExam() && event.isForCurrentExam()) {
                // Set the parameters.
                KeystrokeLogger.getInstance().setFrequency(event.getFrequency());
            }
        });

        
        createFileTab();
    }

    @FXML
    public void newFile(ActionEvent events) {
        createFileTab();
    }

    @FXML
    public void openFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File...");
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("Java Source Code", "*.java"),
                new ExtensionFilter("All Files", "*.*")
        );

        File selected = fileChooser.showOpenDialog(fileTabPane.getScene().getWindow());

        // If the user selects nothing (e.g., clicking the cancel button),
        // return immediately.
        if (selected == null) {
            return;
        }

        // Try to open the file in a new tab.
        try {
            createFileTab().open(selected);
        } catch (IOException ioe) {
            fileTabPane.getTabs().remove(getSelectedFileTab());

            AlertDialog alert = new AlertDialog(
                    "Error",
                    "Unable to open the specified file."
            );

            alert.show();
        }
    }

    @FXML
    public void saveFile(ActionEvent event) {
        // If the current file is still an untitled file,
        // prompt the user to save it as a new file instead.
        if (getSelectedFileTab().getFile() == null) {
            saveFileAs(event);
            return;
        }

        // The code below will do the effective task of saving the file.
        try {
            getSelectedFileTab().save();
        } catch (IOException ioe) {
            AlertDialog alert = new AlertDialog(
                    "Error",
                    "Unable to write to the specified file."
            );

            alert.show();
        }
    }

    @FXML
    public void saveFileAs(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save As...");
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("Java Source Code", "*.java"),
                new ExtensionFilter("All Files", "*.*")
        );

        File selected = fileChooser.showSaveDialog(fileTabPane.getScene().getWindow());

        getSelectedFileTab().setFile(selected);
        saveFile(event);
    }

    @FXML
    public void closeFile(ActionEvent event) {
        // If the current file does not have any unsaved change,
        // close the tab directly.
        if (!getSelectedFileTab().unsaved()) {
            fileTabPane.getTabs().remove(getSelectedFileTab());
            return;
        }

        // Otherwise, show a ConfirmDialog to the user.
        ConfirmDialog dialog = new ConfirmDialog(
                "Unsaved Changes",
                "Do you want to close it without saving?"
        );

        dialog.getConfirmBtn().setOnAction((ActionEvent e) -> {
            fileTabPane.getTabs().remove(getSelectedFileTab());
            dialog.close();
        });

        dialog.show();
    }

    @FXML
    public void toggleTerminal(ActionEvent event) {
        if (terminalTabPane.isVisible()) {
            closeTerminal();
        } else {
            showTerminal();
        }
    }

    @FXML
    public void compile(ActionEvent event) throws IOException {
        // If current file is still unsaved, save it first.
        if (getSelectedFileTab().unsaved()) {
            saveFile(event);
        }

        showTerminal();

        String location = getSelectedFileTab().getFileLocation();
        String filepath = getSelectedFileTab().getAbsoluteFilePath();

        terminal.onTerminalFxReady(() -> {
            terminal.command(String.join(" ", "javac", "-cp", location, filepath, "\r"));
        });
    }

    @FXML
    public void execute(ActionEvent event) {
        showTerminal();

        String location = getSelectedFileTab().getFileLocation();
        String className = getSelectedFileTab().getFilename().split("[.]")[0];

        terminal.onTerminalFxReady(() -> {
            terminal.command(String.join(" ", "java", "-cp", location, className, "\r"));
        });
    }

    @FXML
    public void showHint(ActionEvent e) {
        Problem selectedProblem = (Problem) problemBox.getSelectionModel().getSelectedItem();

        if (selectedProblem == null) {
            AlertDialog alert = new AlertDialog(
                    "No Problem Selected",
                    "Please select a problem first."
            );

            alert.show();
            return;
        }

        AlertDialog hint = new AlertDialog(
                selectedProblem.getTitle(),
                selectedProblem.getDesc()
        );

        hint.show();
    }

    @FXML
    public void submit(ActionEvent event) {
        Student student = (Student) Session.getInstance().getCurrentUser();
        Exam selectedExam = Session.getInstance().getCurrentExam();
        Problem selectedProblem = (Problem) problemBox.getSelectionModel().getSelectedItem();

        if (selectedExam == null | selectedProblem == null) {
            AlertDialog alert = new AlertDialog(
                    "Alert",
                    "Please make sure both Exam and Problem are selected."
            );

            alert.show();
            return;
        }

        ConfirmDialog dialog = new ConfirmDialog(
                "Submit Answer",
                String.format(
                        "Submitting \"%s\" for \"%s\".\n\n"
                        + "Once submitted, you will NOT be able to revise it.\n"
                        + "Submit your code now?",
                        getSelectedFileTab().getFilename(),
                        selectedProblem
                )
        );

        dialog.getConfirmBtn().setOnAction((ActionEvent e) -> {
            Response create = Answer.create(
                    selectedExam.getCourseID(),
                    selectedExam.getExamID(),
                    selectedProblem.getProblemID(),
                    getSelectedFileTab().getFilename(),
                    getSelectedFileTab().getStyledCodeArea().getText(),
                    student.getUsername()
            );

            dialog.close();

            switch (create.getStatusCode()) {
                case SUCCESS:
                    Answer answer = new Answer(
                            selectedExam.getCourseID(),
                            selectedExam.getExamID(),
                            selectedProblem.getProblemID(),
                            (int) Double.parseDouble(create.getContent().get("id").toString())
                    );

                    validate(answer);
                    break;

                case ALREADY_SUBMITTED:
                    AlertDialog submitted = new AlertDialog(
                            "Alert",
                            "You've already submitted your code for this problem."
                    );

                    submitted.show();
                    break;

                default:
                    AlertDialog error = new AlertDialog(
                            "Submission Error",
                            "ErrorCode: " + create.getStatusCode().toString()
                    );

                    error.show();
                    break;
            }
        });

        dialog.show();
    }

    private void validate(Answer answer) {
        Response validate = answer.validate();

        switch (validate.getStatusCode()) {
            case SUCCESS:
                AlertDialog correct = new AlertDialog(
                        "Congratulations",
                        "Your code works correctly. Nice work!"
                );

                correct.show();
                break;

            case INCORRECT_ANSWER:
                AlertDialog failed = new AlertDialog(
                        "Sorry",
                        "It seems that your code doesn't work out,\n\n"
                        + "keep going!"
                );

                failed.show();
                break;

            default:
                AlertDialog error = new AlertDialog(
                        "Validation Error",
                        "ErrorCode: " + validate.getStatusCode().toString()
                );

                error.show();
                break;
        }
    }

    @FXML
    public void leave(ActionEvent event) {
        Exam currentExam = Session.getInstance().getCurrentExam();
        User currentUser = Session.getInstance().getCurrentUser();

        // If the user is trying to leave an exam, but the user hasn't
        // attended to any exam yet, block this attempt and alert the user.
        if (currentExam == null) {
            AlertDialog alert = new AlertDialog(
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
                "Leave Exam",
                "Once left, you will not be able to enter again!\n\n"
                + "Leave the exam now?"
        );

        dialog.getConfirmBtn().setOnAction((ActionEvent e) -> {
            Response leave = currentUser.leave(currentExam);

            switch (leave.getStatusCode()) {
                case SUCCESS:
                    // Shutdown the snapshot and keystroke logging thread.
                    ScreenRecorder.getInstance().shutdown();
                    KeystrokeLogger.getInstance().shutdown();
                    
                    // Clear session data and reset the Code Page to
                    // its original state.
                    Session.getInstance().setCurrentExam(null);
                    reset();

                    // Take the user back to OngoingExams Page.
                    StudentController sc = (StudentController) Session.getInstance().getMainController();
                    sc.showOngoingExams(event);
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
        setProblemBox(null);
        stopExamLabelTimer();
        setExamLabel("No Exam Being Taken");
    }

    /**
     * Creates a new tab in FileTabPane.
     * @return the newly created FileTab.
     */
    private FileTab createFileTab() {
        // Create a new tab "c", add it to our fileTabPane
        // and select (switch to) that tab.
        FileTab tab = new FileTab();
        fileTabPane.getTabs().add(tab);
        fileTabPane.getSelectionModel().select(tab);

        // Overrides the default behavior of the close button on each tab by
        // consuming the original event, and then call my own closing method.
        tab.setOnCloseRequest((Event event) -> {
            // Buggy here.
            event.consume();
            closeFile(null);
        });
        
        return tab;
    }

    /**
     * Gets the currently selected tab in FileTabPane.
     * @return the currently selected tab.
     */
    public FileTab getSelectedFileTab() {
        return ((FileTab) fileTabPane.getSelectionModel().getSelectedItem());
    }

    /**
     * Updates the filepath label on the bottom left.
     */
    private void updateFilepathLabel() {
        FileTab current = getSelectedFileTab();
        filepathLabel.setText((current == null) ? "" : current.getAbsoluteFilePath());
    }

    /**
     * Shows the terminal panel.
     */
    private void showTerminal() {
        if (terminalTabPane.isVisible()) {
            return;
        }

        terminalTabPane.setVisible(true);
        terminalTabPane.setMouseTransparent(false);
    }

    /**
     * Closes the terminal panel.
     */
    private void closeTerminal() {
        if (!terminalTabPane.isVisible()) {
            return;
        }

        terminalTabPane.setVisible(false);
        terminalTabPane.setMouseTransparent(true);
    }

    /**
     * Sets the exam label which shows the title of currently ongoing exam.
     * @param examTitle title of exam.
     */
    public void setExamLabel(String examTitle) {
        examLabel.setText(examTitle);
    }

    /**
     * Sets the exam label which shows the title of currently ongoing exam, and
     * shows the remaining time of the exam as well.
     * @param examTitle name of exam.
     * @param remainingTime remaining time of exam.
     */
    public void setExamLabel(String examTitle, int remainingTime) {
        examLabel.setText(String.format("%s (%s)", examTitle, Utils.formatTime(remainingTime)));

        this.remainingTime = remainingTime;
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateExamLabelTimer(examTitle)));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.playFromStart();
    }

    /**
     * Updates the remaining time in the exam label.
     */
    private void updateExamLabelTimer(String examTitle) {
        if (remainingTime > 0) {
            remainingTime--;
        }

        examLabel.setText(String.format("%s (%s)", examTitle, Utils.formatTime(remainingTime)));
    }

    /**
     * Stop the remaining time from updating.
     */
    public void stopExamLabelTimer() {
        timeline.stop();
    }

    /**
     * Adds all problems of a exam to the problem ComboBox.
     * @param problems all problems of currently ongoing exams.
     */
    public void setProblemBox(List<Problem> problems) {
        problemBox.getItems().clear();

        if (problems != null) {
            problemBox.getItems().addAll(problems);
        }
    }

    private void setOnHaltExam(EventHandler<HaltExamEvent> listener) {
        EventManager.getInstance().addEventHandler(EventType.HALT_EXAM, listener);
    }
    
    private void setOnAdjustSnapshotParam(EventHandler<AdjustSnapshotParamEvent> listener) {
        EventManager.getInstance().addEventHandler(EventType.ADJUST_SNAPSHOT_PARAM, listener);
    }
    
    private void setOnAdjustKeystrokeParam(EventHandler<AdjustKeystrokeParamEvent> listener) {
        EventManager.getInstance().addEventHandler(EventType.ADJUST_KEYSTROKE_PARAM, listener);
    }

}