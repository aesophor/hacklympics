package com.hacklympics.student;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Map;
import java.util.HashMap;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.AnchorPane;
import javafx.event.ActionEvent;
import com.hacklympics.api.communication.Response;
import com.hacklympics.api.communication.SocketServer;
import com.hacklympics.api.event.EventManager;
import com.hacklympics.api.session.Session;
import com.hacklympics.api.session.MainController;
import com.hacklympics.api.user.User;
import com.hacklympics.common.ui.dialog.ConfirmDialog;
import com.hacklympics.common.ui.listview.OnlineUserListView;
import com.hacklympics.common.ui.usermenu.DropDownUserMenu;
import com.hacklympics.utility.FXMLTable;
import com.hacklympics.utility.Utils;

public class StudentController implements Initializable, MainController {
    
    private Map<String, AnchorPane> pages;
    private Map<String, Object> controllers;
    
    private DropDownUserMenu userMenu;
    
    @FXML
    private AnchorPane mainPane;
    @FXML
    private AnchorPane onlineUserPane;
    @FXML
    private AnchorPane contentPane;
    @FXML
    private StackPane dialogPane;
    @FXML
    private Button userMenuBtn;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initPages();
        showPage(pages.get("Dashboard"));
        
        
        // Initialize OnlineUserListView.
        onlineUserPane.getChildren().add(new OnlineUserListView());
        
        // Initialize user menu.
        User currentUser = Session.getInstance().getCurrentUser();
        userMenuBtn.setText(String.format("%s ", currentUser.getFullname()));
        
        userMenu = new DropDownUserMenu();
        
        userMenu.add(new Label("Profile"), (MouseEvent event) -> {
            userMenu.hide();
        });
        
        userMenu.add(new Label("Settings"), (MouseEvent event) -> {
            userMenu.hide();
        });
        
        userMenu.add(new Label("About"), (MouseEvent event) -> {
            userMenu.hide();
        });
        
        userMenu.add(new Label("Logout"), (MouseEvent event) -> {
            userMenu.hide();
            logout();
        });
    }
    
    private void initPages() {
        pages = new HashMap<>();
        controllers = new HashMap<>();
        
        String dashboardFXML = FXMLTable.getInstance().get("Student/Dashboard");
        String coursesFXML = FXMLTable.getInstance().get("Student/Courses");
        String ongoingExamsFXML = FXMLTable.getInstance().get("Student/OngoingExams");
        String messagesFXML = FXMLTable.getInstance().get("Student/Messages");
        String codeFXML = FXMLTable.getInstance().get("Student/Code");
        
        try {
            FXMLLoader dashboardLoader = new FXMLLoader(getClass().getResource(dashboardFXML));
            FXMLLoader coursesLoader = new FXMLLoader(getClass().getResource(coursesFXML));
            FXMLLoader ongoingExamsLoader = new FXMLLoader(getClass().getResource(ongoingExamsFXML));
            FXMLLoader messagesLoader = new FXMLLoader(getClass().getResource(messagesFXML));
            FXMLLoader codeLoader = new FXMLLoader(getClass().getResource(codeFXML));
            
            pages.put("Dashboard", dashboardLoader.load());
            pages.put("Courses", coursesLoader.load());
            pages.put("OngoingExams", ongoingExamsLoader.load());
            pages.put("Messages", messagesLoader.load());
            pages.put("Code", codeLoader.load());
            
            controllers.put("Dashboard", dashboardLoader.getController());
            controllers.put("Courses", coursesLoader.getController());
            controllers.put("OngoingExams", ongoingExamsLoader.getController());
            controllers.put("Messages", messagesLoader.getController());
            controllers.put("Code", codeLoader.getController());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    
    @FXML
    public void showUserMenu(ActionEvent event) {
        userMenu.show(userMenuBtn);
    }
    
    private void logout() {
        ConfirmDialog confirm = new ConfirmDialog(
                "Logout",
                "You are about to be logged out. Are you sure?"
        );
        
        confirm.getConfirmBtn().setOnAction((ActionEvent e) -> {
            Response logout = Session.getInstance().getCurrentUser().logout();
            
            if (logout.success()) {
                String loginFXML = FXMLTable.getInstance().get("Login");
                Utils.loadStage(new FXMLLoader(getClass().getResource(loginFXML)));
                userMenuBtn.getScene().getWindow().hide();
                
                Session.getInstance().clear();
                EventManager.getInstance().clearEventHandlers();
                SocketServer.getInstance().shutdown();
            }
        });
        
        confirm.show();
    }
    
    
    private void showPage(Node node) {
        contentPane.getChildren().clear();
        contentPane.getChildren().add(node);
    }
    
    @FXML
    public void showDashboard(ActionEvent event) {
        showPage(pages.get("Dashboard"));
    }
    
    @FXML
    public void showCourses(ActionEvent event) {
        showPage(pages.get("Courses"));
    }
    
    @FXML
    public void showOngoingExams(ActionEvent event) {
        showPage(pages.get("OngoingExams"));
    }
    
    @FXML
    public void showMessages(ActionEvent event) {
        showPage(pages.get("Messages"));
    }
    
    @FXML
    public void showCode(ActionEvent event) {
        showPage(pages.get("Code"));
    }
    
    
    public Map<String, Object> getControllers() {
        return controllers;
    }
    
    
    @Override
    public AnchorPane getMainPane() {
        return mainPane;
    }
    
    @Override
    public StackPane getDialogPane() {
        return dialogPane;
    }
    
}