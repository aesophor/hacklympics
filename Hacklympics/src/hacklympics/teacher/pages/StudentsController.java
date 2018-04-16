package hacklympics.teacher.pages;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.List;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import com.hacklympics.api.user.Profile;
import com.hacklympics.api.user.Student;

public class StudentsController implements Initializable {
    
    private ObservableList<Profile> records;

    @FXML
    private TableView<Profile> studentsTable;
    @FXML
    private TableColumn<Student, Integer> gradYearColumn;
    @FXML
    private TableColumn<Student, String> usernameColumn;
    @FXML
    private TableColumn<Student, String> fullnameColumn;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initStudentTable();
    }    
    
    
    private void initStudentTable() {
        records = FXCollections.observableArrayList();
        
        List<String> studentList = Student.list();
        
        for (String username: studentList) {
            records.add(new Student(username).getProfile());
        }
        
        gradYearColumn.setCellValueFactory(new PropertyValueFactory<>("gradYear"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        fullnameColumn.setCellValueFactory(new PropertyValueFactory<>("fullname"));
        
        studentsTable.getItems().clear();
        studentsTable.getItems().addAll(records);
    }
    
}