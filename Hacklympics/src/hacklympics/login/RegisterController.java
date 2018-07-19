package hacklympics.login;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.Label;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.hacklympics.api.communication.Response;
import com.hacklympics.api.communication.StatusCode;
import com.hacklympics.api.user.User;
import hacklympics.common.FXMLTable;
import hacklympics.utility.Utils;


public class RegisterController implements Initializable {
    
    @FXML
    private JFXTextField usernameField;
    @FXML
    private JFXTextField fullnameField;
    @FXML
    private JFXPasswordField passwordField;
    @FXML
    private JFXComboBox gradYearBox;
    
    @FXML
    private JFXButton registerBtn;
    @FXML
    private Label warningMsg;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initGradYearBox(108, 120);
    }

    
    public void register(ActionEvent e) {
        if (!isFormValid()) {
            warningMsg.setText("Please fill out all the required fields.");
            return;
        }
        
        String username = this.usernameField.getText();
        String fullname = this.fullnameField.getText();
        String password = this.passwordField.getText();
        Integer gradYear = (Integer) this.gradYearBox.getSelectionModel().getSelectedItem();
        
        try {
            Response register = User.register(username, password, fullname, gradYear);
            
            if (register.success()) {
                back(new ActionEvent());
            } else {
                StatusCode statusCode = register.getStatusCode();
                
                switch (statusCode) {
                    case ALREADY_REGISTERED:
                        warningMsg.setText("An account already exists with this username.");
                        break;
                    default:
                        warningMsg.setText("An error has occurred. Please contact the admin.");
                        break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void back(ActionEvent e) {
        String registerFXML = FXMLTable.getInstance().get("Login");
        Utils.loadStage(new FXMLLoader(getClass().getResource(registerFXML)));
        registerBtn.getScene().getWindow().hide();
    }
    
    public void clearWarningMsg(KeyEvent event) {
        warningMsg.setText("");
    }

    private void initGradYearBox(int min, int max) {
        for (int i = min; i < max; i++) {
            this.gradYearBox.getItems().addAll(i);
        }
    }
    
    private boolean isFormValid() {
        String username = this.usernameField.getText();
        String fullname = this.fullnameField.getText();
        String password = this.passwordField.getText();
        Object gradYear = this.gradYearBox.getSelectionModel().getSelectedItem();
        
        return !(username == null | username.isEmpty() |
                 fullname == null | fullname.isEmpty() |
                 password == null | password.isEmpty() |
                 gradYear == null);
    }
    
}