package hacklympics.teacher.pages;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.hacklympics.api.communication.Response;
import java.net.URL;
import java.util.ResourceBundle;
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
    private TableView<Profile> table;
    @FXML
    private TableColumn<Student, Integer> gradYearColumn;
    @FXML
    private TableColumn<Student, String> usernameColumn;
    @FXML
    private TableColumn<Student, String> fullnameColumn;
    
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
            
            for (JsonElement e: users) {
                records.add(new Profile(
                        e.getAsJsonObject().get("username").getAsString(),
                        e.getAsJsonObject().get("fullname").getAsString(),
                        e.getAsJsonObject().get("graduationYear").getAsInt()
                ));
            }
        }
    }
    
    private void showTable() {
        table.getItems().clear();
        table.getItems().addAll(records);
    }
    
}