package com.hacklympics.api.session;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ListChangeListener;
import com.hacklympics.api.material.Exam;
import com.hacklympics.api.user.User;

public class Session {
    
    public interface MainController {
        public void updateOnlineUserList();
    }
    
    private static Session session;
    
    private ObservableList<User> onlineUsers;
    private MainController mainController;
    private User currentUser;
    private Exam currentExam;
    
    private Session() {
        clear();
    }
    
    public static Session getInstance() {
        if (session == null) {
            session = new Session();
        }
        
        return session;
    }
    
    public void clear() {
        onlineUsers = FXCollections.observableArrayList();
        onlineUsers.setAll(User.getOnlineUsers());
        
        onlineUsers.addListener((ListChangeListener.Change<? extends User> c) -> {
            mainController.updateOnlineUserList();
        });
    }
    
    
    public ObservableList<User> getOnlineUsers() {
        return onlineUsers;
    }
    
    public MainController getMainController() {
        return mainController;
    }
    
    public User getCurrentUser() {
        return currentUser;
    }
    
    public Exam getCurrentExam() {
        return currentExam;
    }
    
    
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
    
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
    
    public void setCurrentExam(Exam exam) {
        this.currentExam = exam;
    }
    
}