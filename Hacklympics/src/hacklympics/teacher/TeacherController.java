package hacklympics.teacher;

import java.net.URL;
import java.util.ResourceBundle;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.hacklympics.api.communication.Response;
import com.hacklympics.api.session.CurrentUser;
import com.hacklympics.api.users.User;
import hacklympics.utility.FXMLTable;
import hacklympics.utility.TextDialog;

public class TeacherController implements Initializable {
    
    private AnchorPane dashboard;
    private AnchorPane courses;
    private AnchorPane students;
    private AnchorPane messages;
    private AnchorPane proctor;
    
    @FXML
    private AnchorPane mainPane;
    @FXML
    private AnchorPane holderPane;
    @FXML
    private StackPane stackPane;
    @FXML
    private Label bannerMsg;
    @FXML
    private JFXButton logoutBtn;
    @FXML
    private JFXListView onlineUsersList;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initOnlineUsersList();
        initPages();
        showPage(dashboard);
        
        setGreetingMsg();
    }
    
    
    private void initPages() {
        String dashboardFXML = FXMLTable.getInstance().get("Teacher/dashboard");
        String coursesFXML = FXMLTable.getInstance().get("Teacher/courses");
        String studentsFXML = FXMLTable.getInstance().get("Teacher/students");
        String messagesFXML = FXMLTable.getInstance().get("Teacher/messages");
        String proctorFXML = FXMLTable.getInstance().get("Teacher/proctor");
        
        try {
            dashboard = FXMLLoader.load(getClass().getResource(dashboardFXML));
            courses = FXMLLoader.load(getClass().getResource(coursesFXML));
            students = FXMLLoader.load(getClass().getResource(studentsFXML));
            //messages = FXMLLoader.load(getClass().getResource(messagesFXML));
            //proctor = FXMLLoader.load(getClass().getResource(proctorFXML));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    
    private void showPage(Node node) {
        holderPane.getChildren().clear();
        holderPane.getChildren().add((Node) node);
    }
    
    private void initOnlineUsersList() {
        Label andrey = new Label("Andrey");
        andrey.getStyleClass().add("online-user-label");
        onlineUsersList.getItems().add(andrey);
    }
    
    public void setGreetingMsg() {
        User current = CurrentUser.getInstance().getUser();
        String greetingMsg = String.format("Welcome, %s", current.getProfile().getFullname());
        bannerMsg.setText(greetingMsg);
    }
    
    
    public void logout(ActionEvent event) {
        TextDialog alert = new TextDialog(stackPane,
                                          "Alert",
                                          "You are about to be logged out. Are you sure?");
        
        alert.getConfirmBtn().setOnAction((ActionEvent e) -> {
            Response logout = User.logout(CurrentUser.getInstance().getUser().getProfile().getUsername());
            
            if (logout.success()) {
                try {
                    String loginFXML = FXMLTable.getInstance().get("Login");
                    Parent root = FXMLLoader.load(getClass().getResource(loginFXML));
                    Scene scene = new Scene(root);
                    Stage stage = new Stage();
                    stage.initStyle(StageStyle.UNDECORATED);
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                
                logoutBtn.getScene().getWindow().hide();
            }
        });
        
        alert.show();
    }
    
    
    public void showDashboard(ActionEvent event) {
        showPage(dashboard);
    }
    
    public void showCourses(ActionEvent event) {
        showPage(courses);
    }
    
    public void showStudents(ActionEvent event) {
        showPage(students);
    }
    
    public void showMessages(ActionEvent event) {
        showPage(messages);
    }
    
    public void showProctor(ActionEvent event) {
        showPage(proctor);
    }
    
}