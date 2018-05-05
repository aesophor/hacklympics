package hacklympics.student;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Map;
import java.util.HashMap;
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
import com.hacklympics.api.session.Session;
import com.hacklympics.api.users.User;
import hacklympics.utility.FXMLTable;
import hacklympics.utility.ConfirmDialog;

public class StudentController implements Initializable {
    
    private Map<String, AnchorPane> pages;
    private Map<String, Object> controllers;
    
    @FXML
    private AnchorPane holderPane;
    @FXML
    private StackPane dialogPane;
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
        showPage(pages.get("dashboard"));
        
        setGreetingMsg();
    }
    
    private void initPages() {
        pages = new HashMap<>();
        controllers = new HashMap<>();
        
        String dashboardFXML = FXMLTable.getInstance().get("Student/dashboard");
        String coursesFXML = FXMLTable.getInstance().get("Student/courses");
        String scoreboardFXML = FXMLTable.getInstance().get("Student/scoreboard");
        String messagesFXML = FXMLTable.getInstance().get("Student/messages");
        String codeFXML = FXMLTable.getInstance().get("Student/code");
        
        try {
            FXMLLoader dashboardLoader = new FXMLLoader(getClass().getResource(dashboardFXML));
            FXMLLoader coursesLoader = new FXMLLoader(getClass().getResource(coursesFXML));
            //FXMLLoader scoreboardLoader = new FXMLLoader(getClass().getResource(coursesFXML));
            //FXMLLoader messagesLoader = new FXMLLoader(getClass().getResource(messagesFXML));
            FXMLLoader codeLoader = new FXMLLoader(getClass().getResource(codeFXML));
            
            pages.put("dashboard", dashboardLoader.load());
            pages.put("courses", coursesLoader.load());
            //pages.put("scoreboard", scoreboardLoader.load());
            //pages.put("messages", messagesLoader.load());
            pages.put("code", codeLoader.load());
            
            controllers.put("dashboard", dashboardLoader.getController());
            controllers.put("courses", coursesLoader.getController());
            //controllers.put("scoreboard", scoreboardLoader.getController());
            //controllers.put("messages", messagesLoader.getController());
            controllers.put("code", codeLoader.getController());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    
    private void initOnlineUsersList() {
        Label andrey = new Label("Andrey");
        andrey.getStyleClass().add("online-user-label");
        onlineUsersList.getItems().add(andrey);
    }
    
    private void setGreetingMsg() {
        User current = Session.getInstance().getCurrentUser();
        String greetingMsg = String.format("Welcome, %s", current.getProfile().getFullname());
        bannerMsg.setText(greetingMsg);
    }
    
    
    private void showPage(Node node) {
        holderPane.getChildren().clear();
        holderPane.getChildren().add((Node) node);
    }
    
    public void showDashboard(ActionEvent event) {
        showPage(pages.get("dashboard"));
    }
    
    public void showCourses(ActionEvent event) {
        showPage(pages.get("courses"));
    }
    
    public void showScoreboard(ActionEvent event) {
        showPage(pages.get("scoreboard"));
    }
    
    public void showMessages(ActionEvent event) {
        showPage(pages.get("messages"));
    }
    
    public void showCode(ActionEvent event) {
        showPage(pages.get("code"));
    }
    
    
    public void logout(ActionEvent event) {
        ConfirmDialog confirm = new ConfirmDialog(
                dialogPane,
                "Logout",
                "You are about to be logged out. Are you sure?"
        );
        
        confirm.getConfirmBtn().setOnAction((ActionEvent e) -> {
            Response logout = Session.getInstance().getCurrentUser().logout();
            
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
        
        confirm.show();
    }
    
    
    public Map<String, Object> getControllers() {
        return controllers;
    }
    
}