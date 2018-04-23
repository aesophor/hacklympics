package hacklympics.login;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXPasswordField;
import com.hacklympics.api.communication.Response;
import com.hacklympics.api.communication.StatusCode;
import com.hacklympics.api.session.CurrentUser;
import com.hacklympics.api.users.User;
import com.hacklympics.api.users.Student;
import com.hacklympics.api.users.Teacher;
import hacklympics.student.StudentController;
import hacklympics.teacher.TeacherController;
import hacklympics.utility.FXMLTable;
import hacklympics.utility.Utils;

public class LoginController {
    
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Label warningMsg;
    @FXML
    private JFXTextField usernameField;
    @FXML
    private JFXPasswordField passwordField;
    @FXML
    private JFXButton loginBtn;
    
    
    @FXML
    public void login(ActionEvent e) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        
        try {
            Response login = User.login(username, password);
            
            if (login.success()) {
                String role = login.getContent().get("role").toString();
            
                switch (role) {
                    case "student":
                        CurrentUser.getInstance().setUser(new Student(username));
                        Utils.loadStage(new FXMLLoader(), FXMLTable.getInstance().get("Student"), StudentController.class);
                        break;
                    case "teacher":
                        CurrentUser.getInstance().setUser(new Teacher(username));
                        Utils.loadStage(new FXMLLoader(), FXMLTable.getInstance().get("Teacher"), TeacherController.class);
                        break;
                    default:
                        break;
                }
                
                loginBtn.getScene().getWindow().hide();
                
                // Add ShutdownHook for auto logout upon exit.
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
            warningMsg.setText("Unable to connect to the server.");
            ex.printStackTrace();
        }
    }
    
    @FXML
    public void register(ActionEvent e) {
        Utils.loadStage(new FXMLLoader(), FXMLTable.getInstance().get("Register"), RegisterController.class);
        loginBtn.getScene().getWindow().hide();
    }
    
    @FXML
    public void exit(ActionEvent e) {
        System.exit(0);
    }
    
    @FXML
    public void clearWarningMsg(KeyEvent e) {
        warningMsg.setText("");
    }
    
}
