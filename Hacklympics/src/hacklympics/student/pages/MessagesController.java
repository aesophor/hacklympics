package hacklympics.student.pages;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.scene.control.TextArea;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;

public class MessagesController implements Initializable {

    @FXML
    private TextArea messageBoard;
    @FXML
    private JFXTextArea inputArea;
    @FXML
    private JFXButton sendBtn;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    
    
    public void send(ActionEvent event) {
        
    }
    
}
