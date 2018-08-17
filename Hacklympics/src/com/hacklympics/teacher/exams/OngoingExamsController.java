package com.hacklympics.teacher.exams;

import com.hacklympics.api.communication.Response;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.List;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import com.jfoenix.controls.JFXTextField;
import com.hacklympics.api.event.EventManager;
import com.hacklympics.api.event.EventType;
import com.hacklympics.api.event.EventHandler;
import com.hacklympics.api.event.exam.LaunchExamEvent;
import com.hacklympics.api.event.exam.HaltExamEvent;
import com.hacklympics.api.material.Exam;
import com.hacklympics.api.session.Session;
import com.hacklympics.api.user.User;
import com.hacklympics.teacher.TeacherController;
import com.hacklympics.teacher.proctor.ProctorController;
import com.hacklympics.utility.dialog.AlertDialog;
import com.hacklympics.utility.dialog.ConfirmDialog;

public class OngoingExamsController implements Initializable {
    
    private ObservableList<Exam> records;
    private List<Exam> recordsCache;
    
    private String keyword;

    @FXML
    private TableView<Exam> table;
    @FXML
    private TableColumn<Exam, String> examTitleCol;
    @FXML
    private TableColumn<Exam, Integer> examDurationCol;
    @FXML
    private TableColumn<Exam, String> examDescCol;
        
    @FXML
    private JFXTextField keywordField;
    @FXML
    private StackPane dialogPane;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initTable();
        fetchAndUpdate();
    }
    
    
    private void initTable() {
        records = FXCollections.observableArrayList();
        
        examTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        examDurationCol.setCellValueFactory(new PropertyValueFactory<>("duration"));
        examDescCol.setCellValueFactory(new PropertyValueFactory<>("desc"));
        
        // Override default empty message of exam table.
        table.setPlaceholder(new Label("No ongoing exams yet."));
        
        // Update the OngoingExams table whenever an exam is launched or halted.
        setOnLaunchExam((LaunchExamEvent e) -> {
            Platform.runLater(() -> {
                //recordsCache.add(e.getExam());
                //updateLocally();
                fetchAndUpdate();
            });
        });
        
        setOnHaltExam((HaltExamEvent e) -> {
            Platform.runLater(() -> {
                //recordsCache.remove(e.getExam());
                //updateLocally();
                fetchAndUpdate();
            });
        });
    }
    
    private void fetchAndUpdate() {
        keyword = (keyword == null) ? "" : keyword;
        
        records.clear();
        
        recordsCache = Exam.getOngoingExams();
        for (Exam e: recordsCache) {
            if (e.getTitle().contains(keyword) | e.getDesc().contains(keyword)) {
                records.add(e);
            }
        }
        
        table.getItems().clear();
        table.getItems().addAll(records);
    }
    
    private void updateLocally() {
        keyword = (keyword == null) ? "" : keyword;
        
        records.clear();
        
        for (Exam e: recordsCache) {
            if (e.getTitle().contains(keyword) | e.getDesc().contains(keyword)) {
                records.add(e);
            }
        }
        
        table.getItems().clear();
        table.getItems().addAll(records);
    }
    
    @FXML
    public void search(KeyEvent event) {
        keyword = keywordField.getText();
        updateLocally();
    }
    
    /**
     * Invoked when the attend exam button is clicked.
     * Asks the user for confirmation for attending the exam.
     * If the user answers yes, then he/she will be taken to the proctor page.
     * @param event emitted by JavaFX.
     */
    @FXML
    public void attendExam(ActionEvent event) {
        // If the user is trying to launch an exam, but no exam is selected,
        // block this attempt and alert the user.
        Exam selectedExam = table.getSelectionModel().getSelectedItem();
        
        if (selectedExam == null) {
            AlertDialog alert = new AlertDialog(
                    "Alert",
                    "You have not selected any exam to attend to."
            );
            
            alert.show();
            return;
        }
        
        // If the user is trying to launch an exam, but is already
        // in an exam, block this attempt and alert the user.
        if (Session.getInstance().isInExam()) {
            AlertDialog alert = new AlertDialog(
                    "Alert",
                    "You are already in an exam."
            );
            
            alert.show();
            return;
        }
        
        // If everything alright, then ask the user for confirmation.
        // If yes, then we will proceed.
        ConfirmDialog confirmation = new ConfirmDialog(
                "Proctor Exam",
                "You have selected the exam: " + selectedExam + "\n\n"
              + "Proctor the exam now? (You will become a proctor of this exam)"
        );
        
        confirmation.getConfirmBtn().setOnAction((ActionEvent e) -> {
            User currentUser = Session.getInstance().getCurrentUser();
            Response attend = currentUser.attend(selectedExam);
            
            switch (attend.getStatusCode()) {
                case SUCCESS:
                    Session.getInstance().setCurrentExam(selectedExam);
            
                    TeacherController tc = (TeacherController) Session.getInstance().getMainController();
                    ProctorController cc = (ProctorController) tc.getControllers().get("Proctor");
                    
                    // Reset the Proctor Page first.
                    cc.reset();
                    
                    // If the user is the owner of the exam, render the exitBtn
                    // as halt exam button. Otherwise, render it as leave button.
                    if (selectedExam.getOnwer().getUsername().equals(currentUser.getUsername())) {
                        cc.enableHaltExamBtn();
                    } else {
                        cc.enableLeaveExamBtn();
                    }
                    
                    cc.setExamLabel(selectedExam.getTitle(), selectedExam.getRemainingTime());
                    tc.showProctor(e);
                    
                    confirmation.close();
                    break;
                    
                case ALREADY_ATTENDED:
                    AlertDialog alert = new AlertDialog(
                            "Alert",
                            "You are already proctoring this exam."
                    );
                    
                    confirmation.close();
                    alert.show();
                    break;
                    
                default:
                    break;
            }
        });
        
        confirmation.show();
    }
    
    
    private void setOnLaunchExam(EventHandler<LaunchExamEvent> listener) {
        EventManager.getInstance().addEventHandler(EventType.LAUNCH_EXAM, listener);
    }
    
    private void setOnHaltExam(EventHandler<HaltExamEvent> listener) {
        EventManager.getInstance().addEventHandler(EventType.HALT_EXAM, listener);
    }
    
}