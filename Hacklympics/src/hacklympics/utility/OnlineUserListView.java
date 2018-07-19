package hacklympics.utility;

import com.hacklympics.api.event.EventHandler;
import com.hacklympics.api.event.EventManager;
import com.hacklympics.api.event.EventType;
import com.hacklympics.api.event.user.LoginEvent;
import com.hacklympics.api.event.user.LogoutEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ListChangeListener;
import com.jfoenix.controls.JFXListView;
import com.hacklympics.api.user.User;
import javafx.application.Platform;

public class OnlineUserListView extends JFXListView {
    
    private static final int LIST_VIEW_WIDTH = 200;
    private static final int LIST_VIEW_HEIGHT = 175;
    
    private final ObservableList<User> users;
    
    public OnlineUserListView() {
        this.getStyleClass().add("online-user-list");
        this.setMouseTransparent(true);
        
        this.setPrefWidth(LIST_VIEW_WIDTH);
        this.setPrefHeight(LIST_VIEW_HEIGHT);
        
        
        this.users = FXCollections.observableArrayList();
        this.users.addListener((ListChangeListener.Change<? extends User> c) -> {
            this.update();
        });
        this.users.addAll(User.getOnlineUsers());
        
        
        // Update the Online User List (on the bottom left) whenever
        // the SocketServer receives events of user login/logout.
        this.setOnLogin((LoginEvent event) -> {
            Platform.runLater(() -> {
                this.add(event.getLoggedInUser());
            });
        });
                
        this.setOnLogout((LogoutEvent event) -> {
            Platform.runLater(() -> {
                this.remove(event.getLoggedOutUser());
            });
        });
    }
    
    
    public void add(User user) {
        this.users.add(user);
    }
    
    public void remove(User user) {
        this.users.remove(user);
    }
    
    private void update() {
        this.getItems().setAll(this.users);
    }
    
    
    private void setOnLogin(EventHandler<LoginEvent> listener) {
        EventManager.getInstance().addEventHandler(EventType.LOGIN, listener);
    }
    
    private void setOnLogout(EventHandler<LogoutEvent> listener) {
        EventManager.getInstance().addEventHandler(EventType.LOGOUT, listener);
    }
    
}