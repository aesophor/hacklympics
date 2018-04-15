package hacklympics.login;

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
import java.io.IOException;

public class LoginController {

    @FXML
    private Label warningMsg;
    @FXML
    private JFXTextField usernameField;
    @FXML
    private JFXPasswordField passwordField;
    @FXML
    private JFXButton loginBtn;
    
    private static final String TEACHER = "/hacklympics/teacher/Teacher.fxml";
    private static final String STUDENT = "/hacklympics/student/Student.fxml";
    
    
    public void login(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        
        try {
            Response login = User.login(username, password);
            
            if (login.success()) {
                String role = login.getContent().get("role").toString();
            
                switch (role) {
                    case "student":
                        loadUserStage(STUDENT);
                        loginBtn.getScene().getWindow().hide();
                        break;
                        
                    case "teacher":
                        loadUserStage(TEACHER);
                        loginBtn.getScene().getWindow().hide();
                        break;
                        
                    default:
                        break;
                }
            } else {
                StatusCode statusCode = login.getStatusCode();
            
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
            warningMsg.setText("Unable to establish a connection to the server.");
            ex.printStackTrace();
        }
        
    }
    
    public void clearWarningMsg(KeyEvent event) {
        warningMsg.setText("");
    }
    
    public void exit(ActionEvent event) {
        System.exit(0);
    }
    
    
    private void loadUserStage(String fxml) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxml));
        Scene scene = new Scene(root);
        Stage teacherStage = new Stage();
        teacherStage.setScene(scene);
        teacherStage.show();
    }
    
}
