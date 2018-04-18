package com.hacklympics.api.materials;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ProblemData {
    
    private final SimpleIntegerProperty courseID;
    private final SimpleIntegerProperty examID;
    private final SimpleIntegerProperty problemID;
    private SimpleStringProperty title;
    private SimpleStringProperty desc;
    
    public ProblemData(int courseID, int examID, int problemID, String title, String desc) {
        this.courseID = new SimpleIntegerProperty(courseID);
        this.examID = new SimpleIntegerProperty(examID);
        this.problemID = new SimpleIntegerProperty(problemID);
        this.title = new SimpleStringProperty(title);
        this.desc = new SimpleStringProperty(desc);
    }
    
    
    public Integer getCourseID() {
        return courseID.get();
    }
    
    public Integer getExamID() {
        return examID.get();
    }
    
    public Integer getProblemID() {
        return problemID.get();
    }
    
    public String getTitle() {
        return title.get();
    }
    
    public String getDesc() {
        return desc.get();
    }
    
    
    public void setTitle(String title) {
        this.title.set(title);
    }
    
    public void setDesc(String desc) {
        this.desc.set(desc);
    }
    
    
    public SimpleIntegerProperty courseIDProperty() {
        return courseID;
    }
    
    public SimpleIntegerProperty examIDProperty() {
        return examID;
    }
    
    public SimpleIntegerProperty problemIDProperty() {
        return problemID;
    }
    
    public SimpleStringProperty titleProperty() {
        return title;
    }
    
    public SimpleStringProperty descProperty() {
        return desc;
    }
    
    
    @Override
    public String toString() {
        return String.format("");
    }
}
