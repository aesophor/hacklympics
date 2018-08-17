package com.hacklympics.utility.listview;

import com.hacklympics.api.event.EventHandler;
import com.hacklympics.api.event.EventManager;
import com.hacklympics.api.event.EventType;
import com.hacklympics.api.event.user.LoginEvent;
import com.hacklympics.api.event.user.LogoutEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.jfoenix.controls.JFXListView;
import com.hacklympics.api.user.User;
import javafx.application.Platform;

public class OnlineUserListView extends JFXListView<User> {
    
    private static final int LIST_VIEW_WIDTH = 200;
    private static final int LIST_VIEW_HEIGHT = 175;
    
    private final ObservableList<User> users;
    
    public OnlineUserListView() {
        getStyleClass().add("online-user-list");
        setMouseTransparent(true);
        setPrefWidth(LIST_VIEW_WIDTH);
        setPrefHeight(LIST_VIEW_HEIGHT);
        
        
        users = FXCollections.observableArrayList();
        users.addAll(User.getOnlineUsers());
        setItems(users);
        
        // Update the Online User List (on the bottom left) whenever
        // the SocketServer receives events of user login/logout.
        setOnLogin((LoginEvent event) -> {
            Platform.runLater(() -> {
                users.add(event.getLoggedInUser());
            });
        });
                
        setOnLogout((LogoutEvent event) -> {
            Platform.runLater(() -> {
                users.remove(event.getLoggedOutUser());
            });
        });
    }
    
    
    private void setOnLogin(EventHandler<LoginEvent> listener) {
        EventManager.getInstance().addEventHandler(EventType.LOGIN, listener);
    }
    
    private void setOnLogout(EventHandler<LogoutEvent> listener) {
        EventManager.getInstance().addEventHandler(EventType.LOGOUT, listener);
    }
    
}