package com.hacklympics.api.session;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.hacklympics.api.material.Exam;
import com.hacklympics.api.user.User;
import javafx.collections.ListChangeListener;

public class Session {
    
    private static Session session;
    
    private ObservableList<User> onlineUsers;
    private UserController mainController;
    private User currentUser;
    private Exam currentExam;
    
    private Session() {
        onlineUsers = FXCollections.observableArrayList();
        onlineUsers.addListener((ListChangeListener.Change<? extends User> c) -> {
            mainController.updateOnlineUserList();
        });
    }
    
    public static Session getInstance() {
        if (session == null) {
            session = new Session();
        }
        
        return session;
    }
    
    
    public ObservableList<User> getOnlineUsers() {
        return onlineUsers;
    }
    
    public UserController getMainController() {
        return mainController;
    }
    
    public User getCurrentUser() {
        return currentUser;
    }
    
    public Exam getCurrentExam() {
        return currentExam;
    }
    
    
    public void setMainController(UserController mainController) {
        this.mainController = mainController;
    }
    
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
    
    public void setCurrentExam(Exam exam) {
        this.currentExam = exam;
    }
    
}
