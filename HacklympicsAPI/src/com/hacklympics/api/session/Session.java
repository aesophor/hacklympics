package com.hacklympics.api.session;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import com.hacklympics.api.material.Exam;
import com.hacklympics.api.user.User;

public class Session {
    
    public interface MainController {
        
    }
    
    
    private static Session session;
    
    private ExecutorService executor;
    private MainController mainController;
    private User currentUser;
    private Exam currentExam;
    
    private Session() {
        executor = Executors.newCachedThreadPool();
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
    
    public MainController getMainController() {
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