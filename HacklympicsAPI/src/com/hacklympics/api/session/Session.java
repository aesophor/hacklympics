package com.hacklympics.api.session;

import com.hacklympics.api.materials.Exam;
import com.hacklympics.api.users.User;

public class Session {
    
    private static Session session;
    private Object mainController;
    private User currentUser;
    private Exam currentExam;
    
    private Session() {
        
    }
    
    public static Session getInstance() {
        if (session == null) {
            session = new Session();
        }
        
        return session;
    }
    
    
    public Object getMainController() {
        return mainController;
    }
    
    public User getCurrentUser() {
        return currentUser;
    }
    
    public Exam getCurrentExam() {
        return currentExam;
    }
    
    
    public void setMainController(Object mainController) {
        this.mainController = mainController;
    }
    
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
    
    public void setCurrentExam(Exam exam) {
        this.currentExam = exam;
    }
    
}
