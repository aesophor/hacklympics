package com.hacklympics.api.users;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class UserProfile {
    
    private SimpleStringProperty username;
    private SimpleStringProperty fullname;
    private SimpleIntegerProperty gradYear;
    
    public UserProfile(String username, String fullname, int gradYear) {
        this.username = new SimpleStringProperty(username);
        this.fullname = new SimpleStringProperty(fullname);
        this.gradYear = new SimpleIntegerProperty(gradYear);
    }
    
    
    public String getUsername() {
        return username.get();
    }
    
    public String getFullname() {
        return fullname.get();
    }
    
    public Integer getGradYear() {
        return gradYear.get();
    }

    
    public void setUsername(String username) {
        this.username.set(username);
    }
    
    public void setFullname(String fullname) {
        this.fullname.set(fullname);
    }
    
    public void setGradYear(int gradYear) {
        this.gradYear.set(gradYear);
    }
    
    
    public SimpleStringProperty usernameProperty() {
        return username;
    }
    
    public SimpleStringProperty fullnameProperty() {
        return fullname;
    }
    
    public SimpleIntegerProperty gradYearProperty() {
        return gradYear;
    }
    
    
    @Override
    public String toString() {
        return String.format("%s", getFullname());
    }
    
}