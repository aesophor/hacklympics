package hacklympics.student.pages;

import com.hacklympics.api.communication.Response;
import com.hacklympics.api.event.EventManager;
import com.hacklympics.api.event.EventType;
import com.hacklympics.api.event.exam.LaunchExamEvent;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.List;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import com.hacklympics.api.material.Exam;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import com.hacklympics.api.event.EventHandler;
import com.hacklympics.api.session.Session;
import com.hacklympics.api.user.User;
import hacklympics.student.StudentController;
import hacklympics.utility.AlertDialog;
import hacklympics.utility.ConfirmDialog;
import javafx.scene.layout.StackPane;

public class OngoingExamsController implements Initializable {
    
    private ObservableList<Exam> records;
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
        buildTable();
        showTable();
        
        // Update the OngoingExams table whenever an exam is launched or halted.
        this.setOnExamLaunched((LaunchExamEvent e) -> {
            Platform.runLater(() -> {
                records.clear();
            
                buildTable();
                showTable();
            });
        });
        
        this.setOnExamHalted((LaunchExamEvent e) -> {
            Platform.runLater(() -> {
                records.clear();
            
                buildTable();
                showTable();
            });
        });
    }    
    
    
    private void initTable() {
        records = FXCollections.observableArrayList();
        
        examTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        examDurationCol.setCellValueFactory(new PropertyValueFactory<>("duration"));
        examDescCol.setCellValueFactory(new PropertyValueFactory<>("desc"));
    }
    
    private void buildTable() {
        List<Exam> exams = Exam.getOngoingExams();
        keyword = (keyword == null) ? "" : keyword;
        
        for (Exam e: exams) {
            if (e.getData().getTitle().contains(keyword) |
                e.getData().getDesc().contains(keyword)) {
                records.add(e);
            }
        }
    }
    
    private void showTable() {
        table.getItems().clear();
        table.getItems().addAll(records);
    }
    
    @FXML
    public void search(ActionEvent event) {
        records.clear();
        keyword = keywordField.getText();
        
        buildTable();
        showTable();
    }
    
    /**
     * Invoked when the attend exam button is clicked.
     * Asks the user for confirmation for attending the exam.
     * If the user answers yes, then he/she will be taken to the code page.
     * @param event emitted by JavaFX.
     */
    @FXML
    public void attendExam(ActionEvent event) {
        Exam selectedExam = table.getSelectionModel().getSelectedItem();
        if (selectedExam == null) return;
        
        ConfirmDialog confirmation = new ConfirmDialog(
                dialogPane,
                "Attend Exam",
                "You have selected the exam: " + selectedExam + "\n\n"
              + "Attend the exam now?"
        );
        
        confirmation.getConfirmBtn().setOnAction((ActionEvent e) -> {
            User currentUser = Session.getInstance().getCurrentUser();
            Response attend = currentUser.attend(selectedExam);
            
            // TODO: Students should not be able to enter the same exam again.
            switch (attend.getStatusCode()) {
                case SUCCESS:
                    Session.getInstance().setCurrentExam(selectedExam);
                    
                    StudentController sc = (StudentController) Session.getInstance().getMainController();
                    CodeController cc = (CodeController) sc.getControllers().get("Code");
            
                    cc.setExamLabel(selectedExam.getTitle(), selectedExam.getRemainingTime());
                    cc.setProblemBox(selectedExam.getProblems());
                    sc.showCode(e);
                    break;
                    
                case NOT_LAUNCHED:
                    AlertDialog alert = new AlertDialog(
                            dialogPane,
                            "Exam Not Ready or Ended",
                            "The selected exam is either not launched yet "
                          + "or has been halted already."
                    );
                    
                    alert.show();
                    break;
                    
                default:
                    break;
            }
            
            confirmation.close();
        });
        
        confirmation.show();
    }
    
    private void setOnExamLaunched(EventHandler<LaunchExamEvent> listener) {
        EventManager.getInstance().addEventHandler(EventType.LAUNCH_EXAM, listener);
    }
    
    private void setOnExamHalted(EventHandler<LaunchExamEvent> listener) {
        EventManager.getInstance().addEventHandler(EventType.HALT_EXAM, listener);
    }
    
}