package hacklympics.login;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.Label;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXPasswordField;
import com.hacklympics.api.communication.Response;
import com.hacklympics.api.communication.StatusCode;
import com.hacklympics.api.user.User;
import com.hacklympics.api.user.Student;
import com.hacklympics.api.user.Teacher;
//import hacklympics.student.StudentController;
import hacklympics.teacher.TeacherController;

public class LoginController {
    
    private static final String TEACHER = "/hacklympics/teacher/Teacher.fxml";
    private static final String STUDENT = "/hacklympics/student/Student.fxml";
    
    private FXMLLoader fxmlLoader;
    private Response loginResp;
    private StatusCode statusCode;

    @FXML
    private Label warningMsg;
    @FXML
    private JFXTextField usernameField;
    @FXML
    private JFXPasswordField passwordField;
    @FXML
    private JFXButton loginBtn;
    
    
    public void login(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        
        try {
            loginResp = User.login(username, password);
            
            if (loginResp.success()) {
                String role = loginResp.getContent().get("role").toString();
            
                switch (role) {
                    case "student":
                        loadStage(STUDENT);
                        loginBtn.getScene().getWindow().hide();
                        // StudentController stCtrl = LOADER.getController();
                        // stCtrl.setStudent(new Student(username));
                        break;
                        
                    case "teacher":
                        loadStage(TEACHER);
                        loginBtn.getScene().getWindow().hide();
                        TeacherController tchCtrl = fxmlLoader.getController();
                        tchCtrl.setTeacher(new Teacher(username));
                        break;
                        
                    default:
                        break;
                }
            } else {
                statusCode = loginResp.getStatusCode();
                
                switch(statusCode) {
                    case VALIDATION_ERR:
                    case NOT_REGISTERED:
                        warningMsg.setText("Incorrect username or password.");
                        break;
                    
                    case ALREADY_LOGGED_IN:
                        warningMsg.setText("This account is currently in use.");
                        break;
                    
                    default:
                        break;
                }
            }
        } catch (Exception ex) {
            warningMsg.setText("Unable to connect to the server.");
            ex.printStackTrace();
        }
    }
    
    private void loadStage(String fxml) {
        try {
            fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(fxml));
            
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ioe) {
            warningMsg.setText("Unable to get FXML resource(s).");
            ioe.printStackTrace();
        }
    }
    
    public void clearWarningMsg(KeyEvent event) {
        warningMsg.setText("");
    }
    
    public void exit(ActionEvent event) {
        System.exit(0);
    }
    
}
