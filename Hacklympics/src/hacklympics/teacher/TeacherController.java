package hacklympics.teacher;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import com.jfoenix.controls.JFXButton;
import com.hacklympics.api.communication.Response;
import com.hacklympics.api.communication.StatusCode;
import com.hacklympics.api.user.User;

public class TeacherController implements Initializable {

    @FXML
    private Label warningMsg;
    @FXML
    private JFXButton logoutBtn;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    
    public void logoutBtn(ActionEvent event) {
    }
}
