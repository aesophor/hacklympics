package hacklympics.teacher.pages;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.hacklympics.api.communication.Response;
import com.hacklympics.api.user.Profile;
import com.hacklympics.api.user.Student;
import javafx.event.ActionEvent;

public class StudentsController implements Initializable {
    
    private ObservableList<Profile> records;
    private String keyword;

    @FXML
    private TableView<Profile> table;
    @FXML
    private TableColumn<Student, Integer> gradYearColumn;
    @FXML
    private TableColumn<Student, String> usernameColumn;
    @FXML
    private TableColumn<Student, String> fullnameColumn;
    
    @FXML
    private JFXTextField keywordField;
    @FXML
    private JFXButton searchBtn;
    
    
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
        Response list = Student.list();
        
        if (list.success()) {
            String json = list.getContent().get("users").toString();
            JsonArray users = new Gson().fromJson(json, JsonArray.class);
            
            keyword = (keyword == null) ? "" : keyword;
            
            for (JsonElement e: users) {
                String username = e.getAsJsonObject().get("username").getAsString();
                String fullname = e.getAsJsonObject().get("username").getAsString();
                int gradYear = e.getAsJsonObject().get("graduationYear").getAsInt();
                
                if (username.contains(keyword) | fullname.contains(keyword)) {
                    records.add(new Profile(username, fullname, gradYear));
                }
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