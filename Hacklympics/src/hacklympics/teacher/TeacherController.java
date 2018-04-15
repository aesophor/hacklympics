package hacklympics.teacher;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import com.jfoenix.controls.JFXButton;
import com.hacklympics.api.communication.Response;
import com.hacklympics.api.user.User;
import com.hacklympics.api.user.Teacher;

import javafx.scene.Node;

public class TeacherController implements Initializable {
    
    private Map<AnchorPane, String> pages;
    private Teacher teacher;
    
    @FXML
    private AnchorPane holderPane;
    private AnchorPane dashboard;
    private AnchorPane students;
    private AnchorPane courses;
    private AnchorPane exams;
    private AnchorPane problems;
    @FXML
    private Label bannerMsg;
    @FXML
    private JFXButton logoutBtn;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //initPages();
        //showPage(dashboard);
    }
    
    
    private void initPages() {
        pages = new HashMap<>();
        
        pages.put(dashboard, "/modules/Dashboard.fxml");
        pages.put(students, "/modules/Students.fxml");
        pages.put(courses, "/modules/Courses.fxml");
        pages.put(exams, "/modules/Exams.fxml");
        pages.put(problems, "/modules/Problems.fxml");
        
        try {
            for (Map.Entry<AnchorPane, String> page : pages.entrySet()) {
                AnchorPane pane = page.getKey();
                String fxml = page.getValue();
                
                pane = FXMLLoader.load(getClass().getResource(fxml));
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    
    private void showPage(Node node) {
        holderPane.getChildren().clear();
        holderPane.getChildren().add((Node) node);
    }
    
    
    public void logout(ActionEvent event) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Logout Confirm");
        alert.setHeaderText("You are about to be logged out");
        alert.setContentText("Are you sure?");

        Optional<ButtonType> result = alert.showAndWait();
        
        if (result.get() == ButtonType.OK) {
            Response logout = User.logout(teacher.getProfile().getUsername());
        
            if (logout.success()) {
                logoutBtn.getScene().getWindow().hide();
            
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/hacklympics/login/Login.fxml"));
                    Scene scene = new Scene(root);
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public Teacher getTeacher() {
        return teacher;
    }
    
    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
        
        String greetMsg = String.format("Welcome, %s", teacher.getProfile().getFullname());
        bannerMsg.setText(greetMsg);
    }
}
