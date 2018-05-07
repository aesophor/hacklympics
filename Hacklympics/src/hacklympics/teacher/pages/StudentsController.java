package hacklympics.teacher.pages;

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
import com.google.gson.Gson;
import com.jfoenix.controls.JFXTextField;
import com.hacklympics.api.user.Student;

public class StudentsController implements Initializable {
    
    private static final Gson GSON = new Gson();
    private ObservableList<Student> records;
    private String keyword;

    @FXML
    private TableView<Student> table;
    @FXML
    private TableColumn<Student, Integer> gradYearColumn;
    @FXML
    private TableColumn<Student, String> usernameColumn;
    @FXML
    private TableColumn<Student, String> fullnameColumn;
    
    @FXML
    private JFXTextField keywordField;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initTable();
        buildTable();
        showTable();
    }    
    
    
    private void initTable() {
        records = FXCollections.observableArrayList();
        
        gradYearColumn.setCellValueFactory(new PropertyValueFactory<>("gradYear"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        fullnameColumn.setCellValueFactory(new PropertyValueFactory<>("fullname"));
    }
    
    private void buildTable() {
        List<Student> students = Student.getStudents();
        keyword = (keyword == null) ? "" : keyword;
        
        for (Student s: students) {
            if (s.getProfile().getUsername().contains(keyword) |
                s.getProfile().getFullname().contains(keyword)) {
                records.add(s);
            }
        }
    }
    
    private void showTable() {
        table.getItems().clear();
        table.getItems().addAll(records);
    }
    
    
    public void search(ActionEvent event) {
        records.clear();
        keyword = keywordField.getText();
        
        buildTable();
        showTable();
    }
    
}