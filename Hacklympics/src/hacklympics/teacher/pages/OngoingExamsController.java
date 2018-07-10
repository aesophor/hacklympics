package hacklympics.teacher.pages;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.List;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.input.KeyEvent;
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
        this.setOnExamLaunched((LaunchExamEvent e) -> {
            Platform.runLater(() -> {
                fetchAndUpdate();
            });
        });
        
        this.setOnExamHalted((HaltExamEvent e) -> {
            Platform.runLater(() -> {
                fetchAndUpdate();
            });
        });
    }
    
    private void fetchAndUpdate() {
        keyword = (keyword == null) ? "" : keyword;
        
        records.clear();
        
        recordsCache = Exam.getOngoingExams();
        for (Exam e: recordsCache) {
            if (e.getData().getTitle().contains(keyword) |
                e.getData().getDesc().contains(keyword)) {
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
            if (e.getData().getTitle().contains(keyword) |
                e.getData().getDesc().contains(keyword)) {
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
    
    private void setOnExamLaunched(EventHandler<LaunchExamEvent> listener) {
        EventManager.getInstance().addEventHandler(EventType.LAUNCH_EXAM, listener);
    }
    
    private void setOnExamHalted(EventHandler<HaltExamEvent> listener) {
        EventManager.getInstance().addEventHandler(EventType.HALT_EXAM, listener);
    }
    
}