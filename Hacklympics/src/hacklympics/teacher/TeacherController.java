package hacklympics.teacher;

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
import com.hacklympics.api.event.user.LoginEvent;
import com.hacklympics.api.event.user.LogoutEvent;
import com.hacklympics.api.session.Session;
import com.hacklympics.api.session.Session.MainController;
import com.hacklympics.api.user.User;
import hacklympics.utility.FXMLTable;
import hacklympics.utility.dialog.ConfirmDialog;
import hacklympics.utility.Utils;
import com.hacklympics.api.event.EventHandler;

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
        setGreetingMsg();
        initOnlineUserListView();
        
        initPages();
        showPage(pages.get("Dashboard"));
        
        
        // Update the Online User List (on the bottom left) whenever
        // the SocketServer receives events of user login/logout.
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
        
        String dashboardFXML = FXMLTable.getInstance().get("Teacher/Dashboard");
        String coursesFXML = FXMLTable.getInstance().get("Teacher/Courses");
        String ongoingExamsFXML = FXMLTable.getInstance().get("Teacher/OngoingExams");
        String messagesFXML = FXMLTable.getInstance().get("Teacher/Messages");
        String proctorFXML = FXMLTable.getInstance().get("Teacher/Proctor");
        
        try {
            FXMLLoader dashboardLoader = new FXMLLoader(getClass().getResource(dashboardFXML));
            FXMLLoader coursesLoader = new FXMLLoader(getClass().getResource(coursesFXML));
            FXMLLoader ongoingExamsLoader = new FXMLLoader(getClass().getResource(ongoingExamsFXML));
            FXMLLoader messagesLoader = new FXMLLoader(getClass().getResource(messagesFXML));
            FXMLLoader proctorLoader = new FXMLLoader(getClass().getResource(proctorFXML));
            
            pages.put("Dashboard", dashboardLoader.load());
            pages.put("Courses", coursesLoader.load());
            pages.put("OngoingExams", ongoingExamsLoader.load());
            pages.put("Messages", messagesLoader.load());
            pages.put("Proctor", proctorLoader.load());
            
            controllers.put("Dashboard", dashboardLoader.getController());
            controllers.put("Courses", coursesLoader.getController());
            controllers.put("OngoingExams", ongoingExamsLoader.getController());
            controllers.put("Messages", messagesLoader.getController());
            controllers.put("Proctor", proctorLoader.getController());
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
        showPage(pages.get("Dashboard"));
    }
    
    public void showCourses(ActionEvent event) {
        showPage(pages.get("Courses"));
    }
    
    public void showOngoingExams(ActionEvent event) {
        showPage(pages.get("OngoingExams"));
    }
    
    public void showMessages(ActionEvent event) {
        showPage(pages.get("Messages"));
    }
    
    public void showProctor(ActionEvent event) {
        showPage(pages.get("Proctor"));
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
                
                Session.getInstance().clear();
                EventManager.getInstance().clearEventHandlers();
                SocketServer.getInstance().shutdown();
            }
        });
        
        confirm.show();
    }
    
    private void setGreetingMsg() {
        User current = Session.getInstance().getCurrentUser();
        String greetingMsg = String.format("Welcome, %s", current.getFullname());
        bannerMsg.setText(greetingMsg);
    }
    
    public Map<String, Object> getControllers() {
        return controllers;
    }
    
    private void setOnLogin(EventHandler<LoginEvent> listener) {
        EventManager.getInstance().addEventHandler(EventType.LOGIN, listener);
    }
    
    private void setOnLogout(EventHandler<LogoutEvent> listener) {
        EventManager.getInstance().addEventHandler(EventType.LOGOUT, listener);
    }
    
}