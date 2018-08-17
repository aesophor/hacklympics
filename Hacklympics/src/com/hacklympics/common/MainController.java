package com.hacklympics.common;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Map;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.AnchorPane;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import com.hacklympics.api.communication.Response;
import com.hacklympics.api.communication.SocketServer;
import com.hacklympics.api.event.EventManager;
import com.hacklympics.api.session.Session;
import com.hacklympics.api.session.UserController;
import com.hacklympics.api.user.User;
import com.hacklympics.common.MainController;
import com.hacklympics.utility.FXMLTable;
import com.hacklympics.utility.Utils;
import com.hacklympics.utility.dialog.AlertDialog;
import com.hacklympics.utility.dialog.ConfirmDialog;
import com.hacklympics.utility.listview.OnlineUserListView;
import com.jfoenix.controls.JFXSnackbar;

public abstract class MainController implements Initializable, UserController {
	
	protected Map<String, AnchorPane> pages;
	protected Map<String, Object> controllers;
    
    protected UserMenu userMenu;
    
    @FXML
    protected AnchorPane mainPane;
    @FXML
    protected AnchorPane onlineUserPane;
    @FXML
    protected AnchorPane contentPane;
    @FXML
    protected StackPane dialogPane;
    @FXML
    protected Button userMenuBtn;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initPages();
        showPage(pages.get("Dashboard"));
        
        
        // Initialize OnlineUserListView.
        onlineUserPane.getChildren().add(new OnlineUserListView());
        
        // Initialize user menu.
        User currentUser = Session.getInstance().getCurrentUser();
        userMenuBtn.setText(String.format("%s ", currentUser.getFullname()));
        
        userMenu = new UserMenu();
        
        userMenu.add(new Label("Profile"), (MouseEvent event) -> {
            userMenu.hide();
        });
        
        userMenu.add(new Label("Settings"), (MouseEvent event) -> {
            userMenu.hide();
        });
        
        userMenu.add(new Label("About"), (MouseEvent event) -> {
        	userMenu.hide();
        	
        	String aboutFXML = FXMLTable.getInstance().get("About");
            Scene aboutScene = Utils.loadStage(new FXMLLoader(getClass().getResource(aboutFXML)));
            
        	AlertDialog aboutDialog = new AlertDialog("About", aboutScene.lookup("#aboutPane"));
        	aboutDialog.show();
        });
        
        userMenu.add(new Label("Logout"), (MouseEvent event) -> {
            userMenu.hide();
            logout();
        });
    }
	
	protected abstract void initPages();
	
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
                Utils.showStage(new FXMLLoader(getClass().getResource(loginFXML)));
                userMenuBtn.getScene().getWindow().hide();
                
                Session.getInstance().clear();
                EventManager.getInstance().clearEventHandlers();
                SocketServer.getInstance().shutdown();
            }
        });
        
        confirm.show();
    }
    
    
    protected void showPage(Node node) {
        contentPane.getChildren().clear();
        contentPane.getChildren().add(node);
    }
    
    @FXML
    protected void showDashboard(ActionEvent event) {
        showPage(pages.get("Dashboard"));
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

	@Override
	public void pushNotification(String message) {
		Platform.runLater(() -> {
    		new JFXSnackbar(mainPane).show(message, 5000);
    	});
	}

}