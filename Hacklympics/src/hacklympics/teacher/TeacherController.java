package hacklympics.teacher;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Map;
import java.util.HashMap;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Label;
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

public class TeacherController implements Initializable, MainController {
    
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
        initOnlineUserListView();
        setGreetingMsg();
        
        initPages();
        showPage(pages.get("dashboard"));
        
        this.setOnLogin((LoginEvent event) -> {
            onlineUsers.add(event.getLoggedInUser());
        });
        
        this.setOnLogout((LogoutEvent event) -> {
            onlineUsers.remove(event.getLoggedOutUser());
        });
    }
    
    
    private void initPages() {
        pages = new HashMap<>();
        controllers = new HashMap<>();
        
        String dashboardFXML = FXMLTable.getInstance().get("Teacher/dashboard");
        String coursesFXML = FXMLTable.getInstance().get("Teacher/courses");
        String studentsFXML = FXMLTable.getInstance().get("Teacher/students");
        String messagesFXML = FXMLTable.getInstance().get("Teacher/messages");
        String proctorFXML = FXMLTable.getInstance().get("Teacher/proctor");
        
        try {
            FXMLLoader dashboardLoader = new FXMLLoader(getClass().getResource(dashboardFXML));
            FXMLLoader coursesLoader = new FXMLLoader(getClass().getResource(coursesFXML));
            FXMLLoader studentsLoader = new FXMLLoader(getClass().getResource(studentsFXML));
            FXMLLoader messagesLoader = new FXMLLoader(getClass().getResource(messagesFXML));
            FXMLLoader proctorLoader = new FXMLLoader(getClass().getResource(proctorFXML));
            
            pages.put("dashboard", dashboardLoader.load());
            pages.put("courses", coursesLoader.load());
            pages.put("students", studentsLoader.load());
            pages.put("messages", messagesLoader.load());
            pages.put("proctor", proctorLoader.load());
            
            controllers.put("dashboard", dashboardLoader.getController());
            controllers.put("courses", coursesLoader.getController());
            controllers.put("students", studentsLoader.getController());
            controllers.put("messages", messagesLoader.getController());
            controllers.put("proctor", proctorLoader.getController());
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
    
    public void showStudents(ActionEvent event) {
        showPage(pages.get("students"));
    }
    
    public void showMessages(ActionEvent event) {
        showPage(pages.get("messages"));
    }
    
    public void showProctor(ActionEvent event) {
        showPage(pages.get("proctor"));
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
        String greetingMsg = String.format("Welcome, %s", current.getFullname());
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