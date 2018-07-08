package hacklympics.teacher.pages;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.List;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import com.jfoenix.controls.JFXComboBox;
import com.hacklympics.api.user.Student;

public class ProctorController implements Initializable {
    
    private List<Student> attendedStudents;
    private ObservableList<Student> normalGroup;
    private ObservableList<Student> specialGroup;
    
    @FXML
    private Tab liveScreensTab;
    @FXML
    private Tab keystrokesTab;
    @FXML
    private StackPane dialogPane;
    @FXML
    private AnchorPane normalGrpPane;
    @FXML
    private AnchorPane specialGrpPane;
    @FXML
    private JFXComboBox groupBox;
    @FXML
    private JFXComboBox imgQualityBox;
    @FXML
    private JFXComboBox imgFreqBox;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        normalGroup = FXCollections.observableArrayList();
        normalGroup.setAll();
        
        specialGroup = FXCollections.observableArrayList();
        specialGroup.setAll();
    }

    
    @FXML
    public void moveToSpecialGrp(ActionEvent event) {
        
    }
    
    @FXML
    public void moveToNormalGrp(ActionEvent event) {
        
    }

    @FXML
    public void adjustLiveScreensParam(ActionEvent event) {
        
    }
    
    @FXML
    public void adjustKeystrokesParam(ActionEvent event) {
        
    }
    
}