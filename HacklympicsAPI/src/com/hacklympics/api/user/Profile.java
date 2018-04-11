package com.hacklympics.api.user;

public class Profile {
    
    private String username;
    private String fullname;
    private int gradYear;
    
    public Profile(String username, String fullname, String gradYear) {
        this.username = username;
        this.fullname = fullname;
        this.gradYear = (int) Double.parseDouble(gradYear);
    }
    
    
    public String getUsername() {
        return username;
    }
    
    public String getFullname() {
        return fullname;
    }
    
    public int getGradYear() {
        return gradYear;
    }

    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
    
    public void setGradYear(int gradYear) {
        this.gradYear = gradYear;
    }
    
    @Override
    public String toString() {
        return String.format("[%s] %s (%s)", username, fullname, gradYear);
    }
    
}