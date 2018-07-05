package hacklympics.student;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Map;
import java.util.HashMap;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.AnchorPane;
import javafx.event.ActionEvent;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.hacklympics.api.communication.Response;
import com.hacklympics.api.communication.SocketServer;
import com.hacklympics.api.event.EventType;
import com.hacklympics.api.event.EventManager;
import com.hacklympics.api.event.EventListener;
import com.hacklympics.api.event.LoginEvent;
import com.hacklympics.api.event.LogoutEvent;
import com.hacklympics.api.session.Session;
import com.hacklympics.api.session.Session.MainController;
import com.hacklympics.api.user.User;
import hacklympics.utility.FXMLTable;
import hacklympics.utility.ConfirmDialog;
import hacklympics.utility.Utils;

public class StudentController implements Initializable, MainController {
    
    private Map<String, AnchorPane> pages;
    private Map<String, Object> controllers;
    private ObservableList<User> onlineUsers;
    
    @FXML
    private AnchorPane holderPane;
    @FXML
    private StackPane dialogPane;
    @FXML
    private Label bannerMsg;
    @FXML
    private JFXButton logoutBtn;
    @FXML
    private JFXListView onlineUserListView;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setGreetingMsg();
        initOnlineUserListView();
        
        initPages();
        showPage(pages.get("dashboard"));
        
        this.setOnLogin((LoginEvent event) -> {
            // Avoid throwing IllegalStateException by running from a non-JavaFX thread.
            // The following lines of code runs the specified Runnable on the 
            // JavaFX Application Thread at some unspecified time in the future.
            Platform.runLater(() -> {
                onlineUsers.add(event.getLoggedInUser());
            });
        });
                
        this.setOnLogout((LogoutEvent event) -> {
            Platform.runLater(() -> {
                onlineUsers.remove(event.getLoggedOutUser());
            });
        });
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
            FXMLLoader messagesLoader = new FXMLLoader(getClass().getResource(messagesFXML));
            FXMLLoader codeLoader = new FXMLLoader(getClass().getResource(codeFXML));
            
            pages.put("dashboard", dashboardLoader.load());
            pages.put("courses", coursesLoader.load());
            //pages.put("scoreboard", scoreboardLoader.load());
            pages.put("messages", messagesLoader.load());
            pages.put("code", codeLoader.load());
            
            controllers.put("dashboard", dashboardLoader.getController());
            controllers.put("courses", coursesLoader.getController());
            //controllers.put("scoreboard", scoreboardLoader.getController());
            controllers.put("messages", messagesLoader.getController());
            controllers.put("code", codeLoader.getController());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    
    private void initOnlineUserListView() {
        onlineUsers = FXCollections.observableArrayList();
        onlineUsers.setAll(User.getOnlineUsers());
        
        onlineUsers.addListener((ListChangeListener.Change<? extends User> c) -> {
            updateOnlineUserListView();
        });
        
        onlineUserListView.getStyleClass().add("online-user-list");
        updateOnlineUserListView();
    }
    
    public void updateOnlineUserListView() {
        onlineUserListView.getItems().clear();
        onlineUserListView.getItems().setAll(onlineUsers);
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
                String loginFXML = FXMLTable.getInstance().get("Login");
                Utils.loadStage(new FXMLLoader(getClass().getResource(loginFXML)));
                logoutBtn.getScene().getWindow().hide();
                
                SocketServer.getInstance().close();
            }
        });
        
        confirm.show();
    }
    
    private void setGreetingMsg() {
        User current = Session.getInstance().getCurrentUser();
        String greetingMsg = String.format("Welcome, %s", current.getProfile().getFullname());
        bannerMsg.setText(greetingMsg);
    }
    
    private void setOnLogin(EventListener<LoginEvent> listener) {
        EventManager.getInstance().addEventListener(EventType.LOGIN, listener);
    }
    
    private void setOnLogout(EventListener<LogoutEvent> listener) {
        EventManager.getInstance().addEventListener(EventType.LOGOUT, listener);
    }
    
    public Map<String, Object> getControllers() {
        return controllers;
    }
    
}