package com.hacklympics.api.session;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import com.hacklympics.api.material.Exam;
import com.hacklympics.api.user.User;

public class Session {
    
    private static Session session;
    private ExecutorService executor;
    private UserController mainController;
    private User currentUser;
    private Exam currentExam;
    private boolean showNotification;
    
    private Session() {
        executor = Executors.newCachedThreadPool();
        
        showNotification = true;
    }
    
    public static Session getInstance() {
        if (session == null) {
            session = new Session();
        }
        
        return session;
    }
    
    
    public void clear() {
        mainController = null;
        currentUser = null;
        currentExam = null;
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
    
    public ExecutorService getExecutor() {
        return executor;
    }
    
    public boolean showNotification() {
        return showNotification;
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
    
    public void setShowNotification(boolean option) {
        showNotification = option;
    }
    
    
    public boolean isInExam() {
        return this.currentExam != null;
    }
    
}