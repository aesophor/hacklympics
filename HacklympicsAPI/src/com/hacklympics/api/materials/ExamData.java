package com.hacklympics.api.materials;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ExamData {
    
    private final SimpleIntegerProperty courseID;
    private final SimpleIntegerProperty examID;
    
    private SimpleStringProperty title;
    private SimpleStringProperty desc;
    private SimpleIntegerProperty duration;
    
    public ExamData(int courseID, int examID, String title, String desc, int duration) {
        this.courseID = new SimpleIntegerProperty(courseID);
        this.examID = new SimpleIntegerProperty(examID);
        
        this.title = new SimpleStringProperty(title);
        this.desc = new SimpleStringProperty(desc);
        this.duration = new SimpleIntegerProperty(duration);
    }
    
    
    public Integer getCourseID() {
        return courseID.get();
    }
    
    public Integer getExamID() {
        return examID.get();
    }
    
    public String getTitle() {
        return title.get();
    }
    
    public String getDesc() {
        return desc.get();
    }
    
    public Integer getDuration() {
        return duration.get();
    }
    
    
    public void setTitle(String title) {
        this.title.set(title);
    }
    
    public void setDesc(String desc) {
        this.desc.set(desc);
    }
    
    public void setDuration(int duration) {
        this.duration.set(duration);
    }
    
    
    public SimpleIntegerProperty courseIDProperty() {
        return courseID;
    }
    
    public SimpleIntegerProperty examIDProperty() {
        return examID;
    }
    
    public SimpleStringProperty titleProperty() {
        return title;
    }
    
    public SimpleStringProperty descProperty() {
        return desc;
    }
    
    public SimpleIntegerProperty durationProperty() {
        return duration;
    }
    
    
    @Override
    public String toString() {
        return title.get();
    }
}
